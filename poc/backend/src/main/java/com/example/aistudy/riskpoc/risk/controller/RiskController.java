package com.example.aistudy.riskpoc.risk.controller;

import com.example.aistudy.riskpoc.common.api.ApiResponse;
import com.example.aistudy.riskpoc.common.api.PageResponse;
import com.example.aistudy.riskpoc.risk.dto.CurrentRiskResponse;
import com.example.aistudy.riskpoc.risk.dto.RiskRecalculateRequest;
import com.example.aistudy.riskpoc.risk.dto.RiskRecalculateResponse;
import com.example.aistudy.riskpoc.risk.dto.RiskRecordResponse;
import com.example.aistudy.riskpoc.risk.service.RiskService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers/{customerNo}")
public class RiskController {
    private final RiskService riskService;

    public RiskController(RiskService riskService) {
        this.riskService = riskService;
    }

    @GetMapping("/risk")
    public ApiResponse<CurrentRiskResponse> current(@PathVariable String customerNo) {
        return ApiResponse.success(riskService.currentRisk(customerNo));
    }

    @GetMapping("/risk-records")
    public ApiResponse<PageResponse<RiskRecordResponse>> history(
            @PathVariable String customerNo,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String riskLevel) {
        return ApiResponse.success(riskService.history(customerNo, pageNo, pageSize, riskLevel));
    }

    @PostMapping("/risk/recalculate")
    public ApiResponse<RiskRecalculateResponse> recalculate(@PathVariable String customerNo,
            @Valid @RequestBody(required = false) RiskRecalculateRequest request) {
        return ApiResponse.success(riskService.recalculate(customerNo, request));
    }
}
