package com.gwangjubob.livealone.backend.domain.entity;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="user_follows")
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
public class UserFollowEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;
    @Column(name="user_id")
    private String userId;
    @Column(name="follow_id")
    private String followId;
    @Column(name="follow_nickname")
    private String followNickname;
    @Column(name="user_nickname")
    private String userNickname;
    @Builder
    public UserFollowEntity(String userId, String followId, String followNickname,String userNickname){ //팔로우 등록 빌더
        this.userId = userId;
        this.followId = followId;
        this.followNickname = followNickname;
        this.userNickname = userNickname;
    }


}
