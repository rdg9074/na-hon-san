package com.gwangjubob.livealone.backend.domain.repository;

import com.gwangjubob.livealone.backend.domain.entity.TipEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipRepository extends JpaRepository<TipEntity, Integer> {
}
