package com.green.qna.Response;


import com.green.qna.Entity.QnAState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QnAboardResponseDto {

    private Long idx;
    private String title;
    private String content;
    private String type;

    private QnAState qnaState;

    private String Wdate;
    private String name;
    private String userid;
    private String role;
    private String comment;
    private String commentuser;
}