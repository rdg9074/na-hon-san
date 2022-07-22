package com.gwangjubob.livealone.backend.service;

import com.gwangjubob.livealone.backend.controller.NoticeController;
import com.gwangjubob.livealone.backend.domain.entity.NoticeEntity;
import com.gwangjubob.livealone.backend.domain.repository.NoticeRepository;
import com.gwangjubob.livealone.backend.dto.notice.NoticeViewDto;
import com.gwangjubob.livealone.backend.dto.user.UserInfoDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@SpringBootTest
//@DataJpaTest
public class NoticeServiceTest {
    private NoticeRepository noticeRepository;
    private UserService userService;
    private NoticeService noticeService;

    @Autowired
    NoticeServiceTest(NoticeRepository noticeRepository, UserService userService, NoticeService noticeService){
        this.noticeRepository = noticeRepository;
        this.userService = userService;
        this.noticeService = noticeService;
    }

    @Test
    public void 알림번호로_알림조회_테스트(){
        // given
        String userId = "ssafy";
        boolean read = true;
        String postType = "deal";
        String noticeType = "comment";

        // when
        NoticeEntity notice = noticeRepository.findByIdx(1).get();

        // then
        Assertions.assertThat(notice.getUser().getId()).isEqualTo(userId);
        Assertions.assertThat(notice.getRead()).isEqualTo(read);
        Assertions.assertThat(notice.getPostType()).isEqualTo(postType);
        Assertions.assertThat(notice.getNoticeType()).isEqualTo(noticeType);

        // System.out.println(notice.toString());
    }

    @Test
    public void 읽지_않은_알림개수_조회(){
        // given
        String decodeId = "ssafy";

        // when
        long count = noticeService.countNotice(decodeId);

        // then
        System.out.println(count);
    }

    @Test
    public void 전체_알림_조회() {
        // given
        String testId = "ssafy";
        Map<String, Object> types = new HashMap<>();
        types.put("like", false);
        types.put("follow",true);
        types.put("comment", true);
        types.put("reply", true);

        UserInfoDto user = userService.infoUser(testId);

        // when
        List<NoticeViewDto> result = new ArrayList<>();

        for(Map.Entry<String, Object> info : types.entrySet()){
            if((boolean)info.getValue()){
                List<NoticeEntity> notices = noticeRepository.findByUserIdAndNoticeType(testId, info.getKey());

                for(NoticeEntity n : notices){
                    NoticeViewDto tmp = new NoticeViewDto();
                    tmp.setIdx(n.getIdx());
                    tmp.setNoticeType(n.getNoticeType());
                    tmp.setUserId(n.getUser().getId());
                    tmp.setFromUserId(n.getFromUserId());
                    tmp.setFromUserNickname(user.getNickname());
                    tmp.setPostIdx(n.getPostIdx());
                    tmp.setRead(n.getRead());
                    tmp.setTime(n.getTime());
                    tmp.setPostType(n.getPostType());

                    result.add(tmp);
                }
            }
        }

        // then
        for(NoticeViewDto notice : result){
            System.out.println(notice.toString());
        }
    }
}
