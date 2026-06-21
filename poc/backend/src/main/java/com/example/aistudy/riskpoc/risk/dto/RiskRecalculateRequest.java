package com.example.aistudy.riskpoc.risk.dto;

import jakarta.validation.constraints.Size;

public record RiskRecalculateRequest(
        @Size(max = 200) String reasonSample,
        @Size(max = 32) String ruleCode) {
}
