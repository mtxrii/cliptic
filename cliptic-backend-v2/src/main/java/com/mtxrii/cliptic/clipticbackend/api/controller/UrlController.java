package com.mtxrii.cliptic.clipticbackend.api.controller;

import com.mtxrii.cliptic.clipticbackend.api.model.request.PostUrlRequest;
import com.mtxrii.cliptic.clipticbackend.api.model.response.Response;
import com.mtxrii.cliptic.clipticbackend.service.UrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UrlController {
    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/url")
    public ResponseEntity<Response> postUrl(
            @RequestBody PostUrlRequest requestBody
    ) {
        Response response = this.urlService.postUrl(requestBody);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
