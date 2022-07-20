package com.gwangjubob.livealone.backend.service.impl;

import com.gwangjubob.livealone.backend.domain.entity.NoticeEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import com.gwangjubob.livealone.backend.domain.repository.NoticeRepository;
import com.gwangjubob.livealone.backend.domain.repository.UserRepository;
import com.gwangjubob.livealone.backend.dto.notice.NoticeReadDto;
import com.gwangjubob.livealone.backend.dto.notice.NoticeViewDto;
import com.gwangjubob.livealone.backend.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NoticeServiceImpl implements NoticeService {

    private NoticeRepository noticeRepository;
    private UserRepository userRepository;
    private NoticeEntity noticeEntity;

    @Autowired
    NoticeServiceImpl(NoticeRepository noticeRepository, UserRepository userRepository){
        this.noticeRepository = noticeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<NoticeViewDto> viewNotice(String id) {
        // 1. id로 내정보 조회한다음에 -> userRepository.findById(id) 이러케
        UserEntity user;

        // 2. 조회한 UserEntity에서 get으로 알림정보 얻어오기
        Boolean like_notice;
        Boolean follow_notice;
        Boolean comment_notice;
        Boolean reply_notice;

        // 3. 얻어와서 true인 것만 보여줌...?

        // 유저아이디 기준 알림 리스트 전체 조회
        List<NoticeEntity> notices = noticeRepository.findByUserId(id);
        List<NoticeViewDto> result = new ArrayList<>();

        for(NoticeEntity n : notices){
            NoticeViewDto tmp = new NoticeViewDto();
            tmp.setIdx(n.getIdx());
            tmp.setNoticeType(n.getNoticeType());
            tmp.setUserId(n.getUser().getId());
            tmp.setFromUserId(n.getFromUserId());
            tmp.setPostIdx(n.getPostIdx());
            tmp.setRead(n.getRead());
            tmp.setTime(n.getTime());
            tmp.setPostType(n.getPostType());

            result.add(tmp);
        }
       return result;
    }

    @Override
    public void deleteNotice(String id, int idx) {
        // 삭제하려는 알림글 번호로 알림Entity 조회
        noticeEntity = noticeRepository.findByIdx(idx);
        String tmpId = noticeEntity.getUser().getId();

        // 받아온 아이디랑 디코드 아이디 같은지 비교
        if(tmpId.equals(id)) {
            // 같으면 알림 삭제
            noticeRepository.delete(noticeEntity);
        }

    }

    @Override
    public NoticeReadDto readNotice(String decodeId, int idx) {
        noticeEntity = noticeRepository.findByIdx(idx);
        String id = noticeEntity.getUser().getId();

//        if(id.equals(decodeId)){
//            Optional<NoticeEntity>
//        }
        return null;
    }

}
