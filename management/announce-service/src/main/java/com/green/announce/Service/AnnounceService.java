package com.green.announce.Service;

import com.green.announce.Dto.AnnounceReqDto;
import com.green.announce.Entity.Announce;
import com.green.announce.Entity.User;
import com.green.announce.Login.LoginUserDetails;
import com.green.announce.Repository.AnnounceRepository;
import com.green.announce.Repository.LectureRepository;
import com.green.announce.Repository.UserAndLectrueRepository;
import com.green.announce.Repository.UserRepository;
import com.green.announce.Response.AnnounceResponseDto;
import com.green.announce.Response.AnnounceResponsePageDto;
import com.green.announce.error.BizException;
import com.green.announce.error.ErrorCode;
import com.green.announce.jwt.JWTManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnnounceService {

    private final AnnounceRepository announceRepository;
    private final ModelMapper modelMapper;
    private final UserAndLectrueRepository userAndLectureRepository;
    private final JWTManager jwtManager;
    private final UserRepository userRepository;
    private final LectureRepository lectureRepository;



    public Announce save(LoginUserDetails loginUserDetails, AnnounceReqDto announceReqDto) {

        User user = userRepository.findByIdx(loginUserDetails.getIdx()).orElseThrow(()->new BizException(ErrorCode.USER_NOT_FOUND));

        Announce announce = modelMapper.map(announceReqDto, Announce.class);
        announce.setWdate(LocalDateTime.now());
        if(announceReqDto.getLecture()!=null){
            announce.setLecture(lectureRepository.findByIdx(announceReqDto.getLecture()).orElseThrow(()->new BizException(ErrorCode.LECTURE_NOT_FOUND)));
        }
        announce.setUser(user);

        announceRepository.save(announce);
        return announce;
    }

    public AnnounceResponsePageDto studentPage(Pageable pageable, LoginUserDetails loginUserDetails) {
        long lectureIdx = userAndLectureRepository
                .findByUser_IdxAndState(loginUserDetails.getIdx(), 1)
                .orElseThrow(() -> new BizException(ErrorCode.LECTURE_NOT_FOUND))
                .getLecture()
                .getIdx();

        Page<Announce> page = announceRepository.findByLectureIdxOrNull(lectureIdx, pageable);

        return mapToQuestionResponsePageDto(page);
    }

    public AnnounceResponsePageDto teacherPage(Pageable pageable, LoginUserDetails loginUserDetails) {
        long lectureIdx = userAndLectureRepository
                .findByUser_IdxAndState(loginUserDetails.getIdx(), 1)
                .orElseThrow(() -> new BizException(ErrorCode.LECTURE_NOT_FOUND))
                .getLecture()
                .getIdx();

        Page<Announce> page = announceRepository.findByLectureIdxOrNull(lectureIdx, pageable);

        return mapToQuestionResponsePageDto(page);
    }

    public AnnounceResponsePageDto managerPage(Pageable pageable) {

        Page<Announce> page = announceRepository.findAll(pageable);

        return mapToQuestionResponsePageDto(page);
    }

    public AnnounceResponsePageDto lectureSearch(Pageable pageable, Long lecture){
        Page<Announce> page = announceRepository.findByLecture_Idx(lecture, pageable);
        return mapToQuestionResponsePageDto(page);
    }

    public AnnounceResponsePageDto allSearch(Pageable pageable){
        Page<Announce> page = announceRepository.findByLecture_IdxIsNull(pageable);
        return mapToQuestionResponsePageDto(page);
    }

    public AnnounceResponseDto viewPage(long idx) {
        Announce announce = announceRepository.findById(idx).orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND));

        AnnounceResponseDto announceResponseDto = modelMapper.map(announce, AnnounceResponseDto.class);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm");
        announceResponseDto.setWdate(dateTimeFormatter.format(announce.getWdate()));

        announceResponseDto.setUser((announce.getUser() != null) ? announce.getUser().getName() : "탈퇴한 회원");
        announceResponseDto.setLecture((announce.getLecture()!=null)? announce.getLecture().getTitle() : "전체");

        return announceResponseDto;

    }

    private AnnounceResponsePageDto mapToQuestionResponsePageDto(Page<Announce> page) {

        List<AnnounceResponseDto> list = page
                .getContent()
                .stream()
                .map(announce -> {
                    AnnounceResponseDto announceResponseDto = modelMapper.map(announce, AnnounceResponseDto.class);

                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm");
                    announceResponseDto.setWdate(dateTimeFormatter.format(announce.getWdate()));

                    announceResponseDto.setUser((announce.getUser() != null) ? announce.getUser().getName() : "탈퇴한 회원");
                    announceResponseDto.setLecture((announce.getLecture()!=null) ? announce.getLecture().getTitle() : "전체");

                    return announceResponseDto;
                }).toList();

        AnnounceResponsePageDto announceResponsePageDto = modelMapper.map(page, AnnounceResponsePageDto.class);
        announceResponsePageDto.setList(list);

        return announceResponsePageDto;
    }

}
