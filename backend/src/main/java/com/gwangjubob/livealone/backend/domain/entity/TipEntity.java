package com.gwangjubob.livealone.backend.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
@Table(name="tips")
public class TipEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx; // 게시글 번호

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user; // 사용자 아이디(FK)

    private String category; // 카테고리(꿀팁,꿀템,꿀시피)

    private String title; // 글 제목

    private String content; // 글 내용

    @Column(name = "banner_img")
    private String bannerImg; // 배너 이미지

    private String view; // 조회수

    private LocalDate time; // 글 작성 시간

    @Builder
    public TipEntity(UserEntity user, String category, String title, String content, String bannerImg) {
        this.user = user;
        this.category = category;
        this.title = title;
        this.content = content;
        this.bannerImg = bannerImg;
    }
}
