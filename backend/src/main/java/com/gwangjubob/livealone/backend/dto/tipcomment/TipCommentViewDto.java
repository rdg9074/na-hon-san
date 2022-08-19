package com.gwangjubob.livealone.backend.dto.tipcomment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class TipCommentViewDto {
    private Integer idx;
    private Integer upIdx;
    private String userNickname;
    private byte[] userProfileImg;

    private String content;
    private byte[] bannerImg;
    private LocalDateTime time;
    private LocalDateTime updateTime;
}
