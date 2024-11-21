package attendance.management.attendance;

import attendance.management.sign.LoginUserDetails;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("attendance")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/getuser")
    public ResponseEntity<List<AttendanceResponseListDto>> studentList(
            @RequestBody AttendanceListReqDto attendanceListReqDto
    ) {
        List<AttendanceResponseListDto> attendanceResponseDtos = attendanceService.studentList(attendanceListReqDto);
        return ResponseEntity.ok(attendanceResponseDtos);
    }

    @PostMapping("/attupdate")
    public ResponseEntity<Attendance> unlogged(@RequestBody AttendanceReqDto attendanceReqDto) {
        Attendance attendance = attendanceService.attadd(attendanceReqDto);
        return ResponseEntity.ok(attendance);
    }

    @DeleteMapping("/attdelete/{idx}")
    public ResponseEntity<Attendance> delete(@PathVariable("idx") long idx) {
        Attendance attendance = attendanceService.attdelete(idx);
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/todayteacherview")
    public ResponseEntity<List<AttendanceResponseStudentListDto>> todayteacherview(
            @AuthenticationPrincipal LoginUserDetails loginUserDetails
    ) {
        if (loginUserDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<AttendanceResponseStudentListDto> attendanceResponseDto = attendanceService.todayteacherview(loginUserDetails);
        return ResponseEntity.ok(attendanceResponseDto);
    }

    @PostMapping("/teacheraccept/{idx}")
    public ResponseEntity<Attendance> teacherAccept(@PathVariable("idx") long idx){
        Attendance attendance = attendanceService.teacherAccept(idx);
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/monthview")
    public ResponseEntity<List<AttendanceResponseMonthDto>> monthview(
            @RequestParam("idx") Long idx,
            @RequestParam("month") String month
    ) {
        List<AttendanceResponseMonthDto> attendanceResponseMonthDtos = attendanceService.monthview(idx, month);

        return ResponseEntity.ok(attendanceResponseMonthDtos);
    }

    @PostMapping("/updateApproval")
    public ResponseEntity<Attendance> updateApproval(@RequestBody AttendanceReqApproveDto attendanceReqApproveDto) {
        Attendance attendance = attendanceService.updateApproval(attendanceReqApproveDto);
        return ResponseEntity.ok(attendance);
    }

}
