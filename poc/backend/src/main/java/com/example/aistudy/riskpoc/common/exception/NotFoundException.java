package com.example.aistudy.riskpoc.common.exception;

import com.example.aistudy.riskpoc.common.error.ErrorCode;

public class NotFoundException extends BusinessException {
    public NotFoundException(String message) {
        super(ErrorCode.CUSTOMER_NOT_FOUND, message);
    }
}
