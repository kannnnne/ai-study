package com.example.aistudy.riskpoc.customer.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record CustomerListItemResponse(
        String customerNo,
        String customerNameSample,
        String customerType,
        String mobileMasked,
        String status,
        String currentRiskLevel,
        BigDecimal currentRiskScore,
        OffsetDateTime lastRiskCalculatedAt,
        OffsetDateTime updatedAt) {
}
