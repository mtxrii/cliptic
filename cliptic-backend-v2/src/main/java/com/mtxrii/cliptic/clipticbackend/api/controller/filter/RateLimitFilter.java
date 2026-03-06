package com.mtxrii.cliptic.clipticbackend.api.controller.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtxrii.cliptic.clipticbackend.ClipticConst;
import com.mtxrii.cliptic.clipticbackend.api.model.response.ErrorResponse;
import com.mtxrii.cliptic.clipticbackend.api.model.response.Response;
import com.mtxrii.cliptic.clipticbackend.util.GlobalRateLimiter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class RateLimitFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final GlobalRateLimiter limiter = new GlobalRateLimiter(
            ClipticConst.RATE_LIMIT_PER_MINUTE,
            ClipticConst.RATE_LIMIT_WINDOW_IN_SECONDS * 1000
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return !path.equals(ClipticConst.MAPPING_URL_CONTROLLER);
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain
    ) throws ServletException, IOException {
        if (!this.limiter.allowRequest()) {
            Response responseJson = new ErrorResponse(429, "Too many requests. Please give the server a moment");
            response.setStatus(responseJson.getStatusCode());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().write(this.objectMapper.writeValueAsString(responseJson));
            return;
        }

        chain.doFilter(request, response);
    }
}
