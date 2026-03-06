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

        String originalUrl = requestBody.getOriginalUrl();
        String alias = requestBody.getAlias();
        boolean customAlias = true;
        if (alias == null || alias.isEmpty()) {
            alias = StringUtil.createRandomAlias(originalUrl);
            int retryAttempts = 0;
            while (this.linkRepository.existsByAlias(alias)) {
                if (retryAttempts >= ClipticConst.CREATE_RANDOM_ALIAS_MAX_RETRIES) {
                    return new ErrorResponse(500, "Failed to generate unique alias");
                }
                alias = StringUtil.createRandomAlias(originalUrl);
                retryAttempts ++;
            }
            log.info("Generated random alias {} for new link: {} ({} retries)", alias, originalUrl, retryAttempts);
            customAlias = false;
        }

        if (this.linkRepository.existsByAlias(alias.toUpperCase())) {
            return new ErrorResponse(409, "Alias already exists");
        }

        if (ClipticConst.RESERVED_ALIASES.contains(alias.toUpperCase())) {
            return new ErrorResponse(409, "Alias is reserved");
        }

        LinkEntity linkEntity = new LinkEntity(
                alias.toUpperCase(),
                customAlias,
                originalUrl,
                requestBody.getCreatedBy()
        );
        this.linkRepository.save(linkEntity);
        RedirectService.addToCache(alias.toUpperCase(), originalUrl);

        String aliasDetail = customAlias ? " (custom alias)" : ClipticConst.EMPTY_STRING;
        log.info("Created new link: {}{} -> {}", alias, aliasDetail, originalUrl);

        return new PostUrlResponse(200, ClipticConst.REDIRECT_BASE_URL + alias);
    }

    public Response getUrl(String alias) {
        if (alias == null) {
            List<LinkEntity> linkEntities = this.linkRepository.findAll();
            return new GetUrlResponse(linkEntities.toArray(new LinkEntity[0]));
        }

        alias = alias.toUpperCase();
        Optional<LinkEntity> linkEntity = this.linkRepository.findByAlias(alias);
        if (linkEntity.isEmpty()) {
            return new ErrorResponse(404, "No link found for alias: " + alias);
        }
        return new GetUrlResponse(linkEntity.get());
    }

    public Response deleteUrl(String alias, String createdBy) {
        alias = alias.toUpperCase();
        Optional<LinkEntity> linkEntity = this.linkRepository.findByAlias(alias);
        if (linkEntity.isEmpty()) {
            return new ErrorResponse(404, "No link found for alias: " + alias);
        }
        if (linkEntity.get().getCreatedBy() != null && GenericUtil.equals(linkEntity.get().getCreatedBy(), createdBy)) {
            return new ErrorResponse(403, "You are not authorized to delete this link");
        }
        log.info("Deleted link: {} -> {}", alias, linkEntity.get().getOriginalUrl());
        this.linkRepository.delete(linkEntity.get());
        return new Response(204);
    }
}
