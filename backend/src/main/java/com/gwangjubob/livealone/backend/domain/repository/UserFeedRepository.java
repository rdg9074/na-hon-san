package com.gwangjubob.livealone.backend.domain.repository;

import com.gwangjubob.livealone.backend.domain.entity.PopularFollowEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserFollowEntity;
import com.gwangjubob.livealone.backend.dto.feed.PopularFollowDto;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface UserFeedRepository extends JpaRepository<UserFollowEntity, String> {
    List<UserFollowEntity> findByUserIdAndFollowId(String userId, String followId);
    List<UserFollowEntity> findByUserId(String userId);
    List<UserFollowEntity> findByFollowId(String followId);
    @Transactional
    void deleteByUserIdAndFollowId(String userId, String followId);

    int countByFollowId(String id);
    int countByUserId(String id);

    List<UserFollowEntity> findByUserIdAndFollowNicknameContaining(String id, String keyword);

    List<UserFollowEntity> findByFollowIdAndUserNicknameContaining(String id, String keyword);
    @Query(value = "select new PopularFollowEntity(f.idx, f.followId ,f.followNickname, COUNT(f.followId))from PopularFollowEntity f WHERE NOT f.followId = :decodeId GROUP BY f.followId ORDER BY COUNT(f.followId) DESC ")
    List<PopularFollowEntity> popularFollowerList(String decodeId,Pageable pageable);

    Optional<Object> findByUserIdAndFollowNickname(String userId, String followNickname);
}
