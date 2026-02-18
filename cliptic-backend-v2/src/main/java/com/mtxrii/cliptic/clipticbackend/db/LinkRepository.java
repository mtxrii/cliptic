package com.mtxrii.cliptic.clipticbackend.db;

import com.mtxrii.cliptic.clipticbackend.db.entity.LinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LinkRepository extends JpaRepository<LinkEntity, UUID> {

    Optional<LinkEntity> findByAlias(String alias);

    boolean existsByAlias(String alias);
}
