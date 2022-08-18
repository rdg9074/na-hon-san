package com.gwangjubob.livealone.backend.service.impl;

import com.gwangjubob.livealone.backend.domain.entity.*;
import com.gwangjubob.livealone.backend.domain.repository.*;
import com.gwangjubob.livealone.backend.dto.Deal.DealDto;
import com.gwangjubob.livealone.backend.dto.feed.FollowViewDto;
import com.gwangjubob.livealone.backend.dto.feed.PopularFollowDto;
import com.gwangjubob.livealone.backend.dto.feed.PostViewDto;
import com.gwangjubob.livealone.backend.dto.feed.ProfileViewDto;
import com.gwangjubob.livealone.backend.dto.tip.TipViewDto;
import com.gwangjubob.livealone.backend.mapper.DealMapper;
import com.gwangjubob.livealone.backend.mapper.UserInfoMapper;
import com.gwangjubob.livealone.backend.service.UserFeedService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class UserFeedServiceImpl implements UserFeedService {
    private UserRepository userRepository;
    private UserCategoryRepository userCategoryRepository;
    private final PasswordEncoder passwordEncoder;
    private UserInfoMapper userInfoMapper;
    private TipRepository tipRepository;
    private DealMapper dealMapper;
    private DealRepository dealRepository;
    private UserFeedRepository userFeedRepository;
    private UserFollowTipsRepository userFollowTipsRepository;
    private NoticeRepository noticeRepository;
    private UserFollowsRepository userFollowsRepository;
    @Autowired
    UserFeedServiceImpl(UserRepository userRepository,UserFollowsRepository userFollowsRepository,NoticeRepository noticeRepository,UserFollowTipsRepository userFollowTipsRepository,DealMapper dealMapper,DealRepository dealRepository,TipRepository tipRepository, UserFeedRepository userFeedRepository, PasswordEncoder passwordEncoder, UserCategoryRepository userCategoryRepository, UserInfoMapper userInfoMapper){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userFollowsRepository = userFollowsRepository;
        this.userFollowTipsRepository = userFollowTipsRepository;
        this.dealMapper = dealMapper;
        this.userCategoryRepository = userCategoryRepository;
        this.userInfoMapper = userInfoMapper;
        this.userFeedRepository = userFeedRepository;
        this.tipRepository = tipRepository;
        this.dealRepository = dealRepository;
        this.noticeRepository = noticeRepository;

    }

    @Override
    public boolean registFollow(String toId, String fromId) {
        Optional<UserEntity> user = userRepository.findById(toId);
        Optional<UserEntity> follow = userRepository.findById(fromId);
        List<UserFollowEntity> isValue = userFeedRepository.findByUserIdAndFollowId(toId, fromId);
        if(follow.isPresent() && user.isPresent() && isValue.isEmpty()){
                UserFollowEntity userFollowEntity = UserFollowEntity.builder()
                .userId(toId)
                .userNickname(user.get().getNickname())
                .followId(fromId).time(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
                .followNickname(follow.get().getNickname())
                .build();
            userFeedRepository.save(userFollowEntity);

            NoticeEntity notice = NoticeEntity.builder()
                    .noticeType("follow")
                    .user(follow.get())
                    .fromUserId(user.get().getId())
                    .time(userFollowEntity.getTime())
                    .build();

            noticeRepository.save(notice);
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
    public ProfileViewDto feedProfile(String id,String decodeId) {
        ProfileViewDto profileViewDto = new ProfileViewDto();
        profileViewDto.setIsFollow(false);
        if(decodeId != null){ // 로그인 한 유저라면
            Optional<UserFollowEntity> res = userFollowsRepository.findByUserIdAndFollowId(decodeId, id);
            if(res.isPresent()){
                profileViewDto.setIsFollow(true);
            }
        }
        Optional<UserEntity> userInfo = userRepository.findById(id);
        int followerCnt = userFeedRepository.countByFollowId(id);
        int followCnt = userFeedRepository.countByUserId(id);
        int tipCount = tipRepository.countByUserId(id);
        int dealCount = dealRepository.countByUserId(id);
        if(userInfo.isPresent()){
            profileViewDto.setId(userInfo.get().getId());
            profileViewDto.setNickname(userInfo.get().getNickname());
            profileViewDto.setProfileImg(userInfo.get().getProfileImg());
            profileViewDto.setProfileMsg(userInfo.get().getProfileMsg());
            profileViewDto.setFollowOpen(userInfo.get().getFollowOpen());
            profileViewDto.setFollowerOpen(userInfo.get().getFollowerOpen());
            profileViewDto.setFollowCount(followCnt);
            profileViewDto.setFollowerCount(followerCnt);
            profileViewDto.setTipCount(tipCount);
            profileViewDto.setDealCount(dealCount);
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
                postViewDto.setLikeCnt(tipEntity.getLike()); // 미구현
                postViewDto.setCommentCnt(tipEntity.getComment()); //미구현
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
                postViewDto.setLikeCnt(dealEntity.getLikes()); // 미구현
                postViewDto.setCommentCnt(dealEntity.getComment()); //미구현
                postViewDtoList.add(postViewDto);
            }
        }
        return postViewDtoList;
    }

    @Override
    @Transactional
    public boolean deleteFollow(String toId, String fromId) {
        List<UserFollowEntity> res = userFeedRepository.findByUserIdAndFollowId(toId, fromId);
        if(!res.isEmpty()){
            userFeedRepository.deleteByUserIdAndFollowId(toId,fromId);

            Optional<NoticeEntity> notice = noticeRepository.findByNoticeTypeAndUserIdAndFromUserId("follow", fromId, toId);
            if(notice.isPresent()){
                noticeRepository.delete(notice.get());
            }
            return true;
        }
        return false;
    }

    @Override
    public List<PopularFollowDto> popularFollower(String decodeId) {
        List<PopularFollowEntity> userFollowEntities = userFeedRepository.popularFollowerList(decodeId,PageRequest.of(0,4));// 인기있는 팔로우 유저
        List<UserFollowEntity> userFollowEntityList= userFollowsRepository.findByUserId(decodeId); // 내가 팔로우 한 유저 목록
        List<PopularFollowDto> popularFollowDtoList = new ArrayList<>();
        //when
        if(!userFollowEntities.isEmpty()){

            for (PopularFollowEntity userFollowEntity : userFollowEntities){

                UserEntity userEntity = userRepository.findById(userFollowEntity.getFollowId()).get();

                PopularFollowDto popularFollowDto = new PopularFollowDto();
                popularFollowDto.setFollow_id(userEntity.getId());
                popularFollowDto.setIsFollow(false);
                if(!userFollowEntityList.isEmpty()){
                    for(UserFollowEntity userFollow : userFollowEntityList){
                        if(userFollow.getFollowId().equals(userFollowEntity.getFollowId())){
                            popularFollowDto.setIsFollow(true);
                        }
                    }
                }
                popularFollowDto.setFollow_nickname(userEntity.getNickname());
                popularFollowDto.setCnt(userFollowEntity.getCnt());
                popularFollowDto.setProfileImg(userEntity.getProfileImg());
                List<TipEntity> tipEntityList = tipRepository.findTop3ByUserIdOrderByIdxDesc(userEntity.getId());
                if(!tipEntityList.isEmpty()){
                    List<TipViewDto> tipViewDtoList = new ArrayList<>();
                    for (TipEntity tipEntity :tipEntityList) { //인기있는 팔로우 유저의 게시글 3개 추가
                        TipViewDto tipViewDto = new TipViewDto();
                        tipViewDto.setIdx(tipEntity.getIdx());
                        tipViewDto.setBannerImg(tipEntity.getBannerImg());
                        tipViewDto.setCategory(tipEntity.getCategory());
                        tipViewDto.setLikes(tipEntity.getLike());
                        tipViewDto.setComment(tipEntity.getComment());
                        tipViewDto.setView(tipEntity.getView());
                        tipViewDtoList.add(tipViewDto);
                    }
                    popularFollowDto.setTipViewDtoList(tipViewDtoList);
                }

                popularFollowDtoList.add(popularFollowDto);

            }
        }
        return popularFollowDtoList;
    }

    @Override
    public List<DealDto> popularHoneyDeal(String decodeId) {
        Optional<UserEntity> user = userRepository.findById(decodeId);
        List<UserCategoryEntity> userCategoryEntityList = userCategoryRepository.findByUser(user.get());//[IT,욕실,가정]


        List<DealEntity> dealEntityList = new ArrayList<>();
        List<DealDto> result = new ArrayList<>();
        List<DealEntity> findTop6 = new ArrayList<>();
        for(UserCategoryEntity userCategoryEntity : userCategoryEntityList){ // 사용자가 선택한 카테고리 목록
            String userArea= null;
            if(user.get().getArea().isEmpty()){ // 전체 조회
                findTop6 = dealRepository.findTop6ByUserNotAndCategoryAndStateOrderByViewDesc(user.get(),userCategoryEntity.getCategory(),"거래 대기");
            }else{ // 지역으로 조회
                findTop6 = dealRepository.findTop6ByUserNotAndCategoryAndStateAndAreaOrderByViewDesc(user.get(),userCategoryEntity.getCategory(),"거래 대기",user.get().getArea().substring(0,3));
            }
            if(!findTop6.isEmpty()){
                for(DealEntity dealEntity : findTop6){
                    dealEntityList.add(dealEntity);
                }
            }
        }
        HashMap<Integer,Boolean> map = new HashMap<>();//
        int cnt = 0;
        while(cnt < dealEntityList.size() && result.size() < 6){
            int rand = (int)(Math.random() * dealEntityList.size()); // 0-17 6개 숫자 중복안됨,
            if(!map.containsKey(rand)){ // 뽑은 숫자가 아니라면 result에
                cnt++;
                map.put(rand,true);
                DealEntity dealEntity = dealEntityList.get(rand);
                DealDto dealDto = new DealDto();
                dealDto.setIdx(dealEntity.getIdx());
                dealDto.setUserNickname(dealEntity.getUser().getNickname());
                dealDto.setUserProfileImg(dealEntity.getUser().getProfileImg());
                dealDto.setTitle(dealEntity.getTitle());
                dealDto.setContent(dealEntity.getContent());
                dealDto.setCategory(dealEntity.getCategory());
                dealDto.setBannerImg(dealEntity.getBannerImg());
                dealDto.setView(dealEntity.getView());
                dealDto.setComment(dealEntity.getComment());
                dealDto.setLikes(dealEntity.getLikes());
                dealDto.setArea(dealEntity.getArea());
                dealDto.setState(dealEntity.getState());
                result.add(dealDto);
            }
        }
        return result;
    }

    @Override
    public Map userFollowHoneyTip(String decodeId, Integer lastIdx, int pageSize) {
        List<TipViewDto> tipViewDtoArrayList = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();
        if(lastIdx == 0){ // null 이면 가장 최신 게시글 찾아줘야함
            lastIdx = userFollowTipsRepository.findTop1ByOrderByIdxDesc().get().getIdx() + 1;
        }
        Pageable pageable = PageRequest.ofSize(pageSize);
        Slice<UserFollowTipsEntity> tipEntityList = userFollowTipsRepository.findTips(decodeId,lastIdx,pageable); //내가 팔로우 한 유저 목록
        for(UserFollowTipsEntity tipEntity : tipEntityList){
            TipViewDto dto = TipViewDto.builder()
                    .idx(tipEntity.getIdx())
                    .userNickname(tipEntity.getUser().getNickname())
                    .userProfileImg(tipEntity.getUser().getProfileImg())
                    .title(tipEntity.getTitle())
                    .category(tipEntity.getCategory())
                    .bannerImg(tipEntity.getBannerImg())
                    .view(tipEntity.getView())
                    .likes(tipEntity.getLike())
                    .comment(tipEntity.getComment())
                    .build();

            tipViewDtoArrayList.add(dto);
        }
        boolean hasNext = tipEntityList.hasNext();
        List<UserFollowEntity> userFollowEntityList= userFollowsRepository.findByUserId(decodeId);
        result.put("follow",userFollowEntityList.size());
        result.put("list", tipViewDtoArrayList);
        result.put("hasNext", hasNext);
        return result;
    }

    @Override
    public boolean checkFollowTip(String decodeId, Integer idx) {
        UserEntity user = userRepository.findById(decodeId).get();
        TipEntity tip = tipRepository.findByIdx(idx).get();

        if(userFeedRepository.findByUserIdAndFollowNickname(decodeId, tip.getUser().getNickname()).isPresent()){
            return true;
        }
        return false;
    }

    @Override
    public boolean checkFollowDeal(String decodeId, Integer idx) {
        UserEntity user = userRepository.findById(decodeId).get();
        DealEntity deal = dealRepository.findByIdx(idx).get();

        if(userFeedRepository.findByUserIdAndFollowNickname(decodeId, deal.getUser().getNickname()).isPresent()){
            return true;
        }
        return false;
    }
}
