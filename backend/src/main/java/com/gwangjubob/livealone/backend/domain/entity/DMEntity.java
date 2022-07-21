package com.gwangjubob.livealone.backend.domain.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Table(name="dms")
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
public class DMEntity {
    @Id
    String idx;
    String fromUserId;
    String toUserId;
    String content;
    String image;
    boolean read;
    LocalDateTime time;

    @Builder
    public DMEntity(String fromUserId, String toUserId, String content, String image){ //DM 전송 빌더

    }
}
