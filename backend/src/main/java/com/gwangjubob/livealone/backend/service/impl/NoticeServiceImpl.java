package com.gwangjubob.livealone.backend.service.impl;

import com.gwangjubob.livealone.backend.domain.entity.NoticeEntity;
import com.gwangjubob.livealone.backend.domain.repository.NoticeRepository;
import com.gwangjubob.livealone.backend.domain.repository.UserRepository;
import com.gwangjubob.livealone.backend.dto.notice.NoticeViewDto;
import com.gwangjubob.livealone.backend.dto.user.UserInfoDto;
import com.gwangjubob.livealone.backend.service.NoticeService;
import com.gwangjubob.livealone.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;


@Service
public class NoticeServiceImpl implements NoticeService {

    private NoticeRepository noticeRepository;
    private UserRepository userRepository;
    private UserService userService;

    @Autowired
    NoticeServiceImpl(NoticeRepository noticeRepository, UserRepository userRepository, UserService userService){
        this.noticeRepository = noticeRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public List<NoticeViewDto> viewNotice(String id) {
        // 1. id로 내정보 조회한다음에
        UserInfoDto user = userService.infoUser(id);

        // 2. 조회한 UserEntity에서 get으로 알림정보 얻어오기

        Map<String, Object> infos = new HashMap<>();
        infos.put("like",user.getLikeNotice());
        infos.put("follow", user.getFollowNotice());
        infos.put("comment", user.getCommentNotice());
        infos.put("reply", user.getReplyNotice());


        List<NoticeViewDto> result = new ArrayList<>();

        for(Map.Entry<String, Object> info : infos.entrySet()){
            // 3. 얻어와서 true인 것만
            if((boolean)info.getValue()){
                // 유저아이디 기준 알림 리스트 전체 조회
                List<NoticeEntity> notices = noticeRepository.findByUserIdAndNoticeType(id, info.getKey());

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
            }
        }


       return result;
    }

    @Override
    public void deleteNotice(String id, int idx) {
        // 삭제하려는 알림글 번호로 알림Entity 조회
        Optional<NoticeEntity> noticeEntity = noticeRepository.findByIdx(idx);
        String tmpId = noticeEntity.get().getUser().getId();

        // 받아온 아이디랑 디코드 아이디 같은지 비교
        if(tmpId.equals(id)) {
            // 같으면 알림 삭제
            noticeRepository.delete(noticeEntity.get());
        }

    }

    @Override
    public boolean readNotice(String decodeId, int idx) {
        Optional<NoticeEntity> noticeEntity = noticeRepository.findByIdx(idx);
        String id = noticeEntity.get().getUser().getId();

        if(id.equals(decodeId) && noticeEntity.isPresent()){
            noticeEntity.get().setRead(true);

            noticeRepository.saveAndFlush(noticeEntity.get());
            return true;
        }
        return false;
    }

}
