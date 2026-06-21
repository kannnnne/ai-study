package com.example.aistudy.riskpoc.common.util;

import com.example.aistudy.riskpoc.common.exception.ValidationException;
import java.util.Set;
import org.springframework.util.StringUtils;

public final class Validators {
    private Validators() {
    }

    public static void page(int pageNo, int pageSize) {
        if (pageNo < 1) {
            throw new ValidationException("pageNo 必须大于等于 1");
        }
        if (pageSize < 1) {
            throw new ValidationException("pageSize 必须大于等于 1");
        }
        if (pageSize > 100) {
            throw new ValidationException("pageSize 最大值为 100");
        }
    }

    public static void allowed(String value, String field, Set<String> allowedValues) {
        if (StringUtils.hasText(value) && !allowedValues.contains(value)) {
            throw new ValidationException(field + " 只允许 " + String.join("、", allowedValues));
        }
    }
}
