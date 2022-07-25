package com.gwangjubob.livealone.backend.service.impl;

import com.gwangjubob.livealone.backend.domain.entity.UserCategoryEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserFollowEntity;
import com.gwangjubob.livealone.backend.domain.repository.UserCategoryRepository;
import com.gwangjubob.livealone.backend.domain.repository.UserFollowRepository;
import com.gwangjubob.livealone.backend.domain.repository.UserRepository;
import com.gwangjubob.livealone.backend.dto.user.UserInfoDto;
import com.gwangjubob.livealone.backend.dto.user.UserLoginDto;
import com.gwangjubob.livealone.backend.dto.user.UserMoreDTO;
import com.gwangjubob.livealone.backend.dto.user.UserRegistDto;
import com.gwangjubob.livealone.backend.mapper.UserInfoMapper;
import com.gwangjubob.livealone.backend.service.UserFollowService;
import com.gwangjubob.livealone.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserFollowServiceImpl implements UserFollowService {
    private UserRepository userRepository;
    private UserCategoryRepository userCategoryRepository;
    private final PasswordEncoder passwordEncoder;
    private UserInfoMapper userInfoMapper;
    private UserFollowRepository userFollowRepository;
    @Autowired
    UserFollowServiceImpl(UserRepository userRepository,UserFollowRepository userFollowRepository, PasswordEncoder passwordEncoder, UserCategoryRepository userCategoryRepository, UserInfoMapper userInfoMapper){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userCategoryRepository = userCategoryRepository;
        this.userInfoMapper = userInfoMapper;
        this.userFollowRepository = userFollowRepository;
    }

    @Override
    public boolean registFollow(String toId, String fromId) {
        UserFollowEntity userFollowEntity = UserFollowEntity.builder()
                .userId(toId)
                .followId(fromId)
                .build();
        if(!userFollowRepository.findByUserIdAndFollowId(toId,fromId).isPresent()){
            userFollowRepository.save(userFollowEntity);
            return true;
        }
        return false;
    }
    @Override
    @Transactional
    public boolean deleteFollow(String toId, String fromId) {
        if(userFollowRepository.findByUserIdAndFollowId(toId,fromId).isPresent()){
            System.out.println(toId + " : " + fromId);
            userFollowRepository.deleteByUserIdAndFollowId(toId,fromId);
            return true;
        }
        return false;
    }
}
