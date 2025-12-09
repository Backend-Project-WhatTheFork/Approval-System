package com.whatthefork.resourcereservation.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 자원 존재하지 않음
    RESOURCE_NOT_FOUND("10001", "해당 자원 찾을 수 없음", HttpStatus.NOT_FOUND),

    // 중복 예약
    REDUNDANT_RESERVATION("10002", "해당 자원은 이미 예약되어 있음", HttpStatus.CONFLICT),

    // 예약 동시 접속
    SIMULTANEOUS_CONNECTION("10003", "이미 누군가 예약중", HttpStatus.BAD_REQUEST),

    // 비품 개수 모자람
    NOT_ENOUGH_CAPACITY("10003", "비품의 재고 없음", HttpStatus.BAD_REQUEST),

    // 권한 없음
    NOT_ENOUGH_AUTHORITY("10004", "권한 부족", HttpStatus.BAD_REQUEST);

    ErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
