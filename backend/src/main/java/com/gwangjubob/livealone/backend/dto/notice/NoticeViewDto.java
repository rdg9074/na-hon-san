package com.gwangjubob.livealone.backend.dto.notice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeViewDto implements Comparable<NoticeViewDto>{
    private Integer idx;
    private String noticeType;
    private Integer postIdx;
    private String userId;
    private String fromUserId;
    private String fromUserNickname;
    private byte[] fromUserProfileImg;
    private String postType;
    private Boolean read;
    private LocalDateTime time;

    @Override
    public int compareTo(NoticeViewDto o) {
        if(o.getIdx() < idx) return -1;
        else if(o.getIdx() > idx) return 1;
        return 0;
    }
}
