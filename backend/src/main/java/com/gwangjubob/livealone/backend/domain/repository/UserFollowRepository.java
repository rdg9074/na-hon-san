package com.gwangjubob.livealone.backend.domain.repository;

import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserFollowEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserFollowRepository extends JpaRepository<UserFollowEntity, String> {
    Optional<UserFollowEntity> findByUserIdAndFollowId(String userId, String followId);

    void deleteByUserIdAndFollowId(String userId, String followId);
}
