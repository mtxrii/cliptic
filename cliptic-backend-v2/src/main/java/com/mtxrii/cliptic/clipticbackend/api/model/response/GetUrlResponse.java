package com.mtxrii.cliptic.clipticbackend.api.model.response;

import com.mtxrii.cliptic.clipticbackend.db.entity.LinkEntity;

import java.util.ArrayList;
import java.util.List;

public class GetUrlResponse extends Response {
    private record UrlRecord(String alias, String originalUrl, String redirectUrl, String createdBy) { }

    private final List<UrlRecord> urls;

    public GetUrlResponse(LinkEntity... linkEntities) {
        super(200);
        this.urls = new ArrayList<>();
        for (LinkEntity link : linkEntities) {
            this.urls.add(new UrlRecord(
                    link.getAlias(),
                    link.getOriginalUrl(),
                    "https://sample.com/" + link.getAlias(),
                    link.getCreatedBy()
            ));
        }
    }
}
