package com.whatthefork.resourcereservation.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 자원 관련 오류
    RESOURCE_NOT_FOUND("10001", "해당 자원 찾을 수 없음", HttpStatus.NOT_FOUND);

    ErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
