package com.mtxrii.cliptic.clipticbackend.service;

import com.mtxrii.cliptic.clipticbackend.db.LinkRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RedirectService {
    private static final Map<String, String> REDIRECT_CACHE = new HashMap<>();

    private final LinkRepository linkRepository;

    public RedirectService(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    public String redirect(String alias) {
        if (REDIRECT_CACHE.containsKey(alias)) {
            return REDIRECT_CACHE.get(alias);
        }

        if (this.linkRepository.findByAlias(alias).isEmpty()) {
            return null;
        }

        String redirectUrl = this.linkRepository.findByAlias(alias).get().getOriginalUrl();
        REDIRECT_CACHE.put(alias, redirectUrl);
        return redirectUrl;
    }
}
