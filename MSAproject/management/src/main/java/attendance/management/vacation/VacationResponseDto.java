package attendance.management.vacation;

import lombok.Data;

@Data
public class VacationResponseDto {
    private Long idx;
    private String user;
    private String lecture;
    private String personalNum;
    private String reason;
    private String date;
    private String wdate;
    private String phonecall;
    private String accept;
    private String hwpfile;
}
