package com.example.aistudy.riskpoc.operationlog.assembler;

import com.example.aistudy.riskpoc.common.util.TimeUtil;
import com.example.aistudy.riskpoc.operationlog.dto.OperationLogResponse;
import com.example.aistudy.riskpoc.operationlog.entity.OperationLogEntity;
import org.springframework.stereotype.Component;

@Component
public class OperationLogAssembler {

    public OperationLogResponse toResponse(OperationLogEntity entity) {
        return new OperationLogResponse(entity.getRequestId(), entity.getOperator(), entity.getOperationType(),
                entity.getTargetType(), entity.getTargetBizNo(), entity.getResult(), entity.getErrorMessageSample(),
                entity.getOperationDescSample(), entity.getClientIpMasked(), entity.getDurationMs(),
                TimeUtil.utcToShanghai(entity.getCreatedAt()));
    }
}
