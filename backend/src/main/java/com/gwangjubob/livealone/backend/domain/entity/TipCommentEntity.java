package com.gwangjubob.livealone.backend.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
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

    private LocalDate time; // 댓글 작성 시간

    @Builder
    public TipCommentEntity(Integer idx, UserEntity user, TipEntity tip, Integer upIdx, String content, String bannerImg, LocalDate time) {
        this.idx = idx;
        this.user = user;
        this.tip = tip;
        this.upIdx = upIdx;
        this.content = content;
        this.bannerImg = bannerImg;
        this.time = time;
    }

    /* 댓글 수정을 위한 setter */
    public void updateTipComment(String content, String bannerImg){
        this.content = content;
        this.bannerImg = bannerImg;
    }
}
