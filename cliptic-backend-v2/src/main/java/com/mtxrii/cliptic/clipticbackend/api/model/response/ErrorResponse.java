package com.mtxrii.cliptic.clipticbackend.api.model.response;

import lombok.Getter;

import java.time.Instant;

@Getter
public class ErrorResponse extends Response {
    private final String timestamp;
    private final int status;
    private final String error;

    public ErrorResponse(int status, String error) {
        super(status);
        this.timestamp = Instant.now().toString();
        this.status = status;
        this.error = error;
    }
}
