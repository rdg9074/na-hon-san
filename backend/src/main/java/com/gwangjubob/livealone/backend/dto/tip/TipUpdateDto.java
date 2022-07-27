package com.gwangjubob.livealone.backend.dto.tip;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TipUpdateDto {
    private Integer idx;
    private String category;
    private String title;
    private String content;
    private String bannerImg;
}