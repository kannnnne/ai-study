package com.example.aistudy.riskpoc.common.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;

public record PageResponse<T>(
        List<T> records,
        long pageNo,
        long pageSize,
        long total,
        long pages,
        boolean hasNext) {

    public static <T> PageResponse<T> from(IPage<?> page, List<T> records) {
        return new PageResponse<>(records, page.getCurrent(), page.getSize(), page.getTotal(),
                page.getPages(), page.getCurrent() < page.getPages());
    }
}
