package com.example.aistudy.riskpoc.customer.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.aistudy.riskpoc.common.api.PageResponse;
import com.example.aistudy.riskpoc.common.context.RequestContextHolder;
import com.example.aistudy.riskpoc.common.exception.ConflictException;
import com.example.aistudy.riskpoc.common.exception.NotFoundException;
import com.example.aistudy.riskpoc.common.util.TimeUtil;
import com.example.aistudy.riskpoc.common.util.Validators;
import com.example.aistudy.riskpoc.customer.assembler.CustomerAssembler;
import com.example.aistudy.riskpoc.customer.dto.CustomerCreateRequest;
import com.example.aistudy.riskpoc.customer.dto.CustomerDetailResponse;
import com.example.aistudy.riskpoc.customer.dto.CustomerListItemResponse;
import com.example.aistudy.riskpoc.customer.dto.CustomerUpdateRequest;
import com.example.aistudy.riskpoc.customer.entity.CustomerEntity;
import com.example.aistudy.riskpoc.customer.mapper.CustomerMapper;
import com.example.aistudy.riskpoc.operationlog.service.OperationLogService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class CustomerService {
    private static final Set<String> TYPES = Set.of("PERSONAL", "COMPANY");
    private static final Set<String> STATUSES = Set.of("ACTIVE", "FROZEN", "CLOSED");
    private static final Set<String> RISK_LEVELS = Set.of("LOW", "MEDIUM", "HIGH");

    private final CustomerMapper customerMapper;
    private final CustomerAssembler assembler;
    private final OperationLogService operationLogService;

    public CustomerService(CustomerMapper customerMapper, CustomerAssembler assembler, OperationLogService operationLogService) {
        this.customerMapper = customerMapper;
        this.assembler = assembler;
        this.operationLogService = operationLogService;
    }

    public PageResponse<CustomerListItemResponse> list(int pageNo, int pageSize, String customerNo,
            String customerNameSample, String customerType, String status, String riskLevel) {
        Validators.page(pageNo, pageSize);
        Validators.allowed(customerType, "customerType", TYPES);
        Validators.allowed(status, "status", STATUSES);
        Validators.allowed(riskLevel, "riskLevel", RISK_LEVELS);
        LambdaQueryWrapper<CustomerEntity> wrapper = new LambdaQueryWrapper<CustomerEntity>()
                .eq(StringUtils.hasText(customerNo), CustomerEntity::getCustomerNo, customerNo)
                .like(StringUtils.hasText(customerNameSample), CustomerEntity::getCustomerNameSample, customerNameSample)
                .eq(StringUtils.hasText(customerType), CustomerEntity::getCustomerType, customerType)
                .eq(StringUtils.hasText(status), CustomerEntity::getStatus, status)
                .eq(StringUtils.hasText(riskLevel), CustomerEntity::getCurrentRiskLevel, riskLevel)
                .orderByDesc(CustomerEntity::getUpdatedAt);
        Page<CustomerEntity> page = customerMapper.selectPage(Page.of(pageNo, pageSize), wrapper);
        List<CustomerListItemResponse> records = page.getRecords().stream().map(assembler::toListItem).toList();
        return PageResponse.from(page, records);
    }

    public CustomerDetailResponse detail(String customerNo) {
        CustomerEntity entity = requireCustomer(customerNo);
        operationLogService.recordSuccess("QUERY_CUSTOMER", "CUSTOMER", customerNo,
                "查询客户详情样例");
        return assembler.toDetail(entity);
    }

    @Transactional
    public CustomerDetailResponse create(CustomerCreateRequest request) {
        validateRequestEnums(request.customerType(), request.status());
        if (findByCustomerNo(request.customerNo()) != null) {
            throw new ConflictException("客户号已存在");
        }
        LocalDateTime now = TimeUtil.nowUtc();
        CustomerEntity entity = new CustomerEntity();
        entity.setCustomerNo(request.customerNo());
        entity.setCustomerNameSample(request.customerNameSample());
        entity.setCustomerType(request.customerType());
        entity.setMobileMasked(request.mobileMasked());
        entity.setIdNoMasked(request.idNoMasked());
        entity.setStatus(request.status());
        entity.setCurrentRiskLevel("LOW");
        entity.setCurrentRiskScore(BigDecimal.ZERO.setScale(2));
        entity.setRemarkSample(request.remarkSample());
        entity.setCreatedBy(RequestContextHolder.operator());
        entity.setCreatedAt(now);
        entity.setUpdatedBy(RequestContextHolder.operator());
        entity.setUpdatedAt(now);
        entity.setVersion(0);
        entity.setDeleted(0);
        customerMapper.insert(entity);
        return assembler.toDetail(entity);
    }

    @Transactional
    public CustomerDetailResponse update(String customerNo, CustomerUpdateRequest request) {
        validateRequestEnums(request.customerType(), request.status());
        CustomerEntity existing = requireCustomer(customerNo);
        if ("CLOSED".equals(existing.getStatus())) {
            throw new ConflictException("关闭状态客户不允许维护");
        }
        LocalDateTime now = TimeUtil.nowUtc();
        int updated = customerMapper.update(null, new LambdaUpdateWrapper<CustomerEntity>()
                .eq(CustomerEntity::getCustomerNo, customerNo)
                .eq(CustomerEntity::getVersion, request.version())
                .set(CustomerEntity::getCustomerNameSample, request.customerNameSample())
                .set(CustomerEntity::getCustomerType, request.customerType())
                .set(CustomerEntity::getMobileMasked, request.mobileMasked())
                .set(CustomerEntity::getIdNoMasked, request.idNoMasked())
                .set(CustomerEntity::getStatus, request.status())
                .set(CustomerEntity::getRemarkSample, request.remarkSample())
                .set(CustomerEntity::getUpdatedBy, RequestContextHolder.operator())
                .set(CustomerEntity::getUpdatedAt, now)
                .set(CustomerEntity::getVersion, request.version() + 1));
        if (updated == 0) {
            throw new ConflictException("客户信息已被更新，请刷新后重试");
        }
        operationLogService.recordSuccess("UPDATE_CUSTOMER", "CUSTOMER", customerNo, "更新客户资料样例");
        return assembler.toDetail(requireCustomer(customerNo));
    }

    public CustomerEntity requireCustomer(String customerNo) {
        CustomerEntity entity = findByCustomerNo(customerNo);
        if (entity == null) {
            throw new NotFoundException("客户不存在");
        }
        return entity;
    }

    public CustomerEntity findByCustomerNo(String customerNo) {
        return customerMapper.selectOne(new LambdaQueryWrapper<CustomerEntity>()
                .eq(CustomerEntity::getCustomerNo, customerNo));
    }

    private void validateRequestEnums(String customerType, String status) {
        Validators.allowed(customerType, "customerType", TYPES);
        Validators.allowed(status, "status", STATUSES);
    }
}
