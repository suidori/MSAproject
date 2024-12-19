package com.green.qna.Dto;



import com.green.qna.Entity.QnAState;
//import com.green.qna.Entity.User;
import lombok.Data;


@Data
public class QnAboardReqDto {

    private String title;

    private String content;

    private String type;

    private QnAState qnAState;

    private String comment;

    private String commentuser;

    private String name;
}
