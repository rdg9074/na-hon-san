package com.gwangjubob.livealone.backend.dto.tip;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipViewDto {
    private Integer idx;
    private String userNickname;
    private byte[] userProfileImg;
    private String title;
    private String bannerImg;

    private Integer likeCnt;
    private Integer commentCnt;
    private Integer viewCnt;
}
