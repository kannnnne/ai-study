package com.example.aistudy.riskpoc.account.assembler;

import com.example.aistudy.riskpoc.account.dto.AccountResponse;
import com.example.aistudy.riskpoc.account.entity.AccountEntity;
import com.example.aistudy.riskpoc.common.util.TimeUtil;
import org.springframework.stereotype.Component;

@Component
public class AccountAssembler {

    public AccountResponse toResponse(AccountEntity entity) {
        return new AccountResponse(entity.getAccountNoMasked(), entity.getCustomerNo(), entity.getAccountType(),
                entity.getCurrency(), entity.getBalance(), entity.getAvailableBalance(), entity.getAccountStatus(),
                Integer.valueOf(1).equals(entity.getHasAbnormalFlag()), entity.getOpenedAt(),
                TimeUtil.utcToShanghai(entity.getUpdatedAt()));
    }
}
