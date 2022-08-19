package com.gwangjubob.livealone.backend.dto.user;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDto {
    private String id;
    private String nickname;
    private String area;
    private Boolean followOpen;
    private Boolean followerOpen;
    private Boolean likeNotice;
    private Boolean followNotice;
    private Boolean commentNotice;
    private Boolean replyNotice;
    private String profileMsg;
    private String social;
    private byte[] profileImg;
}
