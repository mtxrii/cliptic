package com.mtxrii.cliptic.clipticbackend.api.controller.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RequestLoggingFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        log.info("Received request at {} {} body: {}", req.getMethod(), req.getRequestURI(), body);
        chain.doFilter(request, response);
        log.info("Request completed");
    }
}
