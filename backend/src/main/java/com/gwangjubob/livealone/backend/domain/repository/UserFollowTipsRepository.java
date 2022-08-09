package com.gwangjubob.livealone.backend.domain.repository;

import com.gwangjubob.livealone.backend.domain.entity.TipEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserFollowEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserFollowTipsEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserFollowTipsRepository extends JpaRepository<UserFollowTipsEntity, String> {

    @Query(value = "select * from tips t where t.idx < :lastIdx and user_id  In (SELECT follow_id  from user_follows where user_id =:#{#id} )  ORDER  by t.time desc",
            countQuery = "SELECT COUNT(*) FROM tips",
            nativeQuery = true)
    Slice<UserFollowTipsEntity> findTips(String id, int lastIdx, Pageable pageable);


    Optional<UserFollowTipsEntity> findTop1ByOrderByIdxDesc();
}
