package com.gwangjubob.livealone.backend.domain.entity;

import lombok.*;
import org.apache.catalina.User;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@Setter
//@ToString
@Table(name="users", uniqueConstraints = {@UniqueConstraint( //추가 name = "NAME_AGE_UNIQUE",
        columnNames = {"nickname"} )})
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
public class UserEntity implements Serializable {

    @Id
    private String id;
    private String password;
    @Column(unique = true)
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

    @Column(name="area_x")
    private Double areaX;
    @Column(name="area_y")
    private Double areaY;

    @Lob
    @Column(name="profile_img")
    private byte[] profileImg;
    private String social;

//    @OneToMany(mappedBy = "user")
//    private List<NoticeEntity> notices = new ArrayList<>();

    @Builder
    public UserEntity(String id, String password, String nickname, String social){ //회원 가입 빌더
        this.id = id;
        this.password = password;
        this.nickname = nickname;
        this.social = social;
    }
}
