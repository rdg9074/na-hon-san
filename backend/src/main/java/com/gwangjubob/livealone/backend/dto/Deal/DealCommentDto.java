package com.gwangjubob.livealone.backend.dto.Deal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DealCommentDto {
    private String userNickname;
    private Integer postIdx;
    private String content;
    private String bannerImg;
}
