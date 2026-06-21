package com.example.aistudy.riskpoc.operationlog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("operation_log")
public class OperationLogEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String requestId;
    private String operator;
    private String operationType;
    private String targetType;
    private String targetBizNo;
    private String result;
    private String errorMessageSample;
    private String operationDescSample;
    private String clientIpMasked;
    private Long durationMs;
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }
    public String getOperationType() { return operationType; }
    public void setOperationType(String operationType) { this.operationType = operationType; }
    public String getTargetType() { return targetType; }
    public void setTargetType(String targetType) { this.targetType = targetType; }
    public String getTargetBizNo() { return targetBizNo; }
    public void setTargetBizNo(String targetBizNo) { this.targetBizNo = targetBizNo; }
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
    public String getErrorMessageSample() { return errorMessageSample; }
    public void setErrorMessageSample(String errorMessageSample) { this.errorMessageSample = errorMessageSample; }
    public String getOperationDescSample() { return operationDescSample; }
    public void setOperationDescSample(String operationDescSample) { this.operationDescSample = operationDescSample; }
    public String getClientIpMasked() { return clientIpMasked; }
    public void setClientIpMasked(String clientIpMasked) { this.clientIpMasked = clientIpMasked; }
    public Long getDurationMs() { return durationMs; }
    public void setDurationMs(Long durationMs) { this.durationMs = durationMs; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
