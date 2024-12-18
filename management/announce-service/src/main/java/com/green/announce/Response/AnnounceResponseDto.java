package com.green.announce.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnounceResponseDto {

    private Long idx;
    private String title;
    private String body;
    private String wdate;
    private String user;
    private String lecture;
}
