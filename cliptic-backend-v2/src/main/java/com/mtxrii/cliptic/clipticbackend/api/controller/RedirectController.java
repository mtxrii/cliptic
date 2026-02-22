package com.mtxrii.cliptic.clipticbackend.api.controller;

import com.mtxrii.cliptic.clipticbackend.ClipticConst;
import com.mtxrii.cliptic.clipticbackend.service.RedirectService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class RedirectController {
    private final RedirectService redirectService;

    public RedirectController(RedirectService redirectService) {
        this.redirectService = redirectService;
    }

    @GetMapping(ClipticConst.REDIRECT_URL_CONTROLLER)
    public ResponseEntity<Void> redirectToUrl(
            @PathVariable String alias
    ) {
        if (alias == null) {
            return ResponseEntity.status(404).build();
        }
        Optional<String> redirectUrl = this.redirectService.redirect(alias);
        if (redirectUrl.isEmpty()) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity
                .status(302)
                .header(HttpHeaders.LOCATION, redirectUrl.get())
                .build();
    }
}
