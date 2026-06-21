package com.example.aistudy.riskpoc.common.api;

import com.example.aistudy.riskpoc.common.context.RequestContextHolder;
import com.example.aistudy.riskpoc.common.error.ErrorCode;
import com.example.aistudy.riskpoc.common.util.TimeUtil;
import java.time.OffsetDateTime;

public record ApiResponse<T>(
        boolean success,
        String code,
        String message,
        T data,
        String requestId,
        OffsetDateTime timestamp) {

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, ErrorCode.SUCCESS.code(), "success", data,
                RequestContextHolder.requestId(), TimeUtil.nowShanghai());
    }

    public static <T> ApiResponse<T> error(ErrorCode errorCode, String message) {
        return new ApiResponse<>(false, errorCode.code(), message, null,
                RequestContextHolder.requestId(), TimeUtil.nowShanghai());
    }
}
