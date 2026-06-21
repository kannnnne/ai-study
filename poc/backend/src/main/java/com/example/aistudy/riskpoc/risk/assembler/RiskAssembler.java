package com.example.aistudy.riskpoc.risk.assembler;

import com.example.aistudy.riskpoc.common.util.TimeUtil;
import com.example.aistudy.riskpoc.customer.entity.CustomerEntity;
import com.example.aistudy.riskpoc.risk.dto.CurrentRiskResponse;
import com.example.aistudy.riskpoc.risk.dto.RiskRecordResponse;
import com.example.aistudy.riskpoc.risk.dto.RiskRecalculateResponse;
import com.example.aistudy.riskpoc.risk.dto.TransactionEffectResponse;
import com.example.aistudy.riskpoc.risk.entity.RiskRecordEntity;
import org.springframework.stereotype.Component;

@Component
public class RiskAssembler {

    public CurrentRiskResponse toCurrentRisk(CustomerEntity customer, RiskRecordEntity latest) {
        return new CurrentRiskResponse(customer.getCustomerNo(), customer.getCustomerNameSample(),
                customer.getCurrentRiskLevel(), customer.getCurrentRiskScore(),
                TimeUtil.utcToShanghai(customer.getLastRiskCalculatedAt()),
                latest == null ? null : latest.getRiskReasonSample(),
                latest == null ? null : latest.getRuleCode());
    }

    public RiskRecordResponse toRecord(RiskRecordEntity entity) {
        return new RiskRecordResponse(entity.getCustomerNo(), entity.getRiskLevel(), entity.getRiskScore(),
                entity.getRiskReasonSample(), entity.getRuleCode(), entity.getSourceType(), entity.getCalculatedBy(),
                TimeUtil.utcToShanghai(entity.getCalculatedAt()), TimeUtil.utcToShanghai(entity.getCreatedAt()));
    }

    public RiskRecalculateResponse toRecalculate(RiskRecordEntity entity) {
        return new RiskRecalculateResponse(entity.getCustomerNo(), entity.getRiskLevel(), entity.getRiskScore(),
                entity.getRiskReasonSample(), entity.getRuleCode(), entity.getSourceType(), entity.getCalculatedBy(),
                TimeUtil.utcToShanghai(entity.getCalculatedAt()),
                new TransactionEffectResponse(true, true, true));
    }
}
