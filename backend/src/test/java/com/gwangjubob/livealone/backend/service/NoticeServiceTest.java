package com.gwangjubob.livealone.backend.service;

import com.gwangjubob.livealone.backend.domain.entity.NoticeEntity;
import com.gwangjubob.livealone.backend.domain.repository.NoticeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
//@DataJpaTest
public class NoticeServiceTest {
    private NoticeRepository noticeRepository;

    @Autowired
    NoticeServiceTest(NoticeRepository noticeRepository){
        this.noticeRepository = noticeRepository;
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
    }


}
