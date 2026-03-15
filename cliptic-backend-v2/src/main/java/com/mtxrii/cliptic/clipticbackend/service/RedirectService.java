package com.mtxrii.cliptic.clipticbackend.service;

import com.mtxrii.cliptic.clipticbackend.ClipticConst;
import com.mtxrii.cliptic.clipticbackend.db.LinkRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class RedirectService {
    private static final Map<String, String> REDIRECT_CACHE = new LinkedHashMap<>(100, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
            return size() > ClipticConst.MAX_CACHE_SIZE;
        }
    };

    private final LinkRepository linkRepository;

    public RedirectService(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    public Optional<String> redirect(String alias) {
        alias = alias.toUpperCase();
        if (REDIRECT_CACHE.containsKey(alias)) {
            return Optional.of(REDIRECT_CACHE.get(alias));
        }

        if (this.linkRepository.findByAlias(alias).isEmpty()) {
            return Optional.empty();
        }

        String redirectUrl = this.linkRepository.findByAlias(alias).get().getOriginalUrl();
        addToCache(alias, redirectUrl);
        return Optional.of(redirectUrl);
    }

    public static void addToCache(String alias, String redirectUrl) {
        REDIRECT_CACHE.put(alias, redirectUrl);
    }
}
