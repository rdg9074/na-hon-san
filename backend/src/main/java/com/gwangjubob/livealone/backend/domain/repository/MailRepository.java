package com.gwangjubob.livealone.backend.domain.repository;

import com.gwangjubob.livealone.backend.domain.entity.MailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MailRepository extends JpaRepository<MailEntity, String> {
}
