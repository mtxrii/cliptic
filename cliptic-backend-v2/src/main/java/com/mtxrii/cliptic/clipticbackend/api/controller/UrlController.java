package com.mtxrii.cliptic.clipticbackend.api.controller;

import com.mtxrii.cliptic.clipticbackend.api.model.request.PostUrlRequest;
import com.mtxrii.cliptic.clipticbackend.api.model.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UrlController {

    @PostMapping("/url")
    public ResponseEntity<Response> postUrl(
            @RequestBody PostUrlRequest requestBody
    ) {
        return ResponseEntity.ok(new Response(200) {{String body = requestBody.toString();}});
    }
}
