package com.gwangjubob.livealone.backend.domain.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;


@Entity
@Getter
@Setter
@Table(name = "user_category")
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@AllArgsConstructor
public class UserCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String category;

    @Builder
    public UserCategoryEntity(UserEntity user, String category){
        this.user = user;
        this.category = category;
    }
}
