package com.gwangjubob.livealone.backend.service.impl;

import com.gwangjubob.livealone.backend.domain.entity.MailEntity;
import com.gwangjubob.livealone.backend.domain.repository.MailRepository;
import com.gwangjubob.livealone.backend.domain.repository.UserRepository;
import com.gwangjubob.livealone.backend.dto.mail.MailCheckDto;
import com.gwangjubob.livealone.backend.dto.mail.MailSendDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Random;


@Service
@AllArgsConstructor
@EnableAsync
public class MailService {

    private final JavaMailSender javaMailSender;
    private final MailRepository mailRepository;
    private final UserRepository userRepository;
    @Autowired
    public MailService(MailRepository mailRepository,JavaMailSender javaMailSender,UserRepository userRepository){
        this.mailRepository = mailRepository;
        this.userRepository = userRepository;
        this.javaMailSender = javaMailSender;
    }
    private static final String FROM_ADDRESS = "gwangjubob@gmail.com";
    @Async
    public void mailSend(MailSendDto mailSendDto){
        String authKey = makeAuthNumber();
        SimpleMailMessage message = new SimpleMailMessage();
        String subText = "[나 혼자 잘 산다] 인증번호 입니다. \n 인증번호 : " + authKey;
        message.setTo(mailSendDto.getId());
        message.setFrom(MailService.FROM_ADDRESS);
        message.setSubject("[인증번호] 나 혼자 잘 산다");
        message.setText(subText);
        MailEntity dummyMail = MailEntity.builder()
                .id(mailSendDto.getId())
                .type(mailSendDto.getType())
                .number(authKey)
                .time(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
                .build();
        mailRepository.saveAndFlush(dummyMail);
        mailRepository.deleteById(mailSendDto.getId()); //이전에 인증번호 제거
        javaMailSender.send(message); //메일 전송
        MailEntity mail = MailEntity.builder()
                .id(mailSendDto.getId())
                .type(mailSendDto.getType())
                .number(authKey)
                .time(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
                .build();
        mailRepository.saveAndFlush(mail);

    }

    public boolean emailCheck(MailSendDto mailSendDto){
        if((mailSendDto.getType().equals("0") && !userRepository.findById(mailSendDto.getId()).isPresent()) || (mailSendDto.getType().equals("1") && userRepository.findById(mailSendDto.getId()).isPresent())) {
           return true;
        }
        return false;
    }
    public boolean checkAuthNumber(MailCheckDto mailCheckDto){
        if(mailRepository.findById(mailCheckDto.getId(),mailCheckDto.getNumber(), mailCheckDto.getType()) == 1){
            mailRepository.deleteById(mailCheckDto.getId());
            return true;
        }
        return false;

    }
    public String makeAuthNumber(){
        Random random = new Random();
        String authKey = String.valueOf(random.nextInt(888888)+111111);
        return authKey;
    }

}