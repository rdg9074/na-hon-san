package com.gwangjubob.livealone.backend.domain.repository;

import com.gwangjubob.livealone.backend.domain.entity.DealEntity;
import com.gwangjubob.livealone.backend.domain.entity.PopularDealEntity;
import com.gwangjubob.livealone.backend.domain.entity.PopularFollowEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DealRepository extends JpaRepository<DealEntity, Integer> {
    List<DealEntity> findByUser(UserEntity userEntity);
    List<DealEntity> findTop6ByCategoryAndStateAndAreaOrderByViewDesc(String category,String state,String area);

    List<DealEntity> findByCategory(String category);
}
