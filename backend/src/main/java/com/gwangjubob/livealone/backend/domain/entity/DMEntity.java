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
    @ManyToOne
    @JoinColumn(name="from_user_id")
    private UserEntity fromUserId;
    @ManyToOne
    @JoinColumn(name="to_user_id")
    private UserEntity toUserId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer idx;
    String content;
    @Lob
    byte[] image;
    @Column(name="is_read")
    Boolean read;
    LocalDateTime time;
    @Builder
    public DMEntity(UserEntity fromUserId, UserEntity toUserId, String content, byte[] image, LocalDateTime time){ //DM 전송 빌더
        this.content = content;
        this.image = image;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.time = time;
    }
}
