package com.gwangjubob.livealone.backend.service;

import com.gwangjubob.livealone.backend.domain.repository.NoticeRepository;
import com.gwangjubob.livealone.backend.dto.notice.NoticeViewDto;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface NoticeService {
    List<NoticeViewDto> viewNotice(String id);
    void deleteNotice(Integer idx);
}
