package com.gwangjubob.livealone.backend.domain.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
    @Column(name="profile_msg")
    private String profileMsg;
    @Column(name="profile_img")
    private String profileImg;
    private String social;
    private int notice;
    @Column(name="background_img")
    private String backgroundImg;

    @Builder
    public UserEntity(String id, String password, String nickname){ //회원 가입 빌더
        this.id = id;
        this.password = password;
        this.nickname = nickname;
    }

}
