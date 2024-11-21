package attendance.management.userandlecture;

import attendance.management.lecture.Lecture;
import attendance.management.user.User;
import lombok.Data;

@Data
public class UserAndLectureReqDto {

    private User user;

    private Lecture lecture;

    private boolean state;

}
