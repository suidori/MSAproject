package com.green.qna.comment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponseDto {

    private Long idx;

    private String content;

    private LocalDateTime wdate;

    private String uuid;

    private String userid;

    private String name;

    private String role;

}
