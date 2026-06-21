package com.example.aistudy.riskpoc.risk.dto;

public record TransactionEffectResponse(
        boolean riskRecordInserted,
        boolean customerRiskSnapshotUpdated,
        boolean operationLogInserted) {
}
