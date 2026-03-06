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

        String method = req.getMethod();
        String uri = req.getRequestURI();
        String[] queryParamsArr = req.getQueryString() != null ? req.getQueryString().split("&") : new String[0];
        String query = '[' + String.join(", ", queryParamsArr) + ']';

        // @TODO: Log request body
        log.info("Received request at {} {} (queryParams: {})", method, uri, query);

        chain.doFilter(request, response);

        // @TODO: Log response body
        log.info("Request completed at {} {} (queryParams: {})", method, uri, query);
    }
}
