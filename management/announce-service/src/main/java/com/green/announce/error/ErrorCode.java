package com.green.announce.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    DUPLICATE_USERID(HttpStatus.BAD_REQUEST, "아이디 중복입니다."),
    DUPLICATE_USERMAIL(HttpStatus.BAD_REQUEST, "email 중복입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND,"해당되는 게시물이 없습니다."),
    ALREADY_RESPONDED(HttpStatus.BAD_REQUEST, "이미 답변된 게시글입니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "틀린 비밀번호입니다."),
    LECTURE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당되는 강좌가 없습니다."),
    REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당되는 요청이 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"해당하는 사용자가 없습니다."),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다."),
    LECTURE_NOT_ENABLE(HttpStatus.NOT_ACCEPTABLE, "닫힌 강좌입니다."),
    ATTENDANCE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 요청입니다."),

    ;

    private HttpStatus httpStatus;
    private String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
