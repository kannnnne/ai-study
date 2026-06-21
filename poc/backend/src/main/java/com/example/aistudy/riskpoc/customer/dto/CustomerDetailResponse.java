package com.example.aistudy.riskpoc.customer.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record CustomerDetailResponse(
        String customerNo,
        String customerNameSample,
        String customerType,
        String mobileMasked,
        String idNoMasked,
        String status,
        String currentRiskLevel,
        BigDecimal currentRiskScore,
        OffsetDateTime lastRiskCalculatedAt,
        String remarkSample,
        String createdBy,
        OffsetDateTime createdAt,
        String updatedBy,
        OffsetDateTime updatedAt,
        Integer version) {
}
