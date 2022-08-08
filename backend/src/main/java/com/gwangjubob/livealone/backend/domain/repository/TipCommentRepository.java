package com.gwangjubob.livealone.backend.domain.repository;

import com.gwangjubob.livealone.backend.domain.entity.TipCommentEntity;
import com.gwangjubob.livealone.backend.domain.entity.TipEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TipCommentRepository extends JpaRepository<TipCommentEntity, Integer> {

    Optional<TipCommentEntity> findByIdx(Integer idx);
    void deleteByIdx(Integer idx);

    List<TipCommentEntity> findByTip(TipEntity tipEntity);

    List<TipCommentEntity> findByUpIdx(Integer idx);

    @Query(value = "SELECT COUNT(tc.idx) FROM TipCommentEntity tc WHERE tc.tip.idx = :idx")
    int getCommentCount(Integer idx);

    List<TipCommentEntity> findByTipOrderByIdxDesc(TipEntity tipEntity);

    @Query(value = "SELECT t FROM TipCommentEntity t WHERE t.tip = :tipEntity " +
            "ORDER BY ( CASE WHEN t.upIdx = 0 THEN t.idx ELSE t.upIdx END), t.upIdx DESC")
    List<TipCommentEntity> findByTipOrderBycomment(TipEntity tipEntity);
}
