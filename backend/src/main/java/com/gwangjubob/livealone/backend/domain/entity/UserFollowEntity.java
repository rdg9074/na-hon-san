package com.gwangjubob.livealone.backend.domain.entity;


import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="user_follows")
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    private LocalDateTime time;

}
