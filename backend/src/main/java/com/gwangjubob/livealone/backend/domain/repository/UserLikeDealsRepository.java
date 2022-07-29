package com.gwangjubob.livealone.backend.domain.repository;

import com.gwangjubob.livealone.backend.domain.entity.UserLikeDealsEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserLikeTipsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLikeDealsRepository extends JpaRepository<UserLikeDealsEntity, Integer> {
}
