package com.gwangjubob.livealone.backend.domain.repository;

import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import com.gwangjubob.livealone.backend.service.JwtService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByIdAndPassword(String id,String password);
    Optional<UserEntity> findByNickname(String nickname);

    Optional<UserEntity> findByIdAndSocial(String id, String social);
}
