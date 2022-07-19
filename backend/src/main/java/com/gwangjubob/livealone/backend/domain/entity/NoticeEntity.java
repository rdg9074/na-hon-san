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
    private Integer idx;

    @ManyToOne
    @JoinColumn(name="user_id")
    private UserEntity user;

    @Column(name = "notice_type")
    private String noticeType;

    @Column(name = "post_type")
    private String postType;

    @Column(name="post_idx")
    private Integer postIdx;

    private Boolean read;

    @Column(name = "from_user_nickname")
    private String fromUserNickname;

}