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
import com.gwangjubob.livealone.backend.dto.dm.DMViewDto;
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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

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
        final UserEntity toUserId = userRepository.findById("test11").get();
        final DMEntity dmEntity = DMEntity.builder()
                .fromUserId(fromId)
                .toUserId(toUserId)
                .content("test2")
                .time(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
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
    @Disabled
    public void 메시지_전송_없는_메시지_테스트() {
        // given
        final UserEntity fromId = userRepository.findById("test").get();
        final UserEntity toUserId = userRepository.findById("test11").get();
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
        List<DMViewDto> dmViewDtoList = new ArrayList<>();
        UserEntity toUserEntity = userRepository.findById("test").get();
        UserEntity fromUserEntity = userRepository.findById("test11").get();
        Optional<DMEntity> tmpIdx = dmRepository.findTop1ByOrderByIdxDesc();
        Integer lastIdx = 0;
        Pageable pageable = PageRequest.ofSize(5);

        if(lastIdx == 0 && tmpIdx.isPresent()){ // null 이면 가장 최신 게시글 찾아줘야함
            lastIdx = tmpIdx.get().getIdx() + 1;
        }
        Slice<DMEntity> dmEntityList = dmRepository.findByToUserIdAndFromUserId(toUserEntity,fromUserEntity,lastIdx,pageable);
        Map<String, Object> result = new HashMap<>();
        boolean hasNext = false;

        if(!dmEntityList.isEmpty()){
            hasNext = dmEntityList.hasNext();

            for(DMEntity dmEntity : dmEntityList){

                DMViewDto dmViewDto = new DMViewDto();
                if(dmEntity.getFromUserId().getId().equals("test")){
                    dmViewDto.setType("send");
                }else{
                    dmViewDto.setType("recv");
                    dmEntity.setRead(true);
                    dmRepository.save(dmEntity);
                }
                dmViewDto.setIdx(dmEntity.getIdx());
                dmViewDto.setFromId(dmEntity.getFromUserId().getId());
                dmViewDto.setToId(dmEntity.getToUserId().getId());
                dmViewDto.setTime(dmEntity.getTime());
                dmViewDto.setRead(dmEntity.getRead());
                dmViewDto.setNickname(dmEntity.getFromUserId().getNickname());
                dmViewDto.setContent(dmEntity.getContent());
                dmViewDto.setImage((dmEntity.getImage()));
                dmViewDtoList.add(dmViewDto);
            }
        }
        result.put("list",dmViewDtoList);
        result.put("fromProfileImg",fromUserEntity.getProfileImg());
        result.put("fromNickname", fromUserEntity.getNickname());
        result.put("hasNext",hasNext);
    }

    @Test
    @Disabled
    public void 메시지_전송_존재하지않는사용자_테스트() {
        // given
        DMSendDto dmSendDto = new DMSendDto();
        dmSendDto.setFromId("test123");
        dmSendDto.setToNickname("ssafy");
        dmSendDto.setContent("test");

        // when
        boolean res = dmService.sendDM(dmSendDto);


        // then
        System.out.println(res);
    }

}
