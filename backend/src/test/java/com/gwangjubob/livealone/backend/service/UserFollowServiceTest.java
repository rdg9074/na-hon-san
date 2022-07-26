package com.gwangjubob.livealone.backend.service;

import com.gwangjubob.livealone.backend.domain.entity.DMEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserFollowEntity;
import com.gwangjubob.livealone.backend.domain.repository.DMRepository;
import com.gwangjubob.livealone.backend.domain.repository.MailRepository;
import com.gwangjubob.livealone.backend.domain.repository.UserFollowRepository;
import com.gwangjubob.livealone.backend.domain.repository.UserRepository;
import com.gwangjubob.livealone.backend.dto.dm.DMSendDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class UserFollowServiceTest {
    private DMRepository dmRepository;
    private DMService dmService;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JavaMailSender javaMailSender;
    private MailRepository mailRepository;
    private UserFollowRepository userFollowRepository;

    @Autowired
    UserFollowServiceTest(DMRepository dmRepository, UserFollowRepository userFollowRepository,DMService dmService, JavaMailSender javaMailSender, MailRepository mailRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.dmRepository = dmRepository;
        this.dmService = dmService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.javaMailSender = javaMailSender;
        this.mailRepository = mailRepository;
        this.userFollowRepository = userFollowRepository;
    }

    @Test
    public void 유저_팔로우_테스트() {
        // given
        final UserFollowEntity userFollowEntity = UserFollowEntity.builder()
                .userId("test")
                .followId("ssafy")
                .build();

        // when
        final UserFollowEntity res = userFollowRepository.save(userFollowEntity);

        // then
        Assertions.assertThat(res.getIdx()).isNotNull();
        Assertions.assertThat(res.getUserId()).isEqualTo(userFollowEntity.getUserId());
        Assertions.assertThat(res.getFollowId()).isEqualTo(userFollowEntity.getFollowId());

    }
}
