package com.gwangjubob.livealone.backend.domain.entity;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Immutable
@Getter
@Setter
@Data
@ToString
@DynamicInsert
@DynamicUpdate
@Table(name = "tips")
public class UserFollowTipsEntity implements Serializable  {

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name="user_nickname", referencedColumnName = "nickname"),
            @JoinColumn(name="user_id", referencedColumnName = "id")
    })
    private UserEntity user;
    @Id
    private Integer idx;
    private String category;
    private String title;
    private String content;
    @Column(name = "banner_img")
    @Lob
    private byte[] bannerImg;

    private Integer view;
    @Column(name = "likes")
    private Integer like;
    private Integer comment;

    private LocalDateTime time;

    @Column(name = "update_time")
    private LocalDateTime updateTime;
//
//    public UserFollowTipsEntity(int idx, String followId, String followNickname, Long cnt) {
//        this.idx = idx;
//        this.followId = followId;
//        this.followNickname = followNickname;
//        this.cnt = cnt;
//    }
//
//    public UserFollowTipsEntity() {
//
//    }

}
