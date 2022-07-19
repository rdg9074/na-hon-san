package com.gwangjubob.livealone.backend.domain.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Table(name="notices")
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Entity
public class NoticeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx; // 알림글 번호

    @ManyToOne
    @JoinColumn(name="user_id")
    private UserEntity user; // 로그인 한 사용자 아이디

    @Column(name = "notice_type")
    private String noticeType; // 알림 타입 - like/comment/reply/follow

    @Column(name = "post_type")
    private String postType; // 게시글 타입 - tip/deal

    @Column(name="post_idx")
    private Integer postIdx; // 게시글 번호

    private Boolean read; // 읽음 여부

    @Column(name = "from_user_nickname")
    private String fromUserNickname;

    @Builder
    public NoticeEntity(Integer idx, UserEntity user, String noticeType, String postType, Integer postIdx, Boolean read, String fromUserNickname) {
        this.idx = idx;
        this.user = user;
        this.noticeType = noticeType;
        this.postType = postType;
        this.postIdx = postIdx;
        this.read = read;
        this.fromUserNickname = fromUserNickname;
    }
}