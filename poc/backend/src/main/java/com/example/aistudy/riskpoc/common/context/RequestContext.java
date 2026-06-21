package com.example.aistudy.riskpoc.common.context;

public record RequestContext(String requestId, String operator, long startedNanos, String clientIpMasked) {
    public long durationMs() {
        return Math.max(0, (System.nanoTime() - startedNanos) / 1_000_000);
    }
}
