package com.mtxrii.cliptic.clipticbackend.api.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedirectController {

    @GetMapping("/{alias}")
    public ResponseEntity<Void> redirectToUrl(
            @PathVariable String alias
    ) {
        String redirectUrl = "https://web.postman.co/"; // This is temporary
        return ResponseEntity
                .status(302)
                .header(HttpHeaders.LOCATION, redirectUrl)
                .build();
    }
}
