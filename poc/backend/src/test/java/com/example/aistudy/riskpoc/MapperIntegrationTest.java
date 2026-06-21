package com.example.aistudy.riskpoc;

import static org.assertj.core.api.Assertions.assertThat;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.aistudy.riskpoc.account.entity.AccountEntity;
import com.example.aistudy.riskpoc.account.mapper.AccountMapper;
import com.example.aistudy.riskpoc.risk.entity.RiskRecordEntity;
import com.example.aistudy.riskpoc.risk.mapper.RiskRecordMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class MapperIntegrationTest {
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private RiskRecordMapper riskRecordMapper;

    @Test
    void accountMapperFindsCustomerAccounts() {
        List<AccountEntity> accounts = accountMapper.selectList(new LambdaQueryWrapper<AccountEntity>()
                .eq(AccountEntity::getCustomerNo, "CUST100001")
                .orderByAsc(AccountEntity::getAccountNoMasked));

        assertThat(accounts).hasSize(2);
        assertThat(accounts).allMatch(account -> account.getAccountNoMasked().startsWith("SAMPLE-ACC-****-"));
    }

    @Test
    void riskRecordsAreOrderedByCalculatedTimeDesc() {
        List<RiskRecordEntity> records = riskRecordMapper.selectList(new LambdaQueryWrapper<RiskRecordEntity>()
                .eq(RiskRecordEntity::getCustomerNo, "CUST100002")
                .orderByDesc(RiskRecordEntity::getCalculatedAt)
                .orderByDesc(RiskRecordEntity::getId));

        assertThat(records).hasSizeGreaterThanOrEqualTo(2);
        assertThat(records.get(0).getCalculatedAt()).isAfter(records.get(1).getCalculatedAt());
    }
}
