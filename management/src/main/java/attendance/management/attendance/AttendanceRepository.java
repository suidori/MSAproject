package attendance.management.attendance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByLecture_IdxAndAdate(Long lectureIdx, LocalDate adate);
    Optional<Attendance> findByUser_IdxAndAdateAndType(Long userId, LocalDate adate, String type);

    @Query("SELECT a FROM Attendance a WHERE a.user.idx = :useridx AND FUNCTION('DATE_FORMAT', a.adate, '%Y-%m') = :adate")
    List<Attendance> findByUser_IdxAndAdate(@Param("useridx") Long userIdx, @Param("adate") String yearMonth);

    @Query("SELECT a FROM Attendance a WHERE a.lecture.idx = :lectureidx AND FUNCTION('DATE_FORMAT', a.adate, '%Y-%m') = :adate")
    List<Attendance> findByLecture_IdxAndAdate(@Param("lectureidx") Long lectureIdx, @Param("adate") String adate);

    Optional<Attendance> findAttendanceByUserIdxAndAdateAndType(Long useridx, LocalDate adate, String type);
}
