package com.management.ma_project.attendance;

import lombok.Data;

@Data
public class AttendanceReqApproveDto {
    private Long useridx; // 사용자 ID
    private String adate; // 날짜
    private String type; // 출결 유형
    private Boolean approval; // 승인 여부
}
