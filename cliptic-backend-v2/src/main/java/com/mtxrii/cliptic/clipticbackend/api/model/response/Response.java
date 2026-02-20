package com.mtxrii.cliptic.clipticbackend.api.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Response {
    @JsonIgnore
    private final int code;

    public Response(int code) {
        this.code = code;
    }

    @JsonIgnore
    public int getStatusCode() {
        return this.code;
    }
}
