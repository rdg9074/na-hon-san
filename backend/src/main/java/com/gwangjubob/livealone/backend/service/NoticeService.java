package com.gwangjubob.livealone.backend.service;

import com.gwangjubob.livealone.backend.domain.entity.NoticeEntity;
import com.gwangjubob.livealone.backend.domain.repository.NoticeRepository;
import com.gwangjubob.livealone.backend.dto.notice.NoticeReadDto;
import com.gwangjubob.livealone.backend.dto.notice.NoticeViewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NoticeService {
    List<NoticeViewDto> viewNotice(String id);

    void deleteNotice(String id, int idx);

    NoticeReadDto readNotice(String decodeId, int idx);
}
