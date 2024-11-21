package attendance.management.userandlecture;

import attendance.management.jwt.JWTManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAndLectureService {

    private final UserAndLectureRepository userAndLectureRepository;
    private final JWTManager jwtManager;
    private final ModelMapper modelMapper;

    public UserAndLectureResponsePageDto userlist(Pageable pageable, String token) {

        Long userIdx = jwtManager.extractUserIdxFromToken(token);

        Page<UserAndLecture> page = userAndLectureRepository.findByUser_Idx(userIdx, pageable);
        List<UserAndLectureResponseDto> filteredList = page.getContent().stream()
                .map(userAndLecture -> {
                    UserAndLectureResponseDto userAndLectureResponseDto = modelMapper.map(userAndLecture, UserAndLectureResponseDto.class);
                    userAndLectureResponseDto.setUser(userAndLecture.getUser().getName());
                    userAndLectureResponseDto.setLecture(userAndLecture.getLecture().getTitle());
                    userAndLectureResponseDto.setStartDate(userAndLecture.getLecture().getStartDate().toString());
                    userAndLectureResponseDto.setEndDate(userAndLecture.getLecture().getEndDate().toString());
                    userAndLectureResponseDto.setState(userAndLecture.getState()==1 ? "수강" : "미수강");
                    return userAndLectureResponseDto;
                })
                .collect(Collectors.toList());

        UserAndLectureResponsePageDto userAndLectureResponsePageDto = modelMapper.map(page, UserAndLectureResponsePageDto.class);
        userAndLectureResponsePageDto.setList(filteredList);
        userAndLectureResponsePageDto.setTotalElements(page.getTotalElements());

        return userAndLectureResponsePageDto;
    }

    public List<UserAndLectureResponseListDto> findAll() {
        List<UserAndLecture> userAndLectures = userAndLectureRepository.findByState(1);

        return userAndLectures.stream()
                .map(userAndLecture -> {
                    UserAndLectureResponseListDto responseDto = new UserAndLectureResponseListDto();
                    responseDto.setUser(userAndLecture.getUser().getIdx()); // User의 idx 추출
                    responseDto.setLecture(userAndLecture.getLecture().getIdx()); // Lecture의 idx 추출
                    return responseDto;
                })
                .collect(Collectors.toList());
    }
}
