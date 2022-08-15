package com.gwangjubob.livealone.backend.domain.repository;

import com.gwangjubob.livealone.backend.domain.entity.ApiActionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiActionRepository extends JpaRepository<ApiActionEntity, Integer> {
}
