package com.example.aistudy.riskpoc.account.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public record AccountResponse(
        String accountNoMasked,
        String customerNo,
        String accountType,
        String currency,
        BigDecimal balance,
        BigDecimal availableBalance,
        String accountStatus,
        boolean hasAbnormalFlag,
        LocalDate openedAt,
        OffsetDateTime updatedAt) {
}
