package com.gwangjubob.livealone.backend.domain.repository;

import com.gwangjubob.livealone.backend.domain.entity.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<NoticeEntity, Integer> {
    // 사용자 아이디로 알림 전체 조회
    // select n from Notices n where n.userid = ?
    // userId user_id
   List<NoticeEntity> findByUserIdAndNoticeType(String id, String type);
   List<NoticeEntity> findByUserId(String id);
    Optional<NoticeEntity> findByIdx(int idx);

    @Query(value = "SELECT COUNT(n.idx) FROM NoticeEntity n WHERE n.user.id=:id and n.read=false")
    long findCountNotice(String id);
}
