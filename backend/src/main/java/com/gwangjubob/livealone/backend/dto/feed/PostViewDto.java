package com.gwangjubob.livealone.backend.dto.feed;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostViewDto {
    private Integer idx;
    private String title;
    private byte[] bannerImg;

    private Integer likeCnt;
    private Integer commentCnt;
    private Integer viewCnt;
}
