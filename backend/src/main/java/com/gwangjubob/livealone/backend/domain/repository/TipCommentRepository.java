package com.gwangjubob.livealone.backend.domain.repository;

import com.gwangjubob.livealone.backend.domain.entity.TipCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipCommentRepository extends JpaRepository<TipCommentEntity, Integer> {
    TipCommentEntity findByIdx(Integer idx);

    void deleteByIdx(Integer idx);
}
