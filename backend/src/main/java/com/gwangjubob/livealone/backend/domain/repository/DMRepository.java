package com.gwangjubob.livealone.backend.domain.repository;

import com.gwangjubob.livealone.backend.domain.entity.DMEntity;
import com.gwangjubob.livealone.backend.dto.dm.DMViewDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DMRepository extends JpaRepository<DMEntity, String> {
    @Query(value = "select * from (select *from dms where to_user_id=:#{#toUserId} order by dms.time desc limit 9999999 ) as temp group by temp.from_user_id;", nativeQuery = true)
    List<DMEntity> findListViews(String toUserId);
    @Query(value = "select count(d.idx) from DMEntity d where d.toUserId=:toUserId and d.read=false and d.fromUserId = :fromUserId")
    int findCount(String toUserId,String fromUserId);
}
