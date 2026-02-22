package com.mtxrii.cliptic.clipticbackend.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@Table(
    name = "link",
    indexes = {
        @Index(name = "idx_alias", columnList = "alias")
    }
)
public class LinkEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(length = 16, nullable = false, unique = true)
    private String alias;

    @Column(name = "is_custom_alias", nullable = false)
    private boolean isCustomAlias;

    @Column(name = "original_url", nullable = false)
    private String originalUrl;

    @Column(name = "created_time", nullable = false)
    private Instant createdTime;

    @Column(name = "created_by")
    private String createdBy;

    public LinkEntity(String alias, boolean isCustomAlias, String originalUrl, String createdBy) {
        this.alias = alias;
        this.isCustomAlias = isCustomAlias;
        this.originalUrl = originalUrl;
        this.createdBy = createdBy;
        this.createdTime = Instant.now();
    }

    public LinkEntity() { }
}
