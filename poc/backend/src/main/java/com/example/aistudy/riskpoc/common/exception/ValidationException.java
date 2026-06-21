package com.example.aistudy.riskpoc.common.exception;

import com.example.aistudy.riskpoc.common.error.ErrorCode;

public class ValidationException extends BusinessException {
    public ValidationException(String message) {
        super(ErrorCode.VALIDATION_FAILED, message);
    }
}
