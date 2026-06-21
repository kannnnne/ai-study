package com.example.aistudy.riskpoc.operationlog.controller;

import com.example.aistudy.riskpoc.common.api.ApiResponse;
import com.example.aistudy.riskpoc.common.api.PageResponse;
import com.example.aistudy.riskpoc.operationlog.dto.OperationLogResponse;
import com.example.aistudy.riskpoc.operationlog.service.OperationLogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/operation-logs")
public class OperationLogController {
    private final OperationLogService operationLogService;

    public OperationLogController(OperationLogService operationLogService) {
        this.operationLogService = operationLogService;
    }

    @GetMapping
    public ApiResponse<PageResponse<OperationLogResponse>> list(
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String requestId,
            @RequestParam(required = false) String operator,
            @RequestParam(required = false) String operationType,
            @RequestParam(required = false) String targetType,
            @RequestParam(required = false) String targetBizNo,
            @RequestParam(required = false) String result,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        return ApiResponse.success(operationLogService.list(pageNo, pageSize, requestId, operator, operationType,
                targetType, targetBizNo, result, startTime, endTime));
    }
}
