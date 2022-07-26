package com.gwangjubob.livealone.backend.service.impl;

import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserFollowEntity;
import com.gwangjubob.livealone.backend.domain.repository.UserCategoryRepository;
import com.gwangjubob.livealone.backend.domain.repository.UserFollowRepository;
import com.gwangjubob.livealone.backend.domain.repository.UserRepository;
import com.gwangjubob.livealone.backend.dto.feed.FollowViewDto;
import com.gwangjubob.livealone.backend.mapper.UserInfoMapper;
import com.gwangjubob.livealone.backend.service.UserFollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        Optional<UserEntity> user = userRepository.findById(toId);
        Optional<UserEntity> follow = userRepository.findById(fromId);
        if(follow.isPresent() && user.isPresent()){
                UserFollowEntity userFollowEntity = UserFollowEntity.builder()
                .userId(toId)
                .userNickname(user.get().getNickname())
                .followId(fromId)
                .followNickname(follow.get().getNickname())
                .build();
            userFollowRepository.save(userFollowEntity);
            return true;
        }
        return false;
    }

    @Override
    public List<FollowViewDto> listFollow(String id) {
        List<UserFollowEntity> userFollowEntitys = userFollowRepository.findByUserId(id);
        List<FollowViewDto> res = new ArrayList<>();
        for(UserFollowEntity userFollowEntity : userFollowEntitys){
            Optional<UserEntity> userEntity = userRepository.findById(userFollowEntity.getFollowId()); //아이디로 회원정보 조회하기
            if(userEntity.isPresent()) { // 탈퇴한 회원이 아니라면
                FollowViewDto followViewDto = new FollowViewDto();
                followViewDto.setId(userEntity.get().getId());
                followViewDto.setProfileImg(userEntity.get().getProfileImg());
                followViewDto.setNickname(userEntity.get().getNickname());
                res.add(followViewDto);
            }
        }
        return res;
    }

    @Override
    public List<FollowViewDto> listFollower(String id) {
        List<UserFollowEntity> userFollowEntitys = userFollowRepository.findByFollowId(id);
        List<FollowViewDto> res = new ArrayList<>();
        for(UserFollowEntity userFollowEntity : userFollowEntitys){
            Optional<UserEntity> userEntity = userRepository.findById(userFollowEntity.getUserId()); //아이디로 회원정보 조회하기
            if(userEntity.isPresent()) {  // 탈퇴한 회원이 아니라면
                FollowViewDto followViewDto = new FollowViewDto();
                followViewDto.setId(userEntity.get().getId());
                followViewDto.setProfileImg(userEntity.get().getProfileImg());
                followViewDto.setNickname(userEntity.get().getNickname());
                res.add(followViewDto);
            }
        }
        return res;
    }
    @Override
    public List<FollowViewDto> searchFollow(String id, String keyword) {
        List<UserFollowEntity> userFollowEntitys = userFollowRepository.findByUserIdAndFollowNicknameContaining(id,keyword);
        List<FollowViewDto> res = new ArrayList<>();
        for(UserFollowEntity userFollowEntity : userFollowEntitys){
            Optional<UserEntity> userEntity = userRepository.findById(userFollowEntity.getFollowId()); //아이디로 회원정보 조회하기
            if(userEntity.isPresent()) {  // 탈퇴한 회원이 아니라면
                FollowViewDto followViewDto = new FollowViewDto();
                followViewDto.setId(userEntity.get().getId());
                followViewDto.setProfileImg(userEntity.get().getProfileImg());
                followViewDto.setNickname(userEntity.get().getNickname());
                res.add(followViewDto);
            }
        }
        return res;
    }
    @Override
    public List<FollowViewDto> searchFollower(String id, String keyword) {
        List<UserFollowEntity> userFollowEntitys = userFollowRepository.findByFollowIdAndUserNicknameContaining(id,keyword);
        List<FollowViewDto> res = new ArrayList<>();
        for(UserFollowEntity userFollowEntity : userFollowEntitys){
            Optional<UserEntity> userEntity = userRepository.findById(userFollowEntity.getUserId()); //아이디로 회원정보 조회하기
            if(userEntity.isPresent()) {  // 탈퇴한 회원이 아니라면
                FollowViewDto followViewDto = new FollowViewDto();
                followViewDto.setId(userEntity.get().getId());
                followViewDto.setProfileImg(userEntity.get().getProfileImg());
                followViewDto.setNickname(userEntity.get().getNickname());
                res.add(followViewDto);
            }
        }
        return res;
    }

    @Override
    @Transactional
    public boolean deleteFollow(String toId, String fromId) {
        if(userFollowRepository.findByUserIdAndFollowId(toId,fromId).isPresent()){
            userFollowRepository.deleteByUserIdAndFollowId(toId,fromId);
            return true;
        }
        return false;
    }
}
