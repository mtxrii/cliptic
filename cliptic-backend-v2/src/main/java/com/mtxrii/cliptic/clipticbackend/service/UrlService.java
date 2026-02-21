package com.mtxrii.cliptic.clipticbackend.service;

import com.mtxrii.cliptic.clipticbackend.api.model.request.PostUrlRequest;
import com.mtxrii.cliptic.clipticbackend.api.model.response.ErrorResponse;
import com.mtxrii.cliptic.clipticbackend.api.model.response.GetUrlResponse;
import com.mtxrii.cliptic.clipticbackend.api.model.response.PostUrlResponse;
import com.mtxrii.cliptic.clipticbackend.api.model.response.Response;
import com.mtxrii.cliptic.clipticbackend.db.LinkRepository;
import com.mtxrii.cliptic.clipticbackend.db.entity.LinkEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UrlService {
    private final LinkRepository linkRepository;

    public UrlService(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    public Response postUrl(PostUrlRequest requestBody) {
        if (requestBody.getOriginalUrl() == null) {
            return new ErrorResponse(400, "Original URL cannot be empty");
        }

        String alias = requestBody.getAlias();
        if (alias == null) {
            alias = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            if (this.linkRepository.existsByAlias(alias)) {
                alias = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            }
        }

        if (this.linkRepository.existsByAlias(alias)) {
            return new ErrorResponse(409, "Alias already exists");
        }

        LinkEntity linkEntity = new LinkEntity(
                alias,
                requestBody.getOriginalUrl(),
                requestBody.getCreatedBy()
        );
        this.linkRepository.save(linkEntity);
        return new PostUrlResponse(200, "https://sample.com/" + alias);
    }

    public Response getUrl(String alias) {
        Optional<LinkEntity> linkEntity = this.linkRepository.findByAlias(alias);
        if (linkEntity.isEmpty()) {
            return new ErrorResponse(404, "No link found for alias: " + alias);
        }
        return new GetUrlResponse(linkEntity.get());
    }
}
