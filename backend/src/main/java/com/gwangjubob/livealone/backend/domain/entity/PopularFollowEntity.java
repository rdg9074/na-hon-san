package com.gwangjubob.livealone.backend.domain.entity;


import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Immutable
@Getter
@Setter
@Data
@ToString
@DynamicInsert
@DynamicUpdate
@Table(name = "user_follows")
public class PopularFollowEntity implements Serializable  {
       @Id
    private int idx;
    @Column(name = "follow_id")
    private String followId;
    @Column(name = "follow_nickname")
    private String followNickname;
    private Long cnt;

    public PopularFollowEntity(int idx,   String followId,String followNickname, Long cnt) {
        this.idx = idx;
        this.followId = followId;
        this.followNickname = followNickname;
        this.cnt = cnt;
    }

    public PopularFollowEntity() {

    }

}
