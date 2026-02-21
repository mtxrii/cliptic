package com.mtxrii.cliptic.clipticbackend.service;

import com.mtxrii.cliptic.clipticbackend.db.LinkRepository;
import org.springframework.stereotype.Service;

@Service
public class RedirectService {
    private final LinkRepository linkRepository;

    public RedirectService(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    public String redirect(String alias) {
        if (this.linkRepository.findByAlias(alias).isEmpty()) {
            return null;
        }
        return this.linkRepository.findByAlias(alias).get().getOriginalUrl();
    }
}
