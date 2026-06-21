package com.example.aistudy.riskpoc.common.util;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public final class TimeUtil {
    public static final ZoneId SHANGHAI = ZoneId.of("Asia/Shanghai");

    private TimeUtil() {
    }

    public static OffsetDateTime nowShanghai() {
        return OffsetDateTime.now(SHANGHAI);
    }

    public static LocalDateTime nowUtc() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }

    public static OffsetDateTime utcToShanghai(LocalDateTime utc) {
        if (utc == null) {
            return null;
        }
        return utc.atOffset(ZoneOffset.UTC).atZoneSameInstant(SHANGHAI).toOffsetDateTime();
    }
}
