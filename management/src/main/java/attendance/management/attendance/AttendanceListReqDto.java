package attendance.management.attendance;

import attendance.management.user.User;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AttendanceListReqDto {

    private User user;

    private String month;

}
