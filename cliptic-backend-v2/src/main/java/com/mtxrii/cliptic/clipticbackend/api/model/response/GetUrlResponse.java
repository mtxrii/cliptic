package com.mtxrii.cliptic.clipticbackend.api.model.response;

import com.mtxrii.cliptic.clipticbackend.ClipticConst;
import com.mtxrii.cliptic.clipticbackend.db.entity.LinkEntity;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
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
                    ClipticConst.REDIRECT_BASE_URL + link.getAlias(),
                    link.getCreatedBy()
            ));
        }
    }
}
