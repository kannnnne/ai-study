package com.example.aistudy.riskpoc.customer.assembler;

import com.example.aistudy.riskpoc.common.util.TimeUtil;
import com.example.aistudy.riskpoc.customer.dto.CustomerDetailResponse;
import com.example.aistudy.riskpoc.customer.dto.CustomerListItemResponse;
import com.example.aistudy.riskpoc.customer.entity.CustomerEntity;
import org.springframework.stereotype.Component;

@Component
public class CustomerAssembler {

    public CustomerListItemResponse toListItem(CustomerEntity entity) {
        return new CustomerListItemResponse(entity.getCustomerNo(), entity.getCustomerNameSample(),
                entity.getCustomerType(), entity.getMobileMasked(), entity.getStatus(),
                entity.getCurrentRiskLevel(), entity.getCurrentRiskScore(),
                TimeUtil.utcToShanghai(entity.getLastRiskCalculatedAt()),
                TimeUtil.utcToShanghai(entity.getUpdatedAt()));
    }

    public CustomerDetailResponse toDetail(CustomerEntity entity) {
        return new CustomerDetailResponse(entity.getCustomerNo(), entity.getCustomerNameSample(),
                entity.getCustomerType(), entity.getMobileMasked(), entity.getIdNoMasked(), entity.getStatus(),
                entity.getCurrentRiskLevel(), entity.getCurrentRiskScore(),
                TimeUtil.utcToShanghai(entity.getLastRiskCalculatedAt()), entity.getRemarkSample(),
                entity.getCreatedBy(), TimeUtil.utcToShanghai(entity.getCreatedAt()),
                entity.getUpdatedBy(), TimeUtil.utcToShanghai(entity.getUpdatedAt()), entity.getVersion());
    }
}
