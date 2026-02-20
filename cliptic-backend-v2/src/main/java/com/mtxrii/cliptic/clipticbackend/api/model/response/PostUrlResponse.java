package com.mtxrii.cliptic.clipticbackend.api.model.response;

import lombok.Getter;

@Getter
public class PostUrlResponse extends Response {
    private final String shortenedUrl;

    public PostUrlResponse(int code, String shortenedUrl) {
        super(code);
        this.shortenedUrl = shortenedUrl;
    }
}
