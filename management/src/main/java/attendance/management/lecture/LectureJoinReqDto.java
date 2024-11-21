package attendance.management.lecture;

import attendance.management.user.UserReqDto;
import lombok.Data;

@Data

public class LectureJoinReqDto {
    private UserReqDto userReqDto;
    private LectureReqDto lectureReqDto;
}
