package com.lods.trigger.Intercptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

public class HostValidationInterceptor implements HandlerInterceptor {

    private final List<String> allowedHosts;

    public HostValidationInterceptor(List<String> allowedHosts) {
        this.allowedHosts = allowedHosts;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String host = request.getHeader("Host");
        if (host == null || allowedHosts.stream().noneMatch(host::equals)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Forbidden: host not allowed");
            return false;
        }
        return true;
    }
}
