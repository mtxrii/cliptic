package com.mtxrii.cliptic.clipticbackend.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UrlController {

    @PostMapping("/url")
    public ResponseEntity<String> postUrl(
            @RequestBody Map<String, String> requestBody
    ) {
        return ResponseEntity.ok(requestBody.get("url"));
    }
}
