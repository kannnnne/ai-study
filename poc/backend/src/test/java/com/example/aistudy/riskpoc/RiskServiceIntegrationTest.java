package com.example.aistudy.riskpoc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.aistudy.riskpoc.common.context.RequestContext;
import com.example.aistudy.riskpoc.common.context.RequestContextHolder;
import com.example.aistudy.riskpoc.common.exception.NotFoundException;
import com.example.aistudy.riskpoc.customer.mapper.CustomerMapper;
import com.example.aistudy.riskpoc.operationlog.entity.OperationLogEntity;
import com.example.aistudy.riskpoc.operationlog.mapper.OperationLogMapper;
import com.example.aistudy.riskpoc.risk.dto.RiskRecalculateRequest;
import com.example.aistudy.riskpoc.risk.dto.RiskRecalculateResponse;
import com.example.aistudy.riskpoc.risk.entity.RiskRecordEntity;
import com.example.aistudy.riskpoc.risk.mapper.RiskRecordMapper;
import com.example.aistudy.riskpoc.risk.service.RiskService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class RiskServiceIntegrationTest {
    @Autowired
    private RiskService riskService;
    @Autowired
    private RiskRecordMapper riskRecordMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private OperationLogMapper operationLogMapper;

    @BeforeEach
    void setContext() {
        RequestContextHolder.set(new RequestContext("REQ-TEST-RISK", "demo-admin", System.nanoTime(), "SAMPLE-IP-TEST"));
    }

    @AfterEach
    void clearContext() {
        RequestContextHolder.clear();
    }

    @Test
    void recalculateInsertsRiskRecordUpdatesCustomerSnapshotAndWritesLog() {
        long before = riskRecordMapper.selectCount(new LambdaQueryWrapper<RiskRecordEntity>()
                .eq(RiskRecordEntity::getCustomerNo, "CUST100002"));

        RiskRecalculateResponse response = riskService.recalculate("CUST100002",
                new RiskRecalculateRequest("课程录屏手动触发风险重算样例", "DEMO_RULE_V1"));

        assertThat(response.riskLevel()).isEqualTo("MEDIUM");
        assertThat(response.riskScore()).isEqualByComparingTo("55.00");
        assertThat(response.transactionEffect().riskRecordInserted()).isTrue();
        assertThat(riskRecordMapper.selectCount(new LambdaQueryWrapper<RiskRecordEntity>()
                .eq(RiskRecordEntity::getCustomerNo, "CUST100002"))).isEqualTo(before + 1);
        assertThat(customerMapper.selectOne(new LambdaQueryWrapper<com.example.aistudy.riskpoc.customer.entity.CustomerEntity>()
                .eq(com.example.aistudy.riskpoc.customer.entity.CustomerEntity::getCustomerNo, "CUST100002"))
                .getCurrentRiskScore()).isEqualByComparingTo("55.00");
        assertThat(operationLogMapper.selectCount(new LambdaQueryWrapper<OperationLogEntity>()
                .eq(OperationLogEntity::getRequestId, "REQ-TEST-RISK")
                .eq(OperationLogEntity::getOperationType, "RECALCULATE_RISK")
                .eq(OperationLogEntity::getResult, "SUCCESS"))).isGreaterThanOrEqualTo(1);
    }

    @Test
    void recalculateMissingCustomerWritesSafeFailureLog() {
        RequestContextHolder.clear();
        RequestContextHolder.set(new RequestContext("REQ-TEST-RISK-404", "demo-admin", System.nanoTime(), "SAMPLE-IP-TEST"));

        assertThatThrownBy(() -> riskService.recalculate("CUST999999", new RiskRecalculateRequest(null, null)))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("客户不存在");

        OperationLogEntity log = operationLogMapper.selectOne(new LambdaQueryWrapper<OperationLogEntity>()
                .eq(OperationLogEntity::getRequestId, "REQ-TEST-RISK-404")
                .eq(OperationLogEntity::getResult, "FAIL"));
        assertThat(log.getErrorMessageSample()).isEqualTo("样例错误：客户不存在");
        assertThat(log.getErrorMessageSample()).doesNotContain("Exception", "{", "}");
    }
}
