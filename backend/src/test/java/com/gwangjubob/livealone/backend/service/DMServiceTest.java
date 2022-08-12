package com.gwangjubob.livealone.backend.service;

import com.gwangjubob.livealone.backend.domain.entity.DMEntity;
import com.gwangjubob.livealone.backend.domain.entity.MailEntity;
import com.gwangjubob.livealone.backend.domain.entity.NoticeEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import com.gwangjubob.livealone.backend.domain.repository.DMRepository;
import com.gwangjubob.livealone.backend.domain.repository.MailRepository;
import com.gwangjubob.livealone.backend.domain.repository.NoticeRepository;
import com.gwangjubob.livealone.backend.domain.repository.UserRepository;
import com.gwangjubob.livealone.backend.dto.dm.DMSendDto;
import com.gwangjubob.livealone.backend.service.impl.DMServiceImpl;
import com.gwangjubob.livealone.backend.service.impl.MailService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class DMServiceTest {
    private DMRepository dmRepository;
    private DMService dmService;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JavaMailSender javaMailSender;
    private MailRepository mailRepository;

    @Autowired
    DMServiceTest(DMRepository dmRepository, DMService dmService,JavaMailSender javaMailSender,MailRepository mailRepository, UserRepository userRepository,PasswordEncoder passwordEncoder) {
        this.dmRepository = dmRepository;
        this.dmService = dmService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.javaMailSender = javaMailSender;
        this.mailRepository = mailRepository;
    }

    @Test
    public void 메시지_전송_성공_테스트() {
        // given
        final UserEntity fromId = userRepository.findById("test").get();
        final UserEntity toUserId = userRepository.findById("ssafy").get();
        final DMEntity dmEntity = DMEntity.builder()
                .fromUserId(fromId)
                .toUserId(toUserId)
                .content("test2")
                .build();

        // when
        final DMEntity res = dmRepository.save(dmEntity);

        // then
        Assertions.assertThat(res.getIdx()).isNotNull();
        Assertions.assertThat(res.getFromUserId()).isEqualTo(dmEntity.getFromUserId());
        Assertions.assertThat(res.getToUserId()).isEqualTo(dmEntity.getToUserId());
        Assertions.assertThat(res.getContent()).isEqualTo(dmEntity.getContent());
    }
    @Disabled
    @Test
    public void 메시지_전송_없는_사용자_테스트() {
        // given
        final UserEntity fromId = userRepository.findById("test").get();
        final UserEntity toUserId = userRepository.findById("ssafy2").get();
        final DMEntity dmEntity = DMEntity.builder()
                .fromUserId(fromId)
                .toUserId(toUserId)
                .content("test2")
                .build();

        // when
        final DMEntity res = dmRepository.save(dmEntity);

        // then
        Assertions.assertThat(res.getIdx()).isNotNull();
        Assertions.assertThat(res.getFromUserId()).isEqualTo(dmEntity.getFromUserId());
        Assertions.assertThat(res.getToUserId()).isEqualTo(dmEntity.getToUserId());
        Assertions.assertThat(res.getContent()).isEqualTo(dmEntity.getContent());
    }
    @Test
    public void 메시지_전송_없는_메시지_테스트() {
        // given
        final UserEntity fromId = userRepository.findById("test").get();
        final UserEntity toUserId = userRepository.findById("ssafy").get();
        final DMEntity dmEntity = DMEntity.builder()
                .fromUserId(fromId)
                .toUserId(toUserId)
                .build();

        // when
        final DMEntity res = dmRepository.save(dmEntity);

        // then
        Assertions.assertThat(res.getIdx()).isNotNull();
        Assertions.assertThat(res.getFromUserId()).isEqualTo(dmEntity.getFromUserId());
        Assertions.assertThat(res.getToUserId()).isEqualTo(dmEntity.getToUserId());
        Assertions.assertThat(res.getContent()).isEqualTo(dmEntity.getContent());
    }
    @Test
    public void 메시지_리스트_조회_테스트(){
        // given
        final String id = "test";

        UserEntity user = userRepository.findById(id).get();
        // when
        List<UserEntity> toUser = dmRepository.findByfromUserIdGrouptoUserId(user);
        List<UserEntity> fromUser = dmRepository.findBytoUserIdGroupFromUserId(user);
        List<UserEntity> DMUsers = new ArrayList<>();
        // thens
        for (UserEntity d: toUser) {
            if(!DMUsers.contains(d)) {
                DMUsers.add(d);
            }
        }
        for (UserEntity d: fromUser){
            if(!DMUsers.contains(d)) {
                DMUsers.add(d);
            }
        }
        List<DMEntity> dmList = new ArrayList<>();
        for (UserEntity u : DMUsers){
            Optional<DMEntity> optionalDM = dmRepository.findByUserIdAndOtherId(user, u);
            if(optionalDM.isPresent()){
                DMEntity dm = optionalDM.get();
                dmList.add(dm);
            }
        }
        Collections.sort(dmList, (a,b) -> b.getIdx() - a.getIdx());
        for(DMEntity d: dmList){
            System.out.println(d);
        }
    }
    @Test
    public void 메시지_세부_조회_테스트(){
        // given
        UserEntity toId = userRepository.findById("test").get();
        UserEntity fromId = userRepository.findById("ssafy").get();
        Integer lastIdx = 0;
        Pageable pageable = PageRequest.ofSize(5);


        // when
        Slice<DMEntity> dmEntityList = dmRepository.findByToUserIdAndFromUserId(toId, fromId,lastIdx,pageable);

        // thens
//        for (int i = 0; i < dmEntityList.size(); i++) {
//            System.out.println(dmEntityList.get(i).toString());
//        }
    }

    @Test
    @Disabled
    public void 메시지_전송_존재하지않는사용자_테스트() {
        // given
        DMSendDto dmSendDto = new DMSendDto();
        dmSendDto.setFromId("test123");
        dmSendDto.setToId("ssafy");
        dmSendDto.setContent("test");

        // when
        boolean res = dmService.sendDM(dmSendDto);


        // then
        System.out.println(res);
    }

}
