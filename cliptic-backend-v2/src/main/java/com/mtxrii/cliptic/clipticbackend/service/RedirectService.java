package com.mtxrii.cliptic.clipticbackend.service;

import com.mtxrii.cliptic.clipticbackend.db.LinkRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class RedirectService {
    private static final Map<String, String> REDIRECT_CACHE = new HashMap<>();

    private final LinkRepository linkRepository;

    public RedirectService(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    public Optional<String> redirect(String alias) {
        if (REDIRECT_CACHE.containsKey(alias)) {
            return Optional.of(REDIRECT_CACHE.get(alias));
        }

        if (this.linkRepository.findByAlias(alias).isEmpty()) {
            return Optional.empty();
        }

        String redirectUrl = this.linkRepository.findByAlias(alias).get().getOriginalUrl();
        REDIRECT_CACHE.put(alias, redirectUrl);
        return Optional.of(redirectUrl);
    }

    public static void addToCache(String alias, String redirectUrl) {
        REDIRECT_CACHE.put(alias, redirectUrl);
    }
}
