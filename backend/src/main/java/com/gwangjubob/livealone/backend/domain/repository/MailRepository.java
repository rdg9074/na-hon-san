package com.gwangjubob.livealone.backend.domain.repository;

import com.gwangjubob.livealone.backend.domain.entity.MailEntity;
import com.gwangjubob.livealone.backend.dto.mail.MailCheckDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


/**
 *  select COUNT(id) from mail where TIMESTAMPDIFF(SECOND, time, now()) < 180 and id = #{userId} and auth_key = #{authKey};
 */
public interface MailRepository extends JpaRepository<MailEntity, String> {
    @Query(value = "select Count(m.id) from MailEntity m where TIMESTAMPDIFF(SECOND, m.time, now()) < 180 and m.id=:id and m.number=:number and m.type = :typed")
    int findById(String id, String number, String typed);
}
