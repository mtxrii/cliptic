package com.mtxrii.cliptic.clipticbackend.api.controller.filter;

import com.mtxrii.cliptic.clipticbackend.util.GlobalRateLimiter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RateLimitFilter extends OncePerRequestFilter {
    private final GlobalRateLimiter limiter = new GlobalRateLimiter(100, 60000);

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain
    ) throws ServletException, IOException {
        if (!this.limiter.allowRequest()) {
            response.setStatus(429);
            response.getWriter().write("Too many requests");
            return;
        }

        chain.doFilter(request, response);
    }
}
