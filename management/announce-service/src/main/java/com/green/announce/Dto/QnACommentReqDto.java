package com.green.announce.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.sql.exec.spi.StandardEntityInstanceResolver;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QnACommentReqDto {

    private String comment;

}
