package com.gwangjubob.livealone.backend.domain.entity;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Immutable
@Getter
@Setter
@Data
@ToString
@DynamicInsert
@DynamicUpdate
@Table(name = "deal")
public class PopularDealEntity implements Serializable  {
       @Id
    private int idx;
    @Column(name = "follow_id")
    private String followId;
    @Column(name = "follow_nickname")
    private String followNickname;
    private Long cnt;

    public PopularDealEntity(int idx, String followId, String followNickname, Long cnt) {
        this.idx = idx;
        this.followId = followId;
        this.followNickname = followNickname;
        this.cnt = cnt;
    }

    public PopularDealEntity() {

    }

}
