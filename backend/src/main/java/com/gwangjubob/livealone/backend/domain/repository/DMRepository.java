package com.gwangjubob.livealone.backend.domain.repository;

import com.gwangjubob.livealone.backend.domain.entity.DMEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import com.gwangjubob.livealone.backend.dto.dm.DMViewDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DMRepository extends JpaRepository<DMEntity, String> {
    @Query(value = "select * from (select *from dms where to_user_id=:#{#toUserId} or from_user_id=:#{#fromUserId} order by dms.time desc limit 9999999 ) as temp group by temp.from_user_id;", nativeQuery = true)
    List<DMEntity> findListViews(String toUserId);
    @Query(value = "select count(d.idx) from DMEntity d where d.toUserId.id=:toUserId and d.read=false and d.fromUserId.id = :fromUserId")
    int findCount(String toUserId,String fromUserId);
    @Query(value = "select COUNT(t.idx)  from (SELECT * from dms where is_read = FALSE and to_user_id = :#{#id}) t;", nativeQuery = true)
    long findByCountDM(String id);
    @Query(value = "select d from DMEntity d where (d.idx < :lastIdx and d.toUserId=:toId and d.fromUserId=:fromId)or(d.idx < :lastIdx and d.toUserId=:fromId and d.fromUserId=:toId) order by d.time desc")
    Slice<DMEntity> findByToUserIdAndFromUserId(UserEntity toId, UserEntity fromId, Integer lastIdx, Pageable pageable);

    @Query(value = "select d.toUserId from DMEntity d where d.fromUserId=:userId group by d.toUserId")
    List<UserEntity> findByfromUserIdGrouptoUserId(UserEntity userId);

    @Query(value = "select d.fromUserId from DMEntity d where d.toUserId=:userId group by d.fromUserId")
    List<UserEntity> findBytoUserIdGroupFromUserId(UserEntity userId);

    @Query(value = "select d from DMEntity d where d.idx = (select max(d2.idx) from DMEntity d2 where (d2.fromUserId = :userId and d2.toUserId = :otherId) or (d2.fromUserId= :otherId and d2.toUserId = :userId))")
    Optional<DMEntity> findByUserIdAndOtherId(UserEntity userId, UserEntity otherId);


    Optional<DMEntity> findTop1ByOrderByIdxDesc();
}
