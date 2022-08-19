package com.gwangjubob.livealone.backend.domain.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Table(name="notices")
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Entity
public class NoticeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx; // 알림글 번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private UserEntity user;

    @Column(name = "notice_type")
    private String noticeType; // 알림 타입 - like/comment/reply/follow

    @Column(name = "post_type")
    private String postType; // 게시글 타입 - tip/deal

    @Column(name="post_idx")
    private Integer postIdx; // 게시글 번호

    @Column(name="comment_idx")
    private Integer commentIdx;

    @Column(name="comment_up_idx")
    private Integer commentUpIdx;

    @Column(name="isread")
    private Boolean read; // 읽음 여부

    @Column(name = "from_user_id")
    private String fromUserId;

    @Column(name = "time")
    LocalDateTime time;

}