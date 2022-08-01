package com.gwangjubob.livealone.backend.domain.repository;

import com.gwangjubob.livealone.backend.domain.entity.TipEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface TipRepository extends JpaRepository<TipEntity, Integer> {
    Optional<TipEntity> findByIdx(Integer idx);
    Optional<TipEntity> findByTitle(String title);
    List<TipEntity> findByCategory(String category);
    List<TipEntity> findByUser(UserEntity userEntity);
    @Transactional
    @Modifying
    @Query(value = "UPDATE TipEntity t SET t.view = t.view+1 WHERE t.idx = :idx")
    void increaseViewCount(Integer idx);
}
