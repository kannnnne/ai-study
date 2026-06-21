package com.example.aistudy.riskpoc.common.exception;

import com.example.aistudy.riskpoc.common.error.ErrorCode;

public class ConflictException extends BusinessException {
    public ConflictException(String message) {
        super(ErrorCode.BUSINESS_CONFLICT, message);
    }
}
