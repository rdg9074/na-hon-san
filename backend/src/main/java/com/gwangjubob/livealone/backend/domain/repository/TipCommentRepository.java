package com.gwangjubob.livealone.backend.domain.repository;

import com.gwangjubob.livealone.backend.domain.entity.TipCommentEntity;
import com.gwangjubob.livealone.backend.domain.entity.TipEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TipCommentRepository extends JpaRepository<TipCommentEntity, Integer> {
    TipCommentEntity findByIdx(Integer idx);
    void deleteByIdx(Integer idx);

    List<TipCommentEntity> findByTip(TipEntity tipEntity);
}
