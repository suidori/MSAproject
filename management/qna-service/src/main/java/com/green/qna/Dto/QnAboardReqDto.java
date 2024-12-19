package com.green.qna.Dto;



import com.green.qna.Entity.QnASecret;
import com.green.qna.Entity.QnAstate;
//import com.green.qna.Entity.User;
import lombok.Data;


@Data
public class QnAboardReqDto {

    private String title;

    private String content;

    private String type;

    private QnAstate qnAstate;

    private QnASecret qnASecret;

    private String comment;

    private String commentuser;


}
