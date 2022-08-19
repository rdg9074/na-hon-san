package com.gwangjubob.livealone.backend.domain.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@ToString
@Table(name="user_like_posts")
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
public class UserLikeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;
    @Column(name = "post_idx")
    private int postIdx;
    @Column(name = "user_id")
    private String userId;
    private String type;

}
