package com.gwangjubob.livealone.backend.domain.repository;

import com.gwangjubob.livealone.backend.domain.entity.TipCommentEntity;
import com.gwangjubob.livealone.backend.domain.entity.TipEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TipCommentRepository extends JpaRepository<TipCommentEntity, Integer> {

    Optional<TipCommentEntity> findByIdx(Integer idx);
    void deleteByIdx(Integer idx);

    List<TipCommentEntity> findByTip(TipEntity tipEntity);

    List<TipCommentEntity> findByUpIdx(Integer idx);
}
