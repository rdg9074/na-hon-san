package com.gwangjubob.livealone.backend.service.impl;

import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import com.gwangjubob.livealone.backend.domain.repository.NoticeRepository;
import com.gwangjubob.livealone.backend.dto.notice.NoticeViewDto;
import com.gwangjubob.livealone.backend.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoticeServiceImpl implements NoticeService {

    private NoticeRepository noticeRepository;
    private UserEntity userEntity;

    @Autowired
    NoticeServiceImpl(NoticeRepository noticeRepository){
        this.noticeRepository = noticeRepository;
    }

    @Override
    public List<NoticeViewDto> viewNotice(String id) {
        return null;
    }

    @Override
    public void deleteNotice(Integer idx) {
        noticeRepository.deleteById(idx);
    }

}
