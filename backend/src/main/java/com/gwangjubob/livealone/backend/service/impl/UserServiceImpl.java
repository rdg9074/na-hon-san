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
        Optional<UserEntity> user = userRepository.findById(userLoginDto.getId());
        Boolean passwordCheck = passwordEncoder.matches(userLoginDto.getPassword(),user.get().getPassword());
        if(passwordCheck){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void userDelete(String id){
        userRepository.deleteById(id);
    }

    @Override
    public boolean passwordCheckUser(String id, String password) {
        Optional<UserEntity> user = userRepository.findById(id);
        Boolean passwordCheck = passwordEncoder.matches(password,user.get().getPassword());
        return passwordCheck;
    }


    public boolean registUser(UserRegistDto userRegistDto) {
        String password = passwordEncoder.encode(userRegistDto.getPassword());
        UserEntity user = UserEntity.builder()
                .id(userRegistDto.getId())
                .password(password)
                .nickname(userRegistDto.getNickname())
                .build();
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean checkNickName(String nickname) {
        return userRepository.findByNickname(nickname).isPresent();
    }

    @Override
    public UserInfoDto updateUser(UserInfoDto userInfoDto) {
        Optional<UserEntity> user =  userRepository.findById(userInfoDto.getId());
        UserEntity userGet = user.get();
        if(user != null){

            userGet.setNickname(userInfoDto.getNickname());
            userGet.setArea(userInfoDto.getArea());
            userGet.setFollowOpen(userInfoDto.getFollowOpen());
            userGet.setFollowerOpen(userInfoDto.getFollowerOpen());
            userGet.setProfileImg(userInfoDto.getProfileImg());
            userGet.setProfileMsg(userInfoDto.getProfileMsg());
            userGet.setLikeNotice(userInfoDto.getLikeNotice());
            userGet.setFollowNotice(userInfoDto.getFollowNotice());
            userGet.setCommentNotice(userInfoDto.getCommentNotice());
            userGet.setReplyNotice(userInfoDto.getReplyNotice());
            userGet.setBackgroundImg(userInfoDto.getBackgroundImg());
            userRepository.save(userGet);
            return userInfoDto;
        }
        return null;
    }

    @Override
    public boolean updatePassword(UserLoginDto userLoginDto) {

        Optional<UserEntity> user =  userRepository.findById(userLoginDto.getId());
        String password = passwordEncoder.encode(userLoginDto.getPassword());
        if(user.isPresent()){
            user.get().setPassword(password);
            userRepository.save(user.get());
            return true;
        } else{
            return false;
        }
    }

    @Override
    public void moreUpdate(UserMoreDTO userMoreDTO) {
        Optional<UserEntity> user = userRepository.findById(userMoreDTO.getUserId());
        UserEntity userGet = user.get();
        userGet.setArea(userMoreDTO.getArea());
        userRepository.save(userGet);
        List<UserCategoryEntity> delCategorys = userCategoryRepository.findByUser(userGet);
        for (UserCategoryEntity uc : delCategorys) {
            userCategoryRepository.delete(uc);
        }
        List<String> categorys = userMoreDTO.getCategorys();
        for (String c : categorys) {
            UserCategoryEntity usercategory = UserCategoryEntity.builder()
                    .category(c)
                    .user(userGet)
                    .build();
            userCategoryRepository.save(usercategory);
        }
    }
    public UserInfoDto infoUser(String id) {
        Optional<UserEntity> user = userRepository.findById(id);
        UserEntity userGet = user.get();
        UserInfoDto userInfo = new UserInfoDto();
        userInfo.setId(userGet.getId());
        userInfo.setNickname(userGet.getNickname());
        userInfo.setArea(userGet.getArea());
        userInfo.setFollowOpen(userGet.getFollowOpen());
        userInfo.setFollowerOpen(userGet.getFollowerOpen());
        userInfo.setLikeNotice(userGet.getLikeNotice());
        userInfo.setFollowNotice(userGet.getFollowNotice());
        userInfo.setCommentNotice(userGet.getCommentNotice());
        userInfo.setReplyNotice(userGet.getReplyNotice());
        userInfo.setProfileMsg(userGet.getProfileMsg());
        userInfo.setProfileImg(userGet.getProfileImg());
        userInfo.setBackgroundImg(userGet.getBackgroundImg());
        return userInfo;
    }
}
