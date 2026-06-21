package com.example.aistudy.riskpoc;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.aistudy.riskpoc.account.entity.AccountEntity;
import com.example.aistudy.riskpoc.customer.entity.CustomerEntity;
import com.example.aistudy.riskpoc.risk.service.RiskCalculationResult;
import com.example.aistudy.riskpoc.risk.service.RiskCalculator;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class RiskCalculatorTest {
    private final RiskCalculator calculator = new RiskCalculator();

    @Test
    void calculatesMediumForAbnormalLowBalanceCustomer() {
        CustomerEntity customer = customer("ACTIVE");
        AccountEntity normal = account("NORMAL", 0, "860.30");
        AccountEntity abnormalLoan = account("NORMAL", 1, "0.00");

        RiskCalculationResult result = calculator.calculate(customer, List.of(normal, abnormalLoan));

        assertThat(result.riskLevel()).isEqualTo("MEDIUM");
        assertThat(result.riskScore()).isEqualByComparingTo("55.00");
        assertThat(result.riskReasonSample()).contains("存在异常标记账户");
    }

    @Test
    void calculatesHighForFrozenCustomerWithFrozenAccount() {
        CustomerEntity customer = customer("FROZEN");
        AccountEntity frozen = account("FROZEN", 1, "0.00");

        RiskCalculationResult result = calculator.calculate(customer, List.of(frozen));

        assertThat(result.riskLevel()).isEqualTo("HIGH");
        assertThat(result.riskScore()).isEqualByComparingTo("100.00");
    }

    private CustomerEntity customer(String status) {
        CustomerEntity entity = new CustomerEntity();
        entity.setStatus(status);
        return entity;
    }

    private AccountEntity account(String status, int abnormal, String availableBalance) {
        AccountEntity entity = new AccountEntity();
        entity.setAccountStatus(status);
        entity.setHasAbnormalFlag(abnormal);
        entity.setAvailableBalance(new BigDecimal(availableBalance));
        return entity;
    }
}
