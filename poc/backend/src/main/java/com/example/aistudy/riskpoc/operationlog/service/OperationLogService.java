package com.example.aistudy.riskpoc.operationlog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.aistudy.riskpoc.common.api.PageResponse;
import com.example.aistudy.riskpoc.common.context.RequestContextHolder;
import com.example.aistudy.riskpoc.common.exception.ValidationException;
import com.example.aistudy.riskpoc.common.util.TimeUtil;
import com.example.aistudy.riskpoc.common.util.Validators;
import com.example.aistudy.riskpoc.operationlog.assembler.OperationLogAssembler;
import com.example.aistudy.riskpoc.operationlog.dto.OperationLogResponse;
import com.example.aistudy.riskpoc.operationlog.entity.OperationLogEntity;
import com.example.aistudy.riskpoc.operationlog.mapper.OperationLogMapper;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class OperationLogService {
    private static final Set<String> OPERATION_TYPES = Set.of("QUERY_CUSTOMER", "UPDATE_CUSTOMER", "RECALCULATE_RISK", "QUERY_LOG");
    private static final Set<String> TARGET_TYPES = Set.of("CUSTOMER", "ACCOUNT", "RISK", "LOG");
    private static final Set<String> RESULTS = Set.of("SUCCESS", "FAIL");

    private final OperationLogMapper operationLogMapper;
    private final OperationLogAssembler assembler;

    public OperationLogService(OperationLogMapper operationLogMapper, OperationLogAssembler assembler) {
        this.operationLogMapper = operationLogMapper;
        this.assembler = assembler;
    }

    public PageResponse<OperationLogResponse> list(int pageNo, int pageSize, String requestId, String operator,
            String operationType, String targetType, String targetBizNo, String result, String startTime, String endTime) {
        Validators.page(pageNo, pageSize);
        Validators.allowed(operationType, "operationType", OPERATION_TYPES);
        Validators.allowed(targetType, "targetType", TARGET_TYPES);
        Validators.allowed(result, "result", RESULTS);
        OffsetDateTime start = parseTime(startTime, "startTime");
        OffsetDateTime end = parseTime(endTime, "endTime");
        if (start != null && end != null && start.isAfter(end)) {
            throw new ValidationException("startTime 不能晚于 endTime");
        }
        LambdaQueryWrapper<OperationLogEntity> wrapper = new LambdaQueryWrapper<OperationLogEntity>()
                .eq(StringUtils.hasText(requestId), OperationLogEntity::getRequestId, requestId)
                .eq(StringUtils.hasText(operator), OperationLogEntity::getOperator, operator)
                .eq(StringUtils.hasText(operationType), OperationLogEntity::getOperationType, operationType)
                .eq(StringUtils.hasText(targetType), OperationLogEntity::getTargetType, targetType)
                .eq(StringUtils.hasText(targetBizNo), OperationLogEntity::getTargetBizNo, targetBizNo)
                .eq(StringUtils.hasText(result), OperationLogEntity::getResult, result)
                .ge(start != null, OperationLogEntity::getCreatedAt, start == null ? null : start.withOffsetSameInstant(ZoneOffset.UTC).toLocalDateTime())
                .le(end != null, OperationLogEntity::getCreatedAt, end == null ? null : end.withOffsetSameInstant(ZoneOffset.UTC).toLocalDateTime())
                .orderByDesc(OperationLogEntity::getCreatedAt)
                .orderByDesc(OperationLogEntity::getId);
        Page<OperationLogEntity> page = operationLogMapper.selectPage(Page.of(pageNo, pageSize), wrapper);
        List<OperationLogResponse> records = page.getRecords().stream().map(assembler::toResponse).toList();
        recordSuccess("QUERY_LOG", "LOG", null, "查询操作日志样例");
        return PageResponse.from(page, records);
    }

    public void recordSuccess(String operationType, String targetType, String targetBizNo, String descSample) {
        insertLog(operationType, targetType, targetBizNo, "SUCCESS", null, descSample);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recordFailureRequiresNew(String operationType, String targetType, String targetBizNo,
            String errorMessageSample, String descSample) {
        insertLog(operationType, targetType, targetBizNo, "FAIL", errorMessageSample, descSample);
    }

    private void insertLog(String operationType, String targetType, String targetBizNo, String result,
            String errorMessageSample, String descSample) {
        OperationLogEntity log = new OperationLogEntity();
        log.setRequestId(RequestContextHolder.requestId());
        log.setOperator(RequestContextHolder.operator());
        log.setOperationType(operationType);
        log.setTargetType(targetType);
        log.setTargetBizNo(targetBizNo);
        log.setResult(result);
        log.setErrorMessageSample(errorMessageSample);
        log.setOperationDescSample(descSample);
        log.setClientIpMasked(RequestContextHolder.clientIpMasked());
        log.setDurationMs(RequestContextHolder.durationMs());
        log.setCreatedAt(TimeUtil.nowUtc());
        operationLogMapper.insert(log);
    }

    private OffsetDateTime parseTime(String value, String field) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        try {
            return OffsetDateTime.parse(value);
        } catch (Exception ex) {
            throw new ValidationException(field + " 必须为 ISO-8601 时间");
        }
    }
}
