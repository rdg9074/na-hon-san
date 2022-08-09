package com.gwangjubob.livealone.backend.dto.Deal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DealCommentDto {
    private Integer idx;
    private String userId;
    private String userNickname;
    private byte[] userProfileImg;
    private Integer postIdx;
    private Integer upIdx;
    private String content;
    private byte[] bannerImg;
    LocalDateTime time;
    LocalDateTime updateTime;
}
