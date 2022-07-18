package com.gwangjubob.livealone.backend.domain.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Data
@ToString
@Table(name="users")
public class UserEntity {

    @Id
    private String id;
    private String password;
    private String nickname;
    private String area;
    @Column(name="follow_open")
    private boolean followOpen;
    @Column(name="follower_open")
    private boolean followerOpen;
    @Column(name="profile_msg")
    private String profileMsg;
    @Column(name="profile_img")
    private String profileImg;
    private String social;
    private int notice;
    @Column(name="background_img")
    private String backgroundImg;


}
