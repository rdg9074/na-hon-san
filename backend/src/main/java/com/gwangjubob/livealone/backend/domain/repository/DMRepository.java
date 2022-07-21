package com.gwangjubob.livealone.backend.domain.repository;

import com.gwangjubob.livealone.backend.domain.entity.DMEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DMRepository extends JpaRepository<DMEntity, String> {

}
