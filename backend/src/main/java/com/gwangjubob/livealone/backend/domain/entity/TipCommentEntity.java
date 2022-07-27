package com.gwangjubob.livealone.backend.domain.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@ToString
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
    private TipEntity tip; // 게시글 번호

    @Column(name = "up_idx")
    @ColumnDefault("0") // default 0
    private Integer upIdx; // 상위 댓글(댓글이면 0, 대댓글이면 댓글의 idx)

    @Column(length = 200, nullable = false)
    private String content;

    @Column(name = "banner_img")
    private String bannerImg;

    private LocalDateTime time; // 댓글 작성 시간

}
