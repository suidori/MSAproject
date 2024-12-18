package com.green.announce.Dto;



import com.green.announce.Entity.QnASecret;
import com.green.announce.Entity.QnAstate;
import com.green.announce.Entity.User;
import lombok.Data;


@Data
public class QnAboardReqDto {

    private String title;

    private String content;

    private String type;

    private QnAstate qnAstate;

    private QnASecret qnASecret;

    private String comment;

    private User commentuser;


}
