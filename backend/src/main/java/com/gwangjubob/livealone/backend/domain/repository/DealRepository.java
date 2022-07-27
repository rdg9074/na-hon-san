package com.gwangjubob.livealone.backend.domain.repository;

import com.gwangjubob.livealone.backend.domain.entity.DealEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DealRepository extends JpaRepository<DealEntity, Integer> {
    List<DealEntity> findByUser(UserEntity userEntity);
}
