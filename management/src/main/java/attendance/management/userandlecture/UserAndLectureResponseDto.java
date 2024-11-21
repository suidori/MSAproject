package attendance.management.userandlecture;

import lombok.Data;

@Data
public class UserAndLectureResponseDto {

    private String user;
    private String lecture;
    private String state;
    private String startDate;
    private String endDate;
}
