package com.green.qna.qna;



import com.green.qna.qna.entity.QnAState;
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

    private String userid;

    private String role;
}
