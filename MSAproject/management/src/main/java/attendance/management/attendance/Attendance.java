package attendance.management.attendance;

import attendance.management.lecture.Lecture;
import attendance.management.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "attendance")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private String type;

    private String reason;

    private LocalDate adate;

    @ManyToOne
    @JoinColumn(name = "useridx")
    private User user;

    @ManyToOne
    @JoinColumn(name = "lectureidx")
    private Lecture lecture;

    private boolean teacheraccept;

    private boolean manageraccept;

    private Boolean approval;

}
