package com.gwangjubob.livealone.backend.dto.feed;

import lombok.*;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PopularFollowDto {
    private String follow_id;
    private String follow_nickname;
    private long cnt;
    private byte[] profileImg;

}


