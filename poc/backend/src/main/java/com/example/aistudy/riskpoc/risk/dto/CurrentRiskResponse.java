package com.example.aistudy.riskpoc.risk.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record CurrentRiskResponse(
        String customerNo,
        String customerNameSample,
        String currentRiskLevel,
        BigDecimal currentRiskScore,
        OffsetDateTime lastRiskCalculatedAt,
        String latestRiskReasonSample,
        String ruleCode) {
}
