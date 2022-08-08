package com.gwangjubob.livealone.backend.domain.repository;

import com.gwangjubob.livealone.backend.domain.entity.NoticeEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<NoticeEntity, Integer> {
 List<NoticeEntity> findByUserIdAndNoticeType(String id, String type);
    Optional<NoticeEntity> findByIdx(int idx);

    Optional<NoticeEntity> findByNoticeTypeAndFromUserIdAndPostTypeAndPostIdx(String like, String userId, String tip, Integer postIdx);
    List<NoticeEntity> findAllByNoticeTypeAndFromUserIdAndPostTypeAndPostIdx(String reply, String userId, String tip, Integer postIdx);
    List<NoticeEntity> findAllByPostIdxAndPostType(Integer idx, String tip);
    Optional<NoticeEntity> findByNoticeTypeAndFromUserIdAndPostTypeAndPostIdxAndCommentIdx(String reply, String userId, String tip, Integer postIdx, Integer idx);

    List<NoticeEntity> findAllByNoticeTypeAndPostTypeAndPostIdxAndCommentUpIdx(String reply, String tip, Integer postIdx, Integer idx);

 Optional<NoticeEntity> findByNoticeTypeAndPostTypeAndPostIdxAndCommentIdx(String comment, String tip, Integer postIdx, Integer idx);

 Optional<NoticeEntity> findByNoticeTypeAndUserIdAndFromUserId(String follow, String fromId, String toId);
}
