package com.example.aistudy.riskpoc.risk.service;

import com.example.aistudy.riskpoc.account.entity.AccountEntity;
import com.example.aistudy.riskpoc.customer.entity.CustomerEntity;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class RiskCalculator {

    public RiskCalculationResult calculate(CustomerEntity customer, List<AccountEntity> accounts) {
        int score = 20;
        List<String> reasons = new ArrayList<>();
        reasons.add("教学基础分");

        if ("FROZEN".equals(customer.getStatus())) {
            score += 35;
            reasons.add("客户状态冻结");
        } else if ("CLOSED".equals(customer.getStatus())) {
            score += 30;
            reasons.add("客户已关闭");
        } else {
            reasons.add("客户状态正常");
        }

        boolean hasAbnormal = accounts.stream().anyMatch(a -> Integer.valueOf(1).equals(a.getHasAbnormalFlag()));
        if (hasAbnormal) {
            score += 25;
            reasons.add("存在异常标记账户");
        }

        boolean hasFrozenAccount = accounts.stream().anyMatch(a -> "FROZEN".equals(a.getAccountStatus()));
        boolean hasClosedAccount = accounts.stream().anyMatch(a -> "CLOSED".equals(a.getAccountStatus()));
        if (hasFrozenAccount) {
            score += 20;
            reasons.add("存在冻结账户");
        } else if (hasClosedAccount) {
            score += 10;
            reasons.add("存在关闭账户");
        }

        BigDecimal availableTotal = accounts.stream()
                .map(AccountEntity::getAvailableBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (availableTotal.compareTo(new BigDecimal("1000.00")) < 0) {
            score += 10;
            reasons.add("可用余额低于样例阈值");
        }

        score = Math.min(score, 100);
        String level = score < 40 ? "LOW" : score < 70 ? "MEDIUM" : "HIGH";
        return new RiskCalculationResult(level, BigDecimal.valueOf(score).setScale(2, RoundingMode.HALF_UP),
                "样例规则：" + String.join("，", reasons));
    }
}
