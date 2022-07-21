package com.gwangjubob.livealone.backend.domain.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer idx;
    @Column(name="from_user_id")
    String fromUserId;
    @Column(name="to_user_id")
    String toUserId;
    String content;
    String image;
    @Column(name="is_read")
    Boolean read;
    LocalDateTime time;

    @Builder
    public DMEntity(String fromUserId, String toUserId, String content, String image){ //DM 전송 빌더
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.content = content;
        this.image = image;
    }
}
