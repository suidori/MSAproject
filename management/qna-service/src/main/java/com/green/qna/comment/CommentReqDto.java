package com.green.qna.comment;

import com.green.qna.qna.entity.QnAboard;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class CommentReqDto {

    // 기본키
    private Long idx;

    private String comment;

    private LocalDateTime wdate;

    private String uuid;

    private String userid;

    private String name;

    private String role;

    // 외래키
    private Long qnaboard_idx;

}
