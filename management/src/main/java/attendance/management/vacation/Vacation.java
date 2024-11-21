package attendance.management.vacation;

import attendance.management.lecture.Lecture;
import attendance.management.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "vacation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vacation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne
    @JoinColumn(name = "useridx")
    private User user;

    @ManyToOne
    @JoinColumn(name = "lecture")
    private Lecture lecture;

    private String personalNum;

    private String reason;

    private LocalDate date;

    private LocalDate wdate;

    private String phonecall;

    private Boolean accept;

    private String hwpfile;

}
