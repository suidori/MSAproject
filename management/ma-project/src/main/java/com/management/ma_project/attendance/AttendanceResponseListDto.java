package com.management.ma_project.attendance;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AttendanceResponseListDto {

    private Long idx;
    private String type;
    private String reason;
    private LocalDate adate;
    private String lecture;
    private String teacheraccept;
    private String manageraccept;
    private String approval;
}
