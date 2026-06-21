package com.example.aistudy.riskpoc.risk.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.aistudy.riskpoc.account.entity.AccountEntity;
import com.example.aistudy.riskpoc.account.service.AccountService;
import com.example.aistudy.riskpoc.common.api.PageResponse;
import com.example.aistudy.riskpoc.common.context.RequestContextHolder;
import com.example.aistudy.riskpoc.common.exception.ConflictException;
import com.example.aistudy.riskpoc.common.exception.NotFoundException;
import com.example.aistudy.riskpoc.common.util.TimeUtil;
import com.example.aistudy.riskpoc.common.util.Validators;
import com.example.aistudy.riskpoc.customer.entity.CustomerEntity;
import com.example.aistudy.riskpoc.customer.mapper.CustomerMapper;
import com.example.aistudy.riskpoc.customer.service.CustomerService;
import com.example.aistudy.riskpoc.operationlog.service.OperationLogService;
import com.example.aistudy.riskpoc.risk.assembler.RiskAssembler;
import com.example.aistudy.riskpoc.risk.dto.CurrentRiskResponse;
import com.example.aistudy.riskpoc.risk.dto.RiskRecalculateRequest;
import com.example.aistudy.riskpoc.risk.dto.RiskRecalculateResponse;
import com.example.aistudy.riskpoc.risk.dto.RiskRecordResponse;
import com.example.aistudy.riskpoc.risk.entity.RiskRecordEntity;
import com.example.aistudy.riskpoc.risk.mapper.RiskRecordMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class RiskService {
    private static final Set<String> RISK_LEVELS = Set.of("LOW", "MEDIUM", "HIGH");

    private final RiskRecordMapper riskRecordMapper;
    private final CustomerMapper customerMapper;
    private final CustomerService customerService;
    private final AccountService accountService;
    private final RiskCalculator riskCalculator;
    private final RiskAssembler assembler;
    private final OperationLogService operationLogService;

    public RiskService(RiskRecordMapper riskRecordMapper, CustomerMapper customerMapper, CustomerService customerService,
            AccountService accountService, RiskCalculator riskCalculator, RiskAssembler assembler,
            OperationLogService operationLogService) {
        this.riskRecordMapper = riskRecordMapper;
        this.customerMapper = customerMapper;
        this.customerService = customerService;
        this.accountService = accountService;
        this.riskCalculator = riskCalculator;
        this.assembler = assembler;
        this.operationLogService = operationLogService;
    }

    public CurrentRiskResponse currentRisk(String customerNo) {
        CustomerEntity customer = customerService.requireCustomer(customerNo);
        return assembler.toCurrentRisk(customer, latestRecord(customerNo));
    }

    public PageResponse<RiskRecordResponse> history(String customerNo, int pageNo, int pageSize, String riskLevel) {
        customerService.requireCustomer(customerNo);
        Validators.page(pageNo, pageSize);
        Validators.allowed(riskLevel, "riskLevel", RISK_LEVELS);
        LambdaQueryWrapper<RiskRecordEntity> wrapper = new LambdaQueryWrapper<RiskRecordEntity>()
                .eq(RiskRecordEntity::getCustomerNo, customerNo)
                .eq(StringUtils.hasText(riskLevel), RiskRecordEntity::getRiskLevel, riskLevel)
                .orderByDesc(RiskRecordEntity::getCalculatedAt)
                .orderByDesc(RiskRecordEntity::getId);
        Page<RiskRecordEntity> page = riskRecordMapper.selectPage(Page.of(pageNo, pageSize), wrapper);
        return PageResponse.from(page, page.getRecords().stream().map(assembler::toRecord).toList());
    }

    @Transactional
    public RiskRecalculateResponse recalculate(String customerNo, RiskRecalculateRequest request) {
        CustomerEntity customer = customerService.findByCustomerNo(customerNo);
        if (customer == null) {
            operationLogService.recordFailureRequiresNew("RECALCULATE_RISK", "RISK", customerNo,
                    "样例错误：客户不存在", "触发不存在客户风险重算样例");
            throw new NotFoundException("客户不存在");
        }
        if ("CLOSED".equals(customer.getStatus())) {
            operationLogService.recordFailureRequiresNew("RECALCULATE_RISK", "RISK", customerNo,
                    "样例错误：关闭状态客户不允许触发风险重算", "触发关闭状态客户风险重算样例");
            throw new ConflictException("关闭状态客户不允许触发风险重算");
        }

        List<AccountEntity> accounts = accountService.listEntitiesByCustomerNo(customerNo);
        RiskCalculationResult result = riskCalculator.calculate(customer, accounts);
        LocalDateTime now = TimeUtil.nowUtc();
        String ruleCode = request == null || !StringUtils.hasText(request.ruleCode()) ? "DEMO_RULE_V1" : request.ruleCode();

        RiskRecordEntity record = new RiskRecordEntity();
        record.setCustomerNo(customerNo);
        record.setRiskLevel(result.riskLevel());
        record.setRiskScore(result.riskScore());
        record.setRiskReasonSample(result.riskReasonSample());
        record.setRuleCode(ruleCode);
        record.setSourceType("MANUAL");
        record.setCalculatedBy(RequestContextHolder.operator());
        record.setCalculatedAt(now);
        record.setCreatedAt(now);
        riskRecordMapper.insert(record);

        customerMapper.update(null, new LambdaUpdateWrapper<CustomerEntity>()
                .eq(CustomerEntity::getCustomerNo, customerNo)
                .set(CustomerEntity::getCurrentRiskLevel, result.riskLevel())
                .set(CustomerEntity::getCurrentRiskScore, result.riskScore())
                .set(CustomerEntity::getLastRiskCalculatedAt, now)
                .set(CustomerEntity::getUpdatedBy, RequestContextHolder.operator())
                .set(CustomerEntity::getUpdatedAt, now));

        operationLogService.recordSuccess("RECALCULATE_RISK", "RISK", customerNo,
                "触发风险重算样例，结果为" + result.riskLevel());
        return assembler.toRecalculate(record);
    }

    public RiskRecordEntity latestRecord(String customerNo) {
        return riskRecordMapper.selectOne(new LambdaQueryWrapper<RiskRecordEntity>()
                .eq(RiskRecordEntity::getCustomerNo, customerNo)
                .orderByDesc(RiskRecordEntity::getCalculatedAt)
                .orderByDesc(RiskRecordEntity::getId)
                .last("limit 1"));
    }
}
