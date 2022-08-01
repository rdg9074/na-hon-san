package com.gwangjubob.livealone.backend.domain.repository;

import com.gwangjubob.livealone.backend.domain.entity.DealEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserLikeDealsEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserLikeTipsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserLikeDealsRepository extends JpaRepository<UserLikeDealsEntity, Integer> {
    Optional<UserLikeDealsEntity> findByDealAndUser(DealEntity deal, UserEntity user);
}
