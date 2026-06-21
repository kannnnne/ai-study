package com.example.aistudy.riskpoc.common.context;

public final class RequestContextHolder {
    private static final ThreadLocal<RequestContext> HOLDER = new ThreadLocal<>();

    private RequestContextHolder() {
    }

    public static void set(RequestContext context) {
        HOLDER.set(context);
    }

    public static RequestContext get() {
        return HOLDER.get();
    }

    public static String requestId() {
        RequestContext context = HOLDER.get();
        return context == null ? "REQ-DEMO-AUTO-UNKNOWN" : context.requestId();
    }

    public static String operator() {
        RequestContext context = HOLDER.get();
        return context == null ? "demo-operator" : context.operator();
    }

    public static String clientIpMasked() {
        RequestContext context = HOLDER.get();
        return context == null ? "SAMPLE-IP-UNKNOWN" : context.clientIpMasked();
    }

    public static long durationMs() {
        RequestContext context = HOLDER.get();
        return context == null ? 0 : context.durationMs();
    }

    public static void clear() {
        HOLDER.remove();
    }
}
