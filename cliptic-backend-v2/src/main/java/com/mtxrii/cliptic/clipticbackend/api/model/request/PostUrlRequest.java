package com.mtxrii.cliptic.clipticbackend.api.model.request;

import lombok.Data;

@Data
public class PostUrlRequest {
    private final String originalUrl;
    private final String customAlias;
    private final String createdBy;
}
