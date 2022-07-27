package com.gwangjubob.livealone.backend.domain.repository;

import com.gwangjubob.livealone.backend.domain.entity.UserFollowEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserFeedRepository extends JpaRepository<UserFollowEntity, String> {
    Optional<UserFollowEntity> findByUserIdAndFollowId(String userId, String followId);
    List<UserFollowEntity> findByUserId(String userId);
    List<UserFollowEntity> findByFollowId(String followId);
    void deleteByUserIdAndFollowId(String userId, String followId);

    int countByFollowId(String id);
    int countByUserId(String id);

    List<UserFollowEntity> findByUserIdAndFollowNicknameContaining(String id, String keyword);

    List<UserFollowEntity> findByFollowIdAndUserNicknameContaining(String id, String keyword);
}
