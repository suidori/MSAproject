package com.management.ma_project.attendance;

import com.management.ma_project.user.User;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AttendanceReqDto {

    private String type;

    private String oldtype;

    private String reason;

    private LocalDate adate;

    private User user;

    private Boolean approval;

}