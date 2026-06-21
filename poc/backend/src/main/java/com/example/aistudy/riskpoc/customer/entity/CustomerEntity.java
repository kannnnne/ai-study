package com.example.aistudy.riskpoc.customer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("customer")
public class CustomerEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String customerNo;
    private String customerNameSample;
    private String customerType;
    private String mobileMasked;
    private String idNoMasked;
    private String status;
    private String currentRiskLevel;
    private BigDecimal currentRiskScore;
    private LocalDateTime lastRiskCalculatedAt;
    private String remarkSample;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;
    private Integer version;
    @TableLogic
    private Integer deleted;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCustomerNo() { return customerNo; }
    public void setCustomerNo(String customerNo) { this.customerNo = customerNo; }
    public String getCustomerNameSample() { return customerNameSample; }
    public void setCustomerNameSample(String customerNameSample) { this.customerNameSample = customerNameSample; }
    public String getCustomerType() { return customerType; }
    public void setCustomerType(String customerType) { this.customerType = customerType; }
    public String getMobileMasked() { return mobileMasked; }
    public void setMobileMasked(String mobileMasked) { this.mobileMasked = mobileMasked; }
    public String getIdNoMasked() { return idNoMasked; }
    public void setIdNoMasked(String idNoMasked) { this.idNoMasked = idNoMasked; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCurrentRiskLevel() { return currentRiskLevel; }
    public void setCurrentRiskLevel(String currentRiskLevel) { this.currentRiskLevel = currentRiskLevel; }
    public BigDecimal getCurrentRiskScore() { return currentRiskScore; }
    public void setCurrentRiskScore(BigDecimal currentRiskScore) { this.currentRiskScore = currentRiskScore; }
    public LocalDateTime getLastRiskCalculatedAt() { return lastRiskCalculatedAt; }
    public void setLastRiskCalculatedAt(LocalDateTime lastRiskCalculatedAt) { this.lastRiskCalculatedAt = lastRiskCalculatedAt; }
    public String getRemarkSample() { return remarkSample; }
    public void setRemarkSample(String remarkSample) { this.remarkSample = remarkSample; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public Integer getDeleted() { return deleted; }
    public void setDeleted(Integer deleted) { this.deleted = deleted; }
}
