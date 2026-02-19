package com.mtxrii.cliptic.clipticbackend.api.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostUrlRequest {
    private final String originalUrl;
    private final String alias;
    private final String createdBy;
}
