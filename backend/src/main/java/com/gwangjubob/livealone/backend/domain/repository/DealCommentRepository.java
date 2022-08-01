package com.gwangjubob.livealone.backend.domain.repository;

import com.gwangjubob.livealone.backend.domain.entity.DealCommentEntity;
import com.gwangjubob.livealone.backend.domain.entity.DealEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DealCommentRepository extends JpaRepository<DealCommentEntity, Integer> {
    List<DealCommentEntity> findByDeal(DealEntity dealEntity);

    List<DealCommentEntity> findByUpIdx(Integer idx);
}
