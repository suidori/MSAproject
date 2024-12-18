package com.green.announce.Dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AnnounceReqDto {

    private String title;

    private String body;

    private Long lecture;


}
