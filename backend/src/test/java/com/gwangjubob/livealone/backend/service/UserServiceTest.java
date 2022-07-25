package com.gwangjubob.livealone.backend.service;

import com.gwangjubob.livealone.backend.domain.entity.MailEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserCategoryEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import com.gwangjubob.livealone.backend.domain.repository.MailRepository;
import com.gwangjubob.livealone.backend.domain.repository.UserCategoryRepository;
import com.gwangjubob.livealone.backend.domain.repository.UserRepository;
import com.gwangjubob.livealone.backend.dto.user.UserInfoDto;
import com.gwangjubob.livealone.backend.dto.user.UserLoginDto;
import com.gwangjubob.livealone.backend.mapper.UserInfoMapper;
import org.apache.catalina.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
public class UserServiceTest {
    private UserRepository userRepository;
    private UserCategoryRepository userCategoryRepository;
    private UserInfoMapper userInfoMapper;
    private JavaMailSender javaMailSender;
    private MailRepository mailRepository;

    private static final String okay = "SUCCESS";
    private static final String fail = "FAIL";
    private final PasswordEncoder passwordEncoder;

    @Autowired
    UserServiceTest(UserRepository userRepository,JavaMailSender javaMailSender, MailRepository mailRepository,PasswordEncoder passwordEncoder, UserCategoryRepository userCategoryRepository, UserInfoMapper userInfoMapper){
        this.userRepository = userRepository;
        this.userCategoryRepository = userCategoryRepository;
        this.userInfoMapper = userInfoMapper;
        this.passwordEncoder = passwordEncoder;
        this.javaMailSender = javaMailSender;
        this.mailRepository = mailRepository;
    }

    @Test
    public void 내_정보_조회_테스트(){
        String id = "test";
        //when
        UserEntity user = userRepository.findById(id).get();
        if(user != null) {
            UserInfoDto userInfo = userInfoMapper.toDto(user);
            System.out.println(userInfo);
            System.out.println(okay);
        }
    }


    @Test
    public void 회원_가입(){
        String id = "usertest";
        String nickname = "비밀번호는usertest입니다.";
        String password = "usertest";
        UserEntity inputUser = UserEntity.builder()
                .id(id)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .build();
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            System.out.println(fail);
        } else{
            userRepository.save(inputUser);
            System.out.println(okay);
        }
    }

    @Test
    public void 회원_가입_추가_정보(){
        String id = "test";
        String area = "gwangju";
        List<String> categorys = new ArrayList<>();
        categorys.add("A");
        categorys.add("B");
        categorys.add("C");
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            UserEntity user = optionalUser.get();
            user.setArea(area);
            userRepository.save(user);
            List<UserCategoryEntity> delCategorys = userCategoryRepository.findByUser(user);
            for (UserCategoryEntity uc : delCategorys) {
                userCategoryRepository.delete(uc);
            }
            for (String c : categorys) {
                UserCategoryEntity usercategory = UserCategoryEntity.builder()
                        .category(c)
                        .user(user)
                        .build();
                userCategoryRepository.save(usercategory);
            }
            System.out.println(okay);
        } else{
            System.out.println(fail);
        }
    }

    @Test
    public void 닉네임_중복_체크(){
        String nickname = "nickname";
        Optional<UserEntity> optionalUser = userRepository.findByNickname(nickname);
        if(optionalUser.isPresent()){
            System.out.println(fail);
        } else{
            System.out.println(okay);
        }
    }

    @Test
    public void 회원_정보_수정(){
        String id = "test";
        String nickname = "test";
        String area = "test";
        Boolean followOpen = true;
        Boolean followerOpen = true;
        Boolean likeNotice = true;
        Boolean followNotice = true;
        Boolean commentNotice = true;
        Boolean replyNotice = true;
        String profileMsg = "test";
        String profileImg = "test";
        String backgroundImg = "test";
        UserInfoDto userInfoDto = UserInfoDto.builder()
                .id(id)
                .nickname(nickname)
                .area(area)
                .followOpen(followOpen)
                .followerOpen(followerOpen)
                .likeNotice(likeNotice)
                .followNotice(followNotice)
                .commentNotice(commentNotice)
                .replyNotice(replyNotice)
                .profileMsg(profileMsg)
                .profileImg(profileImg)
                .backgroundImg(backgroundImg)
                .build();
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()){
            UserEntity user = optionalUser.get();
            userInfoMapper.updateFromDto(userInfoDto, user);
            userRepository.save(user);
            userInfoDto = userInfoMapper.toDto(user);
            System.out.println(userInfoDto);
            System.out.println(okay);
        } else{
            System.out.println(fail);
        }
    }
    @Test
    public void 회원_비밀번호_수정(){
        String id = "test";
        String password = "test";
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            UserEntity user = optionalUser.get();
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            System.out.println(okay);
        } else{
            System.out.println(fail);
        }
    }
    @Test
    public void 회원_탈퇴(){
        String id = "test";
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            UserEntity user = optionalUser.get();
            userRepository.delete(user);
            System.out.println(okay);
        } else{
            System.out.println(fail);
        }
    }
    @Test
    public void 비밀번호_인증_테스트(){
        //given
        final String id = "test";
        final String password = "test";

        //when
        Optional<UserEntity> user = userRepository.findById(id);
        boolean res = passwordEncoder.matches(password,user.get().getPassword());

        //thens
        if(res){
            System.out.println("OK");
        }else {
            System.out.println("FAIL");
        }
    }
    @Test
    public void 로그인_테스트(){
        //given
        final String id = "test";
        final String password = "test";

        //when
        Optional<UserEntity> user = userRepository.findById(id);
        boolean res = passwordEncoder.matches(password,user.get().getPassword());

        //thens
        if(res){
            System.out.println("OK");
        }else {
            System.out.println("FAIL");
        }
    }
    @Test
    public void 이메일_전송_테스트(){
        //given
        SimpleMailMessage message = new SimpleMailMessage();
        String authKey = "231456";
        String toId = "1552419@gmail.com";
        String fromId = "gwangjubob@gmail.com";
        String subject = ("[인증테스트] 나 혼자 잘 산다.");
        //when
        message.setTo(toId);
        message.setFrom(fromId);
        message.setSubject(subject);
        message.setText("인증번호 : "+ authKey);
        MailEntity dummyMail = MailEntity.builder()
                .id(toId)
                .type(fromId)
                .number(authKey)
                .build();
        mailRepository.saveAndFlush(dummyMail);
        javaMailSender.send(message); //메일 전송
        MailEntity mail = MailEntity.builder()
                .id(toId)
                .type("0")
                .number(authKey)
                .build();
        mailRepository.saveAndFlush(mail);
        //thens
        System.out.println("OK");
    }
    @Test
    public void 이메일_인증_테스트(){
        //given
        String authKey = "917699";
        String id = "1552419@gmail.com";
        String type = "0";
        //when
        int res = mailRepository.findById(id,authKey, type);
        //thens
        if(res == 1){
        System.out.println("OK");

        }else{
        System.out.println("FAIL");

        }

    }
}

