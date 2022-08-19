package com.gwangjubob.livealone.backend.domain.repository;

import com.gwangjubob.livealone.backend.domain.entity.UserCategoryEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCategoryRepository extends JpaRepository<UserCategoryEntity, Integer>{
    List<UserCategoryEntity> findByUser(UserEntity user);
}

