package com.gwangjubob.livealone.backend.service.impl;

import com.gwangjubob.livealone.backend.domain.entity.DealEntity;
import com.gwangjubob.livealone.backend.domain.entity.TipEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserFollowEntity;
import com.gwangjubob.livealone.backend.domain.repository.*;
import com.gwangjubob.livealone.backend.dto.feed.FollowViewDto;
import com.gwangjubob.livealone.backend.dto.feed.PostViewDto;
import com.gwangjubob.livealone.backend.dto.feed.ProfileViewDto;
import com.gwangjubob.livealone.backend.mapper.UserInfoMapper;
import com.gwangjubob.livealone.backend.service.UserFeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserFeedServiceImpl implements UserFeedService {
    private UserRepository userRepository;
    private UserCategoryRepository userCategoryRepository;
    private final PasswordEncoder passwordEncoder;
    private UserInfoMapper userInfoMapper;
    private TipRepository tipRepository;
    private DealRepository dealRepository;
    private UserFeedRepository userFeedRepository;
    @Autowired
    UserFeedServiceImpl(UserRepository userRepository,DealRepository dealRepository,TipRepository tipRepository, UserFeedRepository userFeedRepository, PasswordEncoder passwordEncoder, UserCategoryRepository userCategoryRepository, UserInfoMapper userInfoMapper){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userCategoryRepository = userCategoryRepository;
        this.userInfoMapper = userInfoMapper;
        this.userFeedRepository = userFeedRepository;
        this.tipRepository = tipRepository;
        this.dealRepository = dealRepository;

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
            userFeedRepository.save(userFollowEntity);
            return true;
        }
        return false;
    }

    @Override
    public List<FollowViewDto> listFollow(String id) {
        List<UserFollowEntity> userFollowEntitys = userFeedRepository.findByUserId(id);
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
        List<UserFollowEntity> userFollowEntitys = userFeedRepository.findByFollowId(id);
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
        List<UserFollowEntity> userFollowEntitys = userFeedRepository.findByUserIdAndFollowNicknameContaining(id,keyword);
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
        List<UserFollowEntity> userFollowEntitys = userFeedRepository.findByFollowIdAndUserNicknameContaining(id,keyword);
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
    public ProfileViewDto feedProfile(String id) {
        ProfileViewDto profileViewDto = new ProfileViewDto();
        Optional<UserEntity> userInfo = userRepository.findById(id);
        int followerCnt = userFeedRepository.countByFollowId(id);
        int followCnt = userFeedRepository.countByUserId(id);
        if(userInfo.isPresent()){
            profileViewDto.setId(userInfo.get().getId());
            profileViewDto.setNickname(userInfo.get().getNickname());
            profileViewDto.setProfileImg(userInfo.get().getProfileImg());
            profileViewDto.setProfileMsg(userInfo.get().getProfileMsg());
            profileViewDto.setFollowCount(followCnt);
            profileViewDto.setFollowerCount(followerCnt);
        }
        return profileViewDto;
    }

    @Override
    public List<PostViewDto> feedPosts(String id, int category) {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        List<PostViewDto> postViewDtoList = new ArrayList<>();
        List<TipEntity> tipEntities = null;
        List<DealEntity> dealEntities = null;
        if (category == 0) { //사용자가 작성한 꿀팁 게시글 조회
            tipEntities = tipRepository.findByUser(userEntity.get());
            for(TipEntity tipEntity : tipEntities){
                PostViewDto postViewDto = new PostViewDto();
                postViewDto.setIdx(tipEntity.getIdx());
                postViewDto.setTitle(tipEntity.getTitle());
                postViewDto.setBannerImg(tipEntity.getBannerImg());
                postViewDto.setViewCnt(tipEntity.getView());
                postViewDto.setLikeCnt(0); // 미구현
                postViewDto.setCommentCnt(0); //미구현
                postViewDtoList.add(postViewDto);
            }

        } else if (category == 1) {//사용자가 작성한 꿀팁 게시글 조회
            dealEntities = dealRepository.findByUser(userEntity.get());
            for(DealEntity dealEntity : dealEntities){
                PostViewDto postViewDto = new PostViewDto();
                postViewDto.setIdx(dealEntity.getIdx());
                postViewDto.setTitle(dealEntity.getTitle());
                postViewDto.setBannerImg(dealEntity.getBannerImg());
                postViewDto.setViewCnt(dealEntity.getView());
                postViewDto.setLikeCnt(0); // 미구현
                postViewDto.setCommentCnt(0); //미구현
                postViewDtoList.add(postViewDto);
            }
        }
        return postViewDtoList;
    }

    @Override
    @Transactional
    public boolean deleteFollow(String toId, String fromId) {
        if(userFeedRepository.findByUserIdAndFollowId(toId,fromId).isPresent()){
            userFeedRepository.deleteByUserIdAndFollowId(toId,fromId);
            return true;
        }
        return false;
    }
}
