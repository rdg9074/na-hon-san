package com.gwangjubob.livealone.backend.domain.entity;

import lombok.*;
import org.apache.catalina.User;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(name="users")
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
public class UserEntity {

    @Id
    private String id;
    private String password;
    private String nickname;
    private String area;
    @Column(name="follow_open")
    private Boolean followOpen;
    @Column(name="follower_open")
    private Boolean followerOpen;
    @Column(name="like_notice")
    private Boolean likeNotice;
    @Column(name="follow_notice")
    private Boolean followNotice;
    @Column(name="comment_notice")
    private Boolean commentNotice;
    @Column(name="reply_notice")
    private Boolean replyNotice;
    @Column(name="profile_msg")
    private String profileMsg;
    @Column(name="profile_img")
    private String profileImg;
    private String social;
    @Column(name="background_img")
    private String backgroundImg;

    @OneToMany(mappedBy = "user")
    private List<NoticeEntity> notices = new ArrayList<>();

    @Builder
    public UserEntity(String id, String password, String nickname){ //회원 가입 빌더
        this.id = id;
        this.password = password;
        this.nickname = nickname;
    }


}
