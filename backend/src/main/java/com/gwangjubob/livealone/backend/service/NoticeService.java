package com.gwangjubob.livealone.backend.service;

import com.gwangjubob.livealone.backend.dto.notice.NoticeViewDto;

import java.util.List;

public interface NoticeService {
    List<NoticeViewDto> viewNotice(String id);
    void deleteNotice(String id, int idx);
    boolean readNotice(String decodeId, int idx);
    long countNotice(String decodeId);
}
