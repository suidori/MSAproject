package attendance.management.attendance;

import attendance.management.error.BizException;
import attendance.management.error.ErrorCode;
import attendance.management.jwt.JWTManager;
import attendance.management.lecture.Lecture;
import attendance.management.lecture.LectureRepository;
import attendance.management.sign.LoginUserDetails;
import attendance.management.user.User;
import attendance.management.user.UserRepository;
import attendance.management.userandlecture.UserAndLectureRepository;
import attendance.management.vacation.Vacation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserAndLectureRepository userAndLectureRepository;
    private final JWTManager jwtManager;
    private final LectureRepository lectureRepository;

    public Attendance attadd(AttendanceReqDto attendanceReqDto) {
        User user = userRepository.findByNameAndPhoneNumber(attendanceReqDto.getUser().getName(), attendanceReqDto.getUser().getPhoneNumber()).orElseThrow(()->new BizException(ErrorCode.USER_NOT_FOUND));
        Attendance attendance = attendanceRepository.findByUser_IdxAndAdateAndType(
                user.getIdx(), attendanceReqDto.getAdate(), attendanceReqDto.getOldtype())
                .orElse(null);

        if (attendance == null) {
            attendance = new Attendance();
            attendance.setUser(userRepository
                    .findByNameAndPhoneNumber(
                            attendanceReqDto.getUser().getName(),
                            attendanceReqDto.getUser().getPhoneNumber())
                    .orElseThrow(() -> new BizException(ErrorCode.USER_NOT_FOUND)));
            attendance.setLecture(userAndLectureRepository
                    .findByUser_IdxAndState(attendance.getUser().getIdx(), 1)
                    .orElseThrow(() -> new BizException(ErrorCode.LECTURE_NOT_FOUND)).getLecture());
        }

        attendance.setManageraccept(false);
        attendance.setTeacheraccept(false);
        attendance.setType(attendanceReqDto.getType());
        attendance.setReason(attendanceReqDto.getReason());
        attendance.setApproval(attendanceReqDto.getApproval());
        attendance.setAdate(attendanceReqDto.getAdate());

        attendanceRepository.save(attendance);

        return attendance;
    }

    public List<AttendanceResponseListDto> studentList(AttendanceListReqDto attendanceListReqDto) {
        User user = attendanceListReqDto.getUser();
        user = userRepository.findByNameAndPhoneNumber(user.getName(), user.getPhoneNumber()).orElseThrow(()->new BizException(ErrorCode.USER_NOT_FOUND));

        List<Attendance> attendanceList = attendanceRepository.findByUser_IdxAndAdate(user.getIdx(), attendanceListReqDto.getMonth());

        if(attendanceList.isEmpty()){
            Lecture lecture = userAndLectureRepository.findByUser_IdxAndState(user.getIdx(), 1).orElseThrow(()->new BizException(ErrorCode.LECTURE_NOT_FOUND)).getLecture();
            attendanceList.add(new Attendance(null, null, null, null, null, lecture, false, false, null));
        }

        List<AttendanceResponseListDto> attendanceResponseDtoList = attendanceList
                .stream()
                .map(attendance -> {
                    AttendanceResponseListDto attendanceResponseListDto = modelMapper.map(attendance, AttendanceResponseListDto.class);
                    attendanceResponseListDto.setLecture(attendance.getLecture().getTitle());
                    attendanceResponseListDto.setTeacheraccept(attendance.isTeacheraccept() ? "담당교사 확인 완료" : "담당교사 확인 대기중");
                    attendanceResponseListDto.setManageraccept(attendance.isManageraccept() ? "행정실 확인 완료" : "행정실 확인 대기중");
                    attendanceResponseListDto.setApproval((attendance.getApproval() == null) ? null : (attendance.getApproval() ? "승인됨" : "대기중"));
                    return attendanceResponseListDto;
                }).toList();

        return attendanceResponseDtoList;
    }

    public Attendance attdelete(long idx) {
        Attendance attendance = attendanceRepository.findById(idx).orElseThrow(()->new BizException(ErrorCode.ATTENDANCE_NOT_FOUND));
        attendanceRepository.delete(attendance);
        return attendance;
    }

    public List<AttendanceResponseStudentListDto> todayteacherview(LoginUserDetails loginUserDetails) {
        User user = userRepository.findById(loginUserDetails.getIdx()).orElseThrow(()->new BizException(ErrorCode.USER_NOT_FOUND));
        Lecture lecture = userAndLectureRepository.findByUser_IdxAndState(user.getIdx(), 1).orElseThrow(()->new BizException(ErrorCode.LECTURE_NOT_FOUND)).getLecture();

        List<Attendance> list = attendanceRepository.findByLecture_IdxAndAdate(lecture.getIdx(), LocalDate.now());
        List<AttendanceResponseStudentListDto> attendanceResponseStudentListDtos
                = list
                .stream()
                .map(attendance -> {
                    AttendanceResponseStudentListDto attendanceResponseStudentListDto = modelMapper.map(attendance, AttendanceResponseStudentListDto.class);
                    attendanceResponseStudentListDto.setUser(attendance.getUser().getName());
                    attendanceResponseStudentListDto.setType(attendance.getType());
                    attendanceResponseStudentListDto.setReason(attendance.getReason());
                    attendanceResponseStudentListDto.setLecture(lecture.getTitle());
                    attendanceResponseStudentListDto.setTeacheraccept(attendance.isTeacheraccept() ? "담당교사 확인 완료" : "담당교사 확인 대기중");
                    attendanceResponseStudentListDto.setManageraccept(attendance.isManageraccept() ? "행정실 확인 완료" : "행정실 확인 대기중");
                    attendanceResponseStudentListDto.setApproval((attendance.getApproval() == null) ? "해당 없음" : (attendance.getApproval() ? "승인됨" : "승인 대기중"));
                    return attendanceResponseStudentListDto;
                }).toList();

        return attendanceResponseStudentListDtos;
    }


    public List<AttendanceResponseMonthDto> monthview(long idx, String month) {
        List<Attendance> list = attendanceRepository.findByLecture_IdxAndAdate(idx, month);
        List<AttendanceResponseMonthDto> attendanceResponseMonthDtos = list
                .stream()
                .map(attendance -> {
                    AttendanceResponseMonthDto attendanceResponseMonthDto = modelMapper.map(attendance, AttendanceResponseMonthDto.class);
                    attendanceResponseMonthDto.setUseridx(attendance.getUser().getIdx());
                    attendanceResponseMonthDto.setUser(attendance.getUser().getName());
                    attendanceResponseMonthDto.setAdate(attendance.getAdate().toString());
                    attendanceResponseMonthDto.setType(attendance.getType());
                    attendanceResponseMonthDto.setApproval((attendance.getApproval() == null) ? null : (attendance.getApproval() ? "true" : "false"));
                    return attendanceResponseMonthDto;
                }).toList();
        return attendanceResponseMonthDtos;
    }

    public Attendance updateApproval(AttendanceReqApproveDto attendanceReqApproveDto) {
        LocalDate adate = LocalDate.parse(attendanceReqApproveDto.getAdate());

        // useridx, adate, type을 사용하여 Attendance 조회
        Attendance attendance = attendanceRepository
                .findAttendanceByUserIdxAndAdateAndType(attendanceReqApproveDto.getUseridx(), adate, attendanceReqApproveDto.getType())
                .orElseThrow(() -> new BizException(ErrorCode.ATTENDANCE_NOT_FOUND));

        attendance.setApproval(attendanceReqApproveDto.getApproval());  // approval 필드 업데이트
        attendanceRepository.save(attendance);

        return attendance;
    }

    public Attendance teacherAccept(long idx) {
        Attendance attendance = attendanceRepository.findById(idx).orElseThrow(()->{throw new BizException(ErrorCode.ATTENDANCE_NOT_FOUND);});
        attendance.setTeacheraccept(true);
        attendanceRepository.save(attendance);
        return attendance;
    }

    public void saveVacation(Vacation vacation) {
        Attendance attendance = new Attendance(null, "휴가", vacation.getReason(), vacation.getDate(),vacation.getUser(),vacation.getLecture(),false,true,true);
        attendanceRepository.save(attendance);
    }
}
