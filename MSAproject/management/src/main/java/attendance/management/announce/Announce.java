package attendance.management.announce;

import attendance.management.lecture.Lecture;
import attendance.management.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "announce")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Announce {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private String title;

    private String body;

    private LocalDateTime wdate;

    @ManyToOne
    @JoinColumn(name = "useridx")
    private User user;

    @ManyToOne
    @JoinColumn(name = "lectureidx")
    private Lecture lecture;

}