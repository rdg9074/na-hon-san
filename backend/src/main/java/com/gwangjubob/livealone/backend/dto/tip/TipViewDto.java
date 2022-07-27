package com.gwangjubob.livealone.backend.dto.tip;

import com.gwangjubob.livealone.backend.domain.entity.TipEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipViewDto {
    private Integer idx;
    private String userNickname;
    private byte[] userProfileImg;
//    private UserEntity user;
    private String title;
    private String bannerImg;

    private Integer likeCnt;
    private Integer commentCnt;
    private Integer viewCnt;
}
