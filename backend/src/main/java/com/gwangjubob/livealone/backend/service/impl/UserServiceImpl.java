package com.gwangjubob.livealone.backend.service.impl;

import com.gwangjubob.livealone.backend.domain.repository.UserRepository;
import com.gwangjubob.livealone.backend.dto.user.UserLoginDto;
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
}
