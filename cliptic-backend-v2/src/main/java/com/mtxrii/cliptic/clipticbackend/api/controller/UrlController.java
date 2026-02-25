package com.mtxrii.cliptic.clipticbackend.api.controller;

import com.mtxrii.cliptic.clipticbackend.ClipticConst;
import com.mtxrii.cliptic.clipticbackend.api.model.request.PostUrlRequest;
import com.mtxrii.cliptic.clipticbackend.api.model.response.ErrorResponse;
import com.mtxrii.cliptic.clipticbackend.api.model.response.Response;
import com.mtxrii.cliptic.clipticbackend.service.UrlService;
import com.mtxrii.cliptic.clipticbackend.util.GenericUtil;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.concurrent.Callable;

@Slf4j
@RestController
public class UrlController {
    private final UrlService urlService;
    private final Dotenv dotenv;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
        this.dotenv = Dotenv.load();
    }

    @PostMapping(ClipticConst.MAPPING_URL_CONTROLLER)
    public ResponseEntity<Response> postUrl(
            HttpServletRequest httpRequest,
            @RequestHeader(name = ClipticConst.AUTHORIZATION_HEADER, defaultValue = ClipticConst.NONE_AUTH_HEADER) String authHeader,
            @RequestBody PostUrlRequest requestBody
    ) {
        Collections.list(httpRequest.getHeaderNames())
                   .forEach(name -> log.info("Header: {} = {}", name, httpRequest.getHeader(name)));
        log.info("Resolved authHeader param: '{}'", authHeader);

        Response response = this.runIfAuthenticated(
                HttpMethod.POST,
                authHeader,
                () -> this.urlService.postUrl(requestBody)
        );
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping(ClipticConst.MAPPING_URL_CONTROLLER)
    public ResponseEntity<Response> getUrl(
            @RequestHeader(name = ClipticConst.AUTHORIZATION_HEADER, defaultValue = ClipticConst.NONE_AUTH_HEADER) String authHeader,
            @RequestParam(name = ClipticConst.ALIAS_REQUEST_PARAM, required = false) String alias
    ) {
        Response response = this.runIfAuthenticated(
                HttpMethod.GET,
                authHeader,
                () -> this.urlService.getUrl(alias)
        );
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping(ClipticConst.MAPPING_URL_CONTROLLER)
    public ResponseEntity<Response> deleteUrl(
            @RequestHeader(name = ClipticConst.AUTHORIZATION_HEADER, defaultValue = ClipticConst.NONE_AUTH_HEADER) String authHeader,
            @RequestParam(name = ClipticConst.ALIAS_REQUEST_PARAM, required = true) String alias,
            @RequestParam(name = ClipticConst.OWNER_REQUEST_PARAM, defaultValue = ClipticConst.NONE_AUTH_HEADER) String owner
    ) {
        Response response = this.runIfAuthenticated(
                HttpMethod.DELETE,
                authHeader,
                () -> this.urlService.deleteUrl(alias, owner)
        );
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    private Response runIfAuthenticated(HttpMethod method, String authHeader, Callable<Response> callable) {
        if (authHeader == null || !authHeader.startsWith(ClipticConst.BEARER_AUTH_HEADER_PREFIX)) {
            log.info("Error on {} {}: Missing Bearer token", method.name(), ClipticConst.MAPPING_URL_CONTROLLER);
            return new ErrorResponse(401, "Unauthenticated. Missing or invalid token");
        }

        String token = authHeader.substring(ClipticConst.BEARER_AUTH_HEADER_PREFIX.length());
        String expected = this.dotenv.get(ClipticConst.BEARER_TOKEN_ENV_VAR_KEY);
        if (!GenericUtil.equals(token, expected)) {
            log.info("Error on {} {}: Invalid Bearer token", method.name(), ClipticConst.MAPPING_URL_CONTROLLER);
            return new ErrorResponse(401, "Access denied. Invalid token");
        }

        try {
            log.info("Successfully authenticated {} {}", method.name(), ClipticConst.MAPPING_URL_CONTROLLER);
            return callable.call();
        } catch (Exception e) {
            log.error("Error occurred while processing request", e);
            return new ErrorResponse(500, "Internal server error");
        }
    }
}
