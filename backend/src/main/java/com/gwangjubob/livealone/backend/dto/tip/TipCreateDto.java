package com.gwangjubob.livealone.backend.dto.tip;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipCreateDto {
    private String userNickname;
    private String category;
    private String title;
    private String content;
    private byte[] bannerImg;
    private Integer view;
}
