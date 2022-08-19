package com.gwangjubob.livealone.backend.domain.repository;

import com.gwangjubob.livealone.backend.domain.entity.TipEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserLikeTipsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserLikeTipsRepository extends JpaRepository<UserLikeTipsEntity, Integer> {
    Optional<UserLikeTipsEntity> findByUserAndTip(UserEntity userEntity, TipEntity tipEntity);

    @Query(value = "SELECT count(ult.idx) FROM UserLikeTipsEntity ult WHERE ult.tip.idx = :idx")
    int getLikeCount(Integer idx);
}
