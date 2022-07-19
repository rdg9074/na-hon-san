package com.gwangjubob.livealone.backend.service.impl;

import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import com.gwangjubob.livealone.backend.domain.repository.UserRepository;
import com.gwangjubob.livealone.backend.dto.user.UserLoginDto;
import com.gwangjubob.livealone.backend.dto.user.UserRegistDto;
import com.gwangjubob.livealone.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    @Autowired
    UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public boolean loginUser(UserLoginDto userLoginDto){
        return userRepository.findByIdAndPassword(userLoginDto.getId(),userLoginDto.getPassword()).isPresent();
    }

    @Override
    public void userDelete(String id) {
        userRepository.deleteById(id);
    }

    
    public boolean registUser(UserRegistDto userRegistDto) {
        UserEntity user = UserEntity.builder()
                .id(userRegistDto.getId())
                .password(userRegistDto.getPassword())
                .nickname(userRegistDto.getNickname())
                .build();
        userRepository.save(user);
        System.out.println(userRepository.save(user));
        return true;
    }

    @Override
    public boolean checkNickName(String nickname) {
        return userRepository.findByNickname(nickname).isPresent();
    }


}
