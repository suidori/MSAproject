package com.green.qna.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "qnaboard")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QnAboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private String title;

    private String content;

    private String type;

    private LocalDateTime wdate;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'WAITING'")
    private QnAState qnastate;

    private String token;

    private String comment;

    private String name;

//    @ManyToOne
    @JoinColumn(name = "commentusername")
    private String commentuser; //


}
