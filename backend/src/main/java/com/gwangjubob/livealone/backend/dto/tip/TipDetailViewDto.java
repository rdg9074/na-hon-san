package com.gwangjubob.livealone.backend.dto.tip;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipDetailViewDto {
    private String userNickname;
    private byte[] userProfileImg;
    private String category;
    private String title;
    private String content;
    private byte[] bannerImg;
    private LocalDateTime time;
    private LocalDateTime updateTime;

    private Integer view;
    private Integer like;
    private Integer comment;
}
