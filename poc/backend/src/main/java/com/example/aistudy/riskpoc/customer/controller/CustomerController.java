package com.example.aistudy.riskpoc.customer.controller;

import com.example.aistudy.riskpoc.common.api.ApiResponse;
import com.example.aistudy.riskpoc.common.api.PageResponse;
import com.example.aistudy.riskpoc.customer.dto.CustomerCreateRequest;
import com.example.aistudy.riskpoc.customer.dto.CustomerDetailResponse;
import com.example.aistudy.riskpoc.customer.dto.CustomerListItemResponse;
import com.example.aistudy.riskpoc.customer.dto.CustomerUpdateRequest;
import com.example.aistudy.riskpoc.customer.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ApiResponse<PageResponse<CustomerListItemResponse>> list(
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String customerNo,
            @RequestParam(required = false) String customerNameSample,
            @RequestParam(required = false) String customerType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String riskLevel) {
        return ApiResponse.success(customerService.list(pageNo, pageSize, customerNo, customerNameSample, customerType, status, riskLevel));
    }

    @GetMapping("/{customerNo}")
    public ApiResponse<CustomerDetailResponse> detail(@PathVariable String customerNo) {
        return ApiResponse.success(customerService.detail(customerNo));
    }

    @PostMapping
    public ApiResponse<CustomerDetailResponse> create(@Valid @RequestBody CustomerCreateRequest request) {
        return ApiResponse.success(customerService.create(request));
    }

    @PutMapping("/{customerNo}")
    public ApiResponse<CustomerDetailResponse> update(@PathVariable String customerNo,
            @Valid @RequestBody CustomerUpdateRequest request) {
        return ApiResponse.success(customerService.update(customerNo, request));
    }
}
