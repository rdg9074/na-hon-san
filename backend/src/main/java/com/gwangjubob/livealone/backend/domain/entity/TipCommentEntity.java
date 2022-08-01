package com.gwangjubob.livealone.backend.domain.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "tip_comments")
public class TipCommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "post_idx")
    private TipEntity tip;

    @Column(name = "up_idx")
    @ColumnDefault("0") // default 0
    private Integer upIdx;

    @Column(length = 200, nullable = false)
    private String content;

    @Column(name = "banner_img")
    @Lob
    private byte[] bannerImg;

    private LocalDateTime time;

    @Column(name = "update_time")
    private LocalDateTime updateTime;


}
