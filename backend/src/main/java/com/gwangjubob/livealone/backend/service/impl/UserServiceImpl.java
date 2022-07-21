package com.gwangjubob.livealone.backend.service.impl;

import com.gwangjubob.livealone.backend.domain.entity.NoticeEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserCategoryEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import com.gwangjubob.livealone.backend.domain.repository.UserCategoryRepository;
import com.gwangjubob.livealone.backend.domain.repository.UserRepository;
import com.gwangjubob.livealone.backend.dto.user.UserLoginDto;
import com.gwangjubob.livealone.backend.dto.user.UserMoreDTO;
import com.gwangjubob.livealone.backend.dto.user.UserRegistDto;
import com.gwangjubob.livealone.backend.service.JwtService;
import com.gwangjubob.livealone.backend.dto.user.UserInfoDto;
import com.gwangjubob.livealone.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private UserCategoryRepository userCategoryRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserCategoryRepository userCategoryRepository){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userCategoryRepository = userCategoryRepository;
    }
    @Override
    public boolean loginUser(UserLoginDto userLoginDto){
        UserEntity user = userRepository.findById(userLoginDto.getId()).get();
        if (user !=null){
            Boolean passwordCheck = passwordEncoder.matches(userLoginDto.getPassword(),user.getPassword());
            if(passwordCheck){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }

    @Override
    public void userDelete(String id){
        userRepository.deleteById(id);
    }

    @Override
    public boolean passwordCheckUser(String id, String password) {
        UserEntity user = userRepository.findById(id).get();
        if (user !=null){
            Boolean passwordCheck = passwordEncoder.matches(password,user.getPassword());
            return passwordCheck;
        }
        return false;
    }


    public void registUser(UserRegistDto userRegistDto) {
        String password = passwordEncoder.encode(userRegistDto.getPassword());
        UserEntity user = UserEntity.builder()
                .id(userRegistDto.getId())
                .password(password)
                .nickname(userRegistDto.getNickname())
                .build();
        userRepository.save(user);
    }

    @Override
    public boolean checkNickName(String nickname) {
        return userRepository.findByNickname(nickname).isPresent();
    }

    @Override
    public UserInfoDto updateUser(UserInfoDto userInfoDto) {
        UserEntity user =  userRepository.findById(userInfoDto.getId()).get();
        if(user != null){
            user.setNickname(userInfoDto.getNickname());
            user.setArea(userInfoDto.getArea());
            user.setFollowOpen(userInfoDto.getFollowOpen());
            user.setFollowerOpen(userInfoDto.getFollowerOpen());
            user.setProfileImg(userInfoDto.getProfileImg());
            user.setProfileMsg(userInfoDto.getProfileMsg());
            user.setLikeNotice(userInfoDto.getLikeNotice());
            user.setFollowNotice(userInfoDto.getFollowNotice());
            user.setCommentNotice(userInfoDto.getCommentNotice());
            user.setReplyNotice(userInfoDto.getReplyNotice());
            user.setBackgroundImg(userInfoDto.getBackgroundImg());
            userRepository.save(user);
            return userInfoDto;
        }
        return null;
    }

    @Override
    public boolean updatePassword(UserLoginDto userLoginDto) {
        UserEntity user =  userRepository.findById(userLoginDto.getId()).get();
        String password = passwordEncoder.encode(userLoginDto.getPassword());
        if(user != null){
            user.setPassword(password);
            userRepository.save(user);
            return true;
        } else{
            return false;
        }
    }

    @Override
    public void moreUpdate(UserMoreDTO userMoreDTO) {
        UserEntity user = userRepository.findById(userMoreDTO.getUserId()).get();
        if(user != null){
            user.setArea(userMoreDTO.getArea());
            userRepository.save(user);
            List<UserCategoryEntity> delCategorys = userCategoryRepository.findByUser(user);
            for (UserCategoryEntity uc : delCategorys) {
                userCategoryRepository.delete(uc);
            }
            List<String> categorys = userMoreDTO.getCategorys();
            for (String c : categorys) {
                UserCategoryEntity usercategory = UserCategoryEntity.builder()
                        .category(c)
                        .user(user)
                        .build();
                userCategoryRepository.save(usercategory);
            }
        }
    }
    public UserInfoDto infoUser(String id) {
        UserEntity user = userRepository.findById(id).get();
        UserInfoDto userInfo = new UserInfoDto();
        userInfo.setId(user.getId());
        userInfo.setNickname(user.getNickname());
        userInfo.setArea(user.getArea());
        userInfo.setFollowOpen(user.getFollowOpen());
        userInfo.setFollowerOpen(user.getFollowerOpen());
        userInfo.setLikeNotice(user.getLikeNotice());
        userInfo.setFollowNotice(user.getFollowNotice());
        userInfo.setCommentNotice(user.getCommentNotice());
        userInfo.setReplyNotice(user.getReplyNotice());
        userInfo.setProfileMsg(user.getProfileMsg());
        userInfo.setProfileImg(user.getProfileImg());
        userInfo.setBackgroundImg(user.getBackgroundImg());
        return userInfo;
    }
}
