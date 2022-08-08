package com.gwangjubob.livealone.backend.domain.repository;

import com.gwangjubob.livealone.backend.domain.entity.DealCommentEntity;
import com.gwangjubob.livealone.backend.domain.entity.DealEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DealCommentRepository extends JpaRepository<DealCommentEntity, Integer> {
    List<DealCommentEntity> findByDeal(DealEntity dealEntity);

    List<DealCommentEntity> findByUpIdx(Integer idx);

    Optional<DealCommentEntity> findByIdx(Integer upIdx);

    @Query(value = "SELECT d FROM DealCommentEntity d WHERE d.deal = :deal " +
            "ORDER BY (CASE WHEN d.upIdx = 0 THEN d.idx ELSE d.upIdx END), d.upIdx DESC")
    List<DealCommentEntity> findByDealOrderByComment(DealEntity deal);
}
