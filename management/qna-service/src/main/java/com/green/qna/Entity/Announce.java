package com.green.qna.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "announce")
@Data
@NoArgsConstructor
@AllArgsConstructor
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


//    @ManyToOne(cascade = CascadeType.ALL)
//    @JsonIgnore
//    private User user; // User와 양방향 맵핑

}
