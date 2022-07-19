package com.gwangjubob.livealone.backend.dto.notice;

import com.gwangjubob.livealone.backend.domain.entity.NoticeEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeViewDto {
    private String noticeType;
    private Integer postIdx;
    private String userId;
    private String fromUserNickname;
    private String postType;
    private Boolean read;

    public NoticeViewDto(NoticeEntity noticeEntity) {
    }
}
