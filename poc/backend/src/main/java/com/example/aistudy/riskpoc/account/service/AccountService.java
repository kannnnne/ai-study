package com.example.aistudy.riskpoc.account.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.aistudy.riskpoc.account.assembler.AccountAssembler;
import com.example.aistudy.riskpoc.account.dto.AccountResponse;
import com.example.aistudy.riskpoc.account.entity.AccountEntity;
import com.example.aistudy.riskpoc.account.mapper.AccountMapper;
import com.example.aistudy.riskpoc.customer.service.CustomerService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final AccountMapper accountMapper;
    private final AccountAssembler assembler;
    private final CustomerService customerService;

    public AccountService(AccountMapper accountMapper, AccountAssembler assembler, CustomerService customerService) {
        this.accountMapper = accountMapper;
        this.assembler = assembler;
        this.customerService = customerService;
    }

    public List<AccountResponse> listByCustomerNo(String customerNo) {
        customerService.requireCustomer(customerNo);
        return listEntitiesByCustomerNo(customerNo).stream().map(assembler::toResponse).toList();
    }

    public List<AccountEntity> listEntitiesByCustomerNo(String customerNo) {
        return accountMapper.selectList(new LambdaQueryWrapper<AccountEntity>()
                .eq(AccountEntity::getCustomerNo, customerNo)
                .orderByAsc(AccountEntity::getAccountNoMasked));
    }
}
