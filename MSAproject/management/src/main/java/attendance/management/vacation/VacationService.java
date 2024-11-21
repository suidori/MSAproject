package attendance.management.vacation;

import attendance.management.attendance.AttendanceService;
import attendance.management.error.BizException;
import attendance.management.error.ErrorCode;
import attendance.management.jwt.JWTManager;
import attendance.management.sign.LoginUserDetails;
import attendance.management.user.User;
import attendance.management.user.UserRepository;
import attendance.management.userandlecture.UserAndLecture;
import attendance.management.userandlecture.UserAndLectureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VacationService {

    private final VacationRepository vacationRepository;
    private final ModelMapper modelMapper;
    private final VacationFileEditor vacationFileEditor;
    private final UserAndLectureRepository userAndLectureRepository;
    private final AttendanceService attendanceService;

    public VacationResponseDto request(VacationReqDto vacationReqDto, LoginUserDetails loginUserDetails) {
        Vacation vacation = modelMapper.map(vacationReqDto, Vacation.class);

        Optional<UserAndLecture> userAndLecture = userAndLectureRepository.findByUser_IdxAndState(loginUserDetails.getIdx(), 1);
        userAndLecture.orElseThrow(() -> new BizException(ErrorCode.USER_NOT_FOUND));
        vacation.setLecture(userAndLecture.get().getLecture());
        vacation.setUser(userAndLecture.get().getUser());
        vacation.setWdate(LocalDate.now());
        vacation.setDate(LocalDate.parse(vacationReqDto.getDate()));
        vacationRepository.save(vacation);

        VacationResponseDto vacationResponseDto = modelMapper.map(vacationReqDto, VacationResponseDto.class);
        vacationResponseDto.setIdx(vacation.getIdx());
        vacationResponseDto.setUser(vacation.getUser().getName());
        vacationResponseDto.setLecture(vacation.getLecture().getTitle());
        vacationResponseDto.setWdate(vacation.getWdate().toString());
        if (vacation.getAccept() == null) {
            vacationResponseDto.setAccept("대기중");
        } else {
            vacationResponseDto.setAccept(vacation.getAccept() ? "허가됨" : "반려됨");
        }
        return vacationResponseDto;
    }

    public VacationResponseDto viewPage(long idx) {
        Vacation vacation = vacationRepository.findById(idx).orElseThrow(() -> new BizException(ErrorCode.REQUEST_NOT_FOUND));
        VacationResponseDto vacationResponseDto = modelMapper.map(vacation, VacationResponseDto.class);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yy/MM/dd");
        vacationResponseDto.setWdate(dateTimeFormatter.format(vacation.getWdate()));
        vacationResponseDto.setDate(dateTimeFormatter.format(vacation.getDate()));
        if (vacation.getAccept() == null) {
            vacationResponseDto.setAccept("대기중");
        } else {
            vacationResponseDto.setAccept(vacation.getAccept() ? "허가됨" : "반려됨");
        }
        vacationResponseDto.setUser(vacation.getUser().getName());
        vacationResponseDto.setLecture(vacation.getLecture().getTitle());

        return vacationResponseDto;
    }

    public void accept(long idx) {
        Vacation vacation = vacationRepository
                .findById(idx)
                .orElseThrow(() -> new BizException(ErrorCode.REQUEST_NOT_FOUND));
        vacation.setAccept(true);
        vacationRepository.save(vacation);
    }

    public void deny(long idx) {
        Vacation vacation = vacationRepository
                .findById(idx)
                .orElseThrow(() -> new BizException(ErrorCode.REQUEST_NOT_FOUND));
        vacation.setAccept(false);
        vacationRepository.save(vacation);
    }

    public String newHWP(long idx) throws Exception {
        return vacationFileEditor.newHWP(idx);
    }

    public VacationFileDto download(long idx) throws IOException {
        Vacation vacation = vacationRepository.findById(idx).orElseThrow(()->new BizException(ErrorCode.REQUEST_NOT_FOUND));
        String fileName = vacation.getHwpfile();
        Path filePath = Paths.get("request_hwp" + File.separator + vacation.getLecture().getTitle(), fileName);
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            throw new BizException(ErrorCode.FILE_NOT_FOUND);
        }

        return new VacationFileDto(resource, fileName);
    }

    public VacationResponsePageDto studentPage(Pageable pageable, Long userIdx) {

        Page<Vacation> page = vacationRepository.findByUser_Idx(userIdx, pageable);

        return mapToVacationResponsePageDto(page);
    }

    public VacationResponsePageDto managerPage(Pageable pageable) {

        Page<Vacation> page = vacationRepository.findAll(pageable);

        return mapToVacationResponsePageDto(page);
    }

    private VacationResponsePageDto mapToVacationResponsePageDto(Page<Vacation> page) {
        List<VacationResponseDto> filteredList = page.getContent().stream()
                .map(vacation -> {
                    VacationResponseDto vacationResponseDto = modelMapper.map(vacation, VacationResponseDto.class);
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yy/MM/dd");
                    vacationResponseDto.setWdate(dateTimeFormatter.format(vacation.getWdate()));
                    vacationResponseDto.setDate(dateTimeFormatter.format(vacation.getDate()));
                    vacationResponseDto.setUser(vacation.getUser().getName());
                    vacationResponseDto.setLecture(vacation.getLecture().getTitle());
                    if (vacation.getAccept() == null) {
                        vacationResponseDto.setAccept("대기중");
                    } else {
                        vacationResponseDto.setAccept(vacation.getAccept() ? "허가됨" : "반려됨");
                    }
                    return vacationResponseDto;
                })
                .collect(Collectors.toList());

        VacationResponsePageDto vacationResponsePageDto = modelMapper.map(page, VacationResponsePageDto.class);
        vacationResponsePageDto.setList(filteredList);
        vacationResponsePageDto.setTotalElements(page.getTotalElements());

        return vacationResponsePageDto;
    }


    public VacationResponsePageDto managerSearch(Pageable pageable, String name) {
        Page<Vacation> page = vacationRepository.findByUserName(name, pageable);

        return mapToVacationResponsePageDto(page);

    }

    public VacationResponsePageDto managerUnChecked(Pageable pageable) {
        Page<Vacation> page = vacationRepository.findByAccept(null, pageable);
        return mapToVacationResponsePageDto(page);
    }

    public VacationResponsePageDto studentUnchecked(Pageable pageable, Long idx) {
        Page<Vacation> page = vacationRepository.findByAcceptAndUser_Idx(null, idx, pageable);
        return mapToVacationResponsePageDto(page);
    }

    public void savevacation(long idx) {
        Vacation vacation = vacationRepository.findById(idx).orElseThrow(()->new BizException(ErrorCode.REQUEST_NOT_FOUND));
        attendanceService.saveVacation(vacation);
    }

    public VacationResponsePageDto teacherPage(Pageable pageable, LoginUserDetails loginUserDetails) {

        UserAndLecture userAndLecture = userAndLectureRepository
                .findByUser_IdxAndState(loginUserDetails.getIdx(), 1)
                .orElseThrow(()->new BizException(ErrorCode.LECTURE_NOT_FOUND));

        Page<Vacation> page = vacationRepository.findByLecture_Idx(userAndLecture.getLecture().getIdx(), pageable);
        return mapToVacationResponsePageDto(page);

    }
}

