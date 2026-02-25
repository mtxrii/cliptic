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

@RestController
public class UrlController {
    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping(ClipticConst.MAPPING_URL_CONTROLLER)
    public ResponseEntity<Response> postUrl(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody PostUrlRequest requestBody
    ) {
        if (!isValidToken(authHeader)) {
            return getUnauthorizedResponse();
        }
        Response response = this.urlService.postUrl(requestBody);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping(ClipticConst.MAPPING_URL_CONTROLLER)
    public ResponseEntity<Response> getUrl(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(name = ClipticConst.ALIAS_REQUEST_PARAM, required = false) String alias
    ) {
        if (!isValidToken(authHeader)) {
            return getUnauthorizedResponse();
        }
        Response response = this.urlService.getUrl(alias);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping(ClipticConst.MAPPING_URL_CONTROLLER)
    public ResponseEntity<Response> deleteUrl(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(name = ClipticConst.ALIAS_REQUEST_PARAM, required = true) String alias,
            @RequestParam(name = ClipticConst.OWNER_REQUEST_PARAM, required = true) String owner
    ) {
        if (!isValidToken(authHeader)) {
            return getUnauthorizedResponse();
        }
        Response response = this.urlService.deleteUrl(alias, owner);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    private boolean isValidToken(String authHeader) {
        return authHeader != null
                && authHeader.startsWith("Bearer ")
                && authHeader.substring(7).equals("your-secret-token");
    }

    private ResponseEntity<Response> getUnauthorizedResponse() {
        Response response = new ErrorResponse(401, "Unauthorized");
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
