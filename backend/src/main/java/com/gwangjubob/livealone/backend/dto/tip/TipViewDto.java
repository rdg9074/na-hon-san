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
    private String category;
    private String title;
    private byte[] bannerImg;

    private Integer likes;
    private Integer comment;
    private Integer view;

    @Override
    public String toString() {
        return "TipViewDto{" +
                "idx=" + idx +
                ", userNickname='" + userNickname + '\'' +
                ", title='" + title + '\'' +
                ", like=" + likes +
                ", comment=" + comment +
                ", view=" + view +
                '}';
    }
}
