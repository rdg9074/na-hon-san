package com.gwangjubob.livealone.backend.dto.feed;

import com.gwangjubob.livealone.backend.dto.tip.TipViewDto;
import lombok.*;

import javax.persistence.Column;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PopularFollowDto {
    private String follow_id;
    private Boolean isFollow;
    private String follow_nickname;
    private long cnt;
    private byte[] profileImg;
    private List<TipViewDto> tipViewDtoList;

}


