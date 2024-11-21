package attendance.management.userandlecture;

import attendance.management.lecture.Lecture;
import attendance.management.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "userandlecture")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAndLecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "lecture")
    private Lecture lecture;

    private int state;

}
