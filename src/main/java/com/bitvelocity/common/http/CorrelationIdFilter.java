package com.bitvelocity.common.http;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import java.io.IOException;
import java.util.UUID;

public class CorrelationIdFilter implements Filter {
    private static final String CORRELATION_HEADER = "X-Correlation-Id";
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            if (request instanceof HttpServletRequest http) {
                String existing = http.getHeader(CORRELATION_HEADER);
                String cid = (existing == null || existing.isBlank()) ? UUID.randomUUID().toString() : existing;
                MDC.put("correlationId", cid);
            }
            chain.doFilter(request, response);
        } finally {
            MDC.remove("correlationId");
        }
    }
}