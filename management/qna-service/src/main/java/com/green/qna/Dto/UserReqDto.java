package com.green.qna.Dto;

import com.green.qna.Entity.Role;
import lombok.Data;

@Data
public class UserReqDto {

    private String idx;

    private String useridx;

    private String password;

    private String name;

    private String phoneNumber;

    private String email;

    private Role role;

    private boolean accept;



}
