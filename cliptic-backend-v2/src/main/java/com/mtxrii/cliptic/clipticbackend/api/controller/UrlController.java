package com.mtxrii.cliptic.clipticbackend.api.controller;

import com.mtxrii.cliptic.clipticbackend.ClipticConst;
import com.mtxrii.cliptic.clipticbackend.api.model.request.PostUrlRequest;
import com.mtxrii.cliptic.clipticbackend.api.model.response.ErrorResponse;
import com.mtxrii.cliptic.clipticbackend.api.model.response.Response;
import com.mtxrii.cliptic.clipticbackend.service.UrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;

@RestController
public class UrlController {
    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping(ClipticConst.MAPPING_URL_CONTROLLER)
    public ResponseEntity<Response> postUrl(
            @RequestHeader(name = ClipticConst.AUTHORIZATION_HEADER, defaultValue = ClipticConst.NONE_AUTH_HEADER) String authHeader,
            @RequestBody PostUrlRequest requestBody
    ) {
        Response response = this.runIfAuthenticated(
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
                authHeader,
                () -> this.urlService.deleteUrl(alias, owner)
        );
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    private Response runIfAuthenticated(String authHeader, Callable<Response> callable) {
        if (authHeader == null || !authHeader.startsWith(ClipticConst.BEARER_AUTH_HEADER_PREFIX)) {
            return new ErrorResponse(401, "Unauthenticated. Missing or invalid token");
        }

        String token = authHeader.substring(ClipticConst.BEARER_AUTH_HEADER_PREFIX.length());
        if (!token.equals("your-secret-token")) {
            return new ErrorResponse(401, "Access denied. Invalid token");
        }

        try {
            return callable.call();
        } catch (Exception e) {
            return new ErrorResponse(500, "Internal server error");
        }
    }
}
