package com.gwangjubob.livealone.backend.dto.Deal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DealViewDto {
    private Integer idx;
    private String userNickname;
    private byte[] userProfileImg;
    private String category;
    private String title;
    private String state;
    private String area;
    private byte[] bannerImg;
    private Integer likes;
    private Integer comment;
    private Integer view;
}
