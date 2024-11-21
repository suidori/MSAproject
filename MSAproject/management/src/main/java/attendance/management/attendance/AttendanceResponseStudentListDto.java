package attendance.management.attendance;

import lombok.Data;

@Data
public class AttendanceResponseStudentListDto {
    private Long idx;
    private String user;
    private String type;
    private String reason;
    private String lecture;
    private String teacheraccept;
    private String manageraccept;
    private String approval;
}
