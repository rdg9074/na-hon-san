package com.gwangjubob.livealone.backend.dto.tip;

import com.gwangjubob.livealone.backend.domain.entity.TipEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
public class TipCreateDto {

    private UserEntity user; // 글 작성자 정보
    private String category; // 해당 글 카테고리
    private String title; // 글 제목
    private String content; // 글 내용
    private String bannerImg; // 배너 이미지 경로

    @Builder
    public TipCreateDto(UserEntity user, String category, String title, String content, String bannerImg) {
        this.user = user;
        this.category = category;
        this.title = title;
        this.content = content;
        this.bannerImg = bannerImg;
    }

    public TipEntity toEntity(){
        return TipEntity.builder()
                .user(user)
                .category(category)
                .title(title)
                .content(content)
                .bannerImg(bannerImg)
                .build();
    }
}
