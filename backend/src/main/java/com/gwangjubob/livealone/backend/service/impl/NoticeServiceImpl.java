package com.gwangjubob.livealone.backend.service.impl;

import com.gwangjubob.livealone.backend.domain.entity.NoticeEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
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
    private UserService userService;

    @Autowired
    NoticeServiceImpl(NoticeRepository noticeRepository, UserService userService){
        this.noticeRepository = noticeRepository;
        this.userService = userService;
    }

    @Override
    public List<NoticeViewDto> viewNotice(String id) {
        UserInfoDto user = userService.infoUser(id);

        Map<String, Object> infos = new HashMap<>();
        infos.put("like",user.getLikeNotice());
        infos.put("follow", user.getFollowNotice());
        infos.put("comment", user.getCommentNotice());
        infos.put("reply", user.getReplyNotice());

        List<NoticeViewDto> result = new ArrayList<>();

        for(Map.Entry<String, Object> info : infos.entrySet()){
            if((boolean)info.getValue()){
                List<NoticeEntity> notices = noticeRepository.findByUserIdAndNoticeType(id, info.getKey());
                if(!notices.isEmpty()){
                    for(NoticeEntity n : notices){
                        NoticeViewDto tmp = new NoticeViewDto();
                        tmp.setIdx(n.getIdx());
                        tmp.setNoticeType(n.getNoticeType());
                        tmp.setUserId(n.getUser().getId());
                        tmp.setFromUserId(n.getFromUserId());
                        tmp.setFromUserNickname(userService.infoUser(n.getFromUserId()).getNickname());
                        tmp.setFromUserProfileImg(userService.infoUser(n.getFromUserId()).getProfileImg());
                        tmp.setPostIdx(n.getPostIdx());
                        tmp.setRead(n.getRead());
                        tmp.setTime(n.getTime());
                        tmp.setPostType(n.getPostType());

                        result.add(tmp);
                    }
                }

            }
        }

        Collections.sort(result);
       return result;
    }

    @Override
    public void deleteNotice(String id, int idx) {
        NoticeEntity notice = noticeRepository.findByIdx(idx).get();
        String tmpId = notice.getUser().getId();

        if(tmpId.equals(id)) {
            noticeRepository.delete(notice);
        }
    }

    @Override
    public boolean readNotice(String decodeId, int idx) {
        NoticeEntity notice = noticeRepository.findByIdx(idx).get();
        String id = notice.getUser().getId();

        if(id.equals(decodeId) && notice != null){
            notice.setRead(true);
            noticeRepository.saveAndFlush(notice);
            return true;
        }
        return false;
    }

    @Override
    public long countNotice(String decodeId) {
        List<NoticeViewDto> list = viewNotice(decodeId);

        long count = 0;
        for(NoticeViewDto n : list){
            if(n.getRead() == false){
                count = count + 1;
            }
        }
        return count;
    }
}
