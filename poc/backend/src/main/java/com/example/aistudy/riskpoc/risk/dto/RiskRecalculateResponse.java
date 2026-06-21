package com.example.aistudy.riskpoc.risk.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record RiskRecalculateResponse(
        String customerNo,
        String riskLevel,
        BigDecimal riskScore,
        String riskReasonSample,
        String ruleCode,
        String sourceType,
        String calculatedBy,
        OffsetDateTime calculatedAt,
        TransactionEffectResponse transactionEffect) {
}
