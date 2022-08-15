package com.gwangjubob.livealone.backend.domain.repository;

import com.gwangjubob.livealone.backend.domain.entity.ApiActionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApiActionRepository extends JpaRepository<ApiActionEntity, Integer> {
    Optional<ApiActionEntity> findByIdx(int i);
}
