package com.green.qna.Response;


import com.green.qna.Entity.QnASecret;
import com.green.qna.Entity.QnAstate;
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
    private QnAstate qnastate;
    private QnASecret qnASecret;

    private String Wdate;
    private String user;
    private String comment;
    private String commentuser;
}
