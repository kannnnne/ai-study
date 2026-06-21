package com.example.aistudy.riskpoc.common.error;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    SUCCESS("000000", HttpStatus.OK),
    VALIDATION_FAILED("400001", HttpStatus.BAD_REQUEST),
    CUSTOMER_NOT_FOUND("404001", HttpStatus.NOT_FOUND),
    BUSINESS_CONFLICT("409001", HttpStatus.CONFLICT),
    SYSTEM_ERROR("500000", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final HttpStatus httpStatus;

    ErrorCode(String code, HttpStatus httpStatus) {
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public String code() {
        return code;
    }

    public HttpStatus httpStatus() {
        return httpStatus;
    }
}
