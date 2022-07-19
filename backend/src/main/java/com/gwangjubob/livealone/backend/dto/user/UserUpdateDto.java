package com.gwangjubob.livealone.backend.dto.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {
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
    private String profileImg;
    private String backgroundImg;
}
