package com.mtxrii.cliptic.clipticbackend.api.controller;

import com.mtxrii.cliptic.clipticbackend.api.model.request.PostUrlRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UrlController {

    @PostMapping("/url")
    public ResponseEntity<String> postUrl(
            @RequestBody PostUrlRequest requestBody
    ) {
        return ResponseEntity.ok(requestBody.toString());
    }
}
