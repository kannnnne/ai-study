package com.example.aistudy.riskpoc.risk.service;

import java.math.BigDecimal;

public record RiskCalculationResult(String riskLevel, BigDecimal riskScore, String riskReasonSample) {
}
