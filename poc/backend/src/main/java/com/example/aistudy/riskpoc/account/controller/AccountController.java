package com.example.aistudy.riskpoc.account.controller;

import com.example.aistudy.riskpoc.account.dto.AccountResponse;
import com.example.aistudy.riskpoc.account.service.AccountService;
import com.example.aistudy.riskpoc.common.api.ApiResponse;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers/{customerNo}/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ApiResponse<List<AccountResponse>> list(@PathVariable String customerNo) {
        return ApiResponse.success(accountService.listByCustomerNo(customerNo));
    }
}
