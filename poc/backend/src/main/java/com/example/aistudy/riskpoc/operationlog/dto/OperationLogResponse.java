package com.example.aistudy.riskpoc.operationlog.dto;

import java.time.OffsetDateTime;

public record OperationLogResponse(
        String requestId,
        String operator,
        String operationType,
        String targetType,
        String targetBizNo,
        String result,
        String errorMessageSample,
        String operationDescSample,
        String clientIpMasked,
        Long durationMs,
        OffsetDateTime createdAt) {
}
