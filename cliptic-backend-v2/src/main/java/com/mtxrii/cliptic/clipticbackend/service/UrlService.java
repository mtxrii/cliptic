package com.mtxrii.cliptic.clipticbackend.service;

import com.mtxrii.cliptic.clipticbackend.ClipticConst;
import com.mtxrii.cliptic.clipticbackend.api.model.request.PostUrlRequest;
import com.mtxrii.cliptic.clipticbackend.api.model.response.ErrorResponse;
import com.mtxrii.cliptic.clipticbackend.api.model.response.GetUrlResponse;
import com.mtxrii.cliptic.clipticbackend.api.model.response.PostUrlResponse;
import com.mtxrii.cliptic.clipticbackend.api.model.response.Response;
import com.mtxrii.cliptic.clipticbackend.db.LinkRepository;
import com.mtxrii.cliptic.clipticbackend.db.entity.LinkEntity;
import com.mtxrii.cliptic.clipticbackend.util.GenericUtil;
import com.mtxrii.cliptic.clipticbackend.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
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
        boolean customAlias = true;
        if (alias == null) {
            alias = StringUtil.createRandomAlias(requestBody.getOriginalUrl());
            if (this.linkRepository.existsByAlias(alias)) {
                alias = StringUtil.createRandomAlias(requestBody.getOriginalUrl());
            }
            customAlias = false;
        }

        if (this.linkRepository.existsByAlias(alias)) {
            return new ErrorResponse(409, "Alias already exists");
        }

        LinkEntity linkEntity = new LinkEntity(
                alias,
                customAlias,
                requestBody.getOriginalUrl(),
                requestBody.getCreatedBy()
        );
        this.linkRepository.save(linkEntity);
        RedirectService.addToCache(alias, requestBody.getOriginalUrl());

        String aliasDetail = customAlias ? " (custom alias)" : "";
        log.info("Created new link: {}{} -> {}", alias, aliasDetail, requestBody.getOriginalUrl());

        return new PostUrlResponse(200, ClipticConst.REDIRECT_BASE_URL + alias);
    }

    public Response getUrl(String alias) {
        if (alias == null) {
            List<LinkEntity> linkEntities = this.linkRepository.findAll();
            return new GetUrlResponse(linkEntities.toArray(new LinkEntity[0]));
        }

        Optional<LinkEntity> linkEntity = this.linkRepository.findByAlias(alias);
        if (linkEntity.isEmpty()) {
            return new ErrorResponse(404, "No link found for alias: " + alias);
        }
        return new GetUrlResponse(linkEntity.get());
    }

    public Response deleteUrl(String alias, String createdBy) {
        Optional<LinkEntity> linkEntity = this.linkRepository.findByAlias(alias);
        if (linkEntity.isEmpty()) {
            return new ErrorResponse(404, "No link found for alias: " + alias);
        }
        if (GenericUtil.equals(linkEntity.get().getCreatedBy(), createdBy)) {
            return new ErrorResponse(403, "You are not authorized to delete this link");
        }
        log.info("Deleted link: {} -> {}", alias, linkEntity.get().getOriginalUrl());
        this.linkRepository.delete(linkEntity.get());
        return new Response(204);
    }
}
