package com.gwangjubob.livealone.backend.domain.repository;

import com.gwangjubob.livealone.backend.domain.entity.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<NoticeEntity, Integer> {
   List<NoticeEntity> findByUserIdAndNoticeType(String id, String type);
   List<NoticeEntity> findByUserId(String id);
    Optional<NoticeEntity> findByIdx(int idx);

    @Query(value = "SELECT COUNT(n.idx) FROM NoticeEntity n WHERE n.user.id=:id and n.read=false")
    long findCountNotice(String id);

    NoticeEntity findByNoticeTypeAndFromUserIdAndPostTypeAndPostIdx(String like, String userId, String tip, Integer postIdx);

    List<NoticeEntity> findAllByNoticeTypeAndFromUserIdAndPostTypeAndPostIdx(String reply, String userId, String tip, Integer postIdx);

    List<NoticeEntity> findAllByPostIdxAndPostType(Integer idx, String tip);
}
