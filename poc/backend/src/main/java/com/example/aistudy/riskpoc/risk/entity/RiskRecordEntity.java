package com.example.aistudy.riskpoc.risk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("risk_record")
public class RiskRecordEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String customerNo;
    private String riskLevel;
    private BigDecimal riskScore;
    private String riskReasonSample;
    private String ruleCode;
    private String sourceType;
    private String calculatedBy;
    private LocalDateTime calculatedAt;
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCustomerNo() { return customerNo; }
    public void setCustomerNo(String customerNo) { this.customerNo = customerNo; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public BigDecimal getRiskScore() { return riskScore; }
    public void setRiskScore(BigDecimal riskScore) { this.riskScore = riskScore; }
    public String getRiskReasonSample() { return riskReasonSample; }
    public void setRiskReasonSample(String riskReasonSample) { this.riskReasonSample = riskReasonSample; }
    public String getRuleCode() { return ruleCode; }
    public void setRuleCode(String ruleCode) { this.ruleCode = ruleCode; }
    public String getSourceType() { return sourceType; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }
    public String getCalculatedBy() { return calculatedBy; }
    public void setCalculatedBy(String calculatedBy) { this.calculatedBy = calculatedBy; }
    public LocalDateTime getCalculatedAt() { return calculatedAt; }
    public void setCalculatedAt(LocalDateTime calculatedAt) { this.calculatedAt = calculatedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
