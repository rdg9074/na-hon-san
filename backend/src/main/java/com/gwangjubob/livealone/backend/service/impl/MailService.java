package com.gwangjubob.livealone.backend.service.impl;

import com.gwangjubob.livealone.backend.domain.entity.MailEntity;
import com.gwangjubob.livealone.backend.domain.repository.MailRepository;
import com.gwangjubob.livealone.backend.dto.mail.MailCheckDto;
import com.gwangjubob.livealone.backend.dto.mail.MailSendDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Random;


@Service
@AllArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final MailRepository mailRepository;
    @Autowired
    public MailService(MailRepository mailRepository,JavaMailSender javaMailSender){
        this.mailRepository = mailRepository;
        this.javaMailSender = javaMailSender;
    }
    private static final String FROM_ADDRESS = "gwangjubob@gmail.com";

    public boolean sendMail(MailSendDto mailSendDto) {
        String authKey = makeAuthNumber();
        SimpleMailMessage message = new SimpleMailMessage();
        String subText = "회원 가입을 위한 인증번호 입니다. \n 인증번호 : " + authKey;
        message.setTo(mailSendDto.getId());
        message.setFrom(MailService.FROM_ADDRESS);
        message.setSubject("[인증번호] 나 혼자 잘 산다");
        message.setText(subText);

        try{
            MailEntity dummyMail = MailEntity.builder()
                    .id(mailSendDto.getId())
                    .type(mailSendDto.getType())
                    .number(authKey)
                    .build();
            mailRepository.saveAndFlush(dummyMail);
            mailRepository.deleteById(mailSendDto.getId()); //이전에 인증번호 제거
            javaMailSender.send(message); //메일 전송
            MailEntity mail = MailEntity.builder()
                    .id(mailSendDto.getId())
                    .type(mailSendDto.getType())
                    .number(authKey)
                    .build();
            mailRepository.saveAndFlush(mail);
            return true;
        }catch (Exception e){
            return false;
        }
    }
    public boolean checkAuthNumber(MailCheckDto mailCheckDto){
        if(mailRepository.findById(mailCheckDto.getId(),mailCheckDto.getNumber(), mailCheckDto.getType()) == 1){
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