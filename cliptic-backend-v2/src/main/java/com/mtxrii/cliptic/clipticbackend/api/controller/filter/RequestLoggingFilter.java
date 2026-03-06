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

        String[] queryParamsArr = req.getQueryString() != null ? req.getQueryString().split("&") : new String[0];
        String query = '[' + String.join(", ", queryParamsArr) + ']';
        String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        log.info("Received request at {} {} query: {} body: {}", req.getMethod(), req.getRequestURI(), query, body);
        chain.doFilter(request, response);
        log.info("Request completed");
    }
}
