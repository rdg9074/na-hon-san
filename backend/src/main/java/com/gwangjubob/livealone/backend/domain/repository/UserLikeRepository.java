package com.gwangjubob.livealone.backend.domain.repository;

import com.gwangjubob.livealone.backend.domain.entity.UserLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserLikeRepository extends JpaRepository<UserLikeEntity, Integer>{
    List<UserLikeEntity> findByUserId (String userId);
}

