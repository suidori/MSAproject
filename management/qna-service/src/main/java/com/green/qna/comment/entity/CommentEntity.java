package com.green.qna.comment.entity;

import com.green.qna.qna.entity.QnAboard;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "comment")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private String comment;

    private LocalDateTime wdate;

    private String uuid;

    private String name;

    private String role;

    @ManyToOne
    @JoinColumn(name = "qnaboard_idx")
    private QnAboard qnaboard;

}
