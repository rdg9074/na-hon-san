package com.gwangjubob.livealone.backend.service.impl;

import com.gwangjubob.livealone.backend.domain.entity.NoticeEntity;
import com.gwangjubob.livealone.backend.domain.entity.TipEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserLikeTipsEntity;
import com.gwangjubob.livealone.backend.domain.repository.NoticeRepository;
import com.gwangjubob.livealone.backend.domain.repository.TipRepository;
import com.gwangjubob.livealone.backend.domain.repository.UserLikeTipsRepository;
import com.gwangjubob.livealone.backend.domain.repository.UserRepository;
import com.gwangjubob.livealone.backend.dto.tip.*;
import com.gwangjubob.livealone.backend.mapper.TipCreateMapper;
import com.gwangjubob.livealone.backend.mapper.TipDetailViewMapper;
import com.gwangjubob.livealone.backend.mapper.TipUpdateMapper;
import com.gwangjubob.livealone.backend.service.TipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class TipServiceImpl implements TipService {
    private TipRepository tipRepository;
    private UserRepository userRepository;
    private NoticeRepository noticeRepository;
    private TipCreateMapper tipCreateMapper;
    private TipUpdateMapper tipUpdateMapper;
    private TipDetailViewMapper tipDetailViewMapper;
    private UserLikeTipsRepository userLikeTipsRepository;

    @Autowired
    public TipServiceImpl(TipRepository tipRepository, NoticeRepository noticeRepository, UserRepository userRepository,
                          TipCreateMapper tipCreateMapper, TipUpdateMapper tipUpdateMapper, TipDetailViewMapper tipDetailViewMapper,
                          UserLikeTipsRepository userLikeTipsRepository){
        this.tipRepository = tipRepository;
        this.userRepository = userRepository;
        this.tipCreateMapper = tipCreateMapper;
        this.tipUpdateMapper = tipUpdateMapper;
        this.tipDetailViewMapper = tipDetailViewMapper;
        this.userLikeTipsRepository = userLikeTipsRepository;
        this.noticeRepository = noticeRepository;
    }
    @Override
    public int createTip(String decodeId, TipCreateDto tipCreateDto) {
        UserEntity user = userRepository.findById(decodeId).get();

        TipCreateDto dto = TipCreateDto.builder()
                .userNickname(user.getNickname())
                .category(tipCreateDto.getCategory())
                .title(tipCreateDto.getTitle())
                .content(tipCreateDto.getContent())
                .bannerImg(tipCreateDto.getBannerImg())
                .build();

        TipEntity tipEntity = tipCreateMapper.toEntity(dto);
        tipEntity.setUser(user);
        tipEntity.setTime(LocalDateTime.now(ZoneId.of("Asia/Seoul")));

        tipRepository.save(tipEntity);

        return tipEntity.getIdx();
    }

    @Override
    public Map viewTip(TipListDto tipListDto) {
        Map<String, Object> result = new HashMap<>();
        String keyword = tipListDto.getKeyword();
        String category = tipListDto.getCategory();
        String type = tipListDto.getType();

        Integer lastIdx = tipListDto.getLastIdx();
        Integer lastView = tipListDto.getLastView();
        Integer lastLike = tipListDto.getLastLike();
        Integer pageSize = tipListDto.getPageSize();

        Slice<TipEntity> tips = null;
        Pageable pageable = PageRequest.ofSize(pageSize);

        if(lastIdx == null){
            lastIdx = tipRepository.findTop1ByOrderByIdxDesc().get().getIdx() + 1;
        }
        if(lastView == null){
            lastView = tipRepository.findTop1ByOrderByViewDesc().get().getView() + 1;
        }
        if(lastLike == null){
            lastLike = tipRepository.findTop1ByOrderByLikeDesc().get().getLike() + 1;
        }

        if(keyword == null) {
            if(category == null){
                if(type.equals("최신순")){
                    tips = tipRepository.findByOrderByIdxDesc(lastIdx, pageable);
                }else if(type.equals("좋아요순")){
                    tips = tipRepository.findByOrderByLikeDescAndIdxDesc(lastLike, lastIdx, pageable);
                }else{
                    tips = tipRepository.findByOrderByViewDescAndIdxDesc(lastView, lastIdx, pageable);
                }
            }else{ // 카테고리 별 조회
                if(type.equals("최신순")){
                    tips = tipRepository.findByCategoryOrderByIdxDesc(category, lastIdx, pageable);
                }else if(type.equals("좋아요순")){
                    tips = tipRepository.findByCategoryOrderByLikeDescAndIdxDesc(category, lastLike, lastIdx, pageable);
                }else{
                    tips = tipRepository.findByCategoryOrderByViewDescAndIdxDesc(category, lastView, lastIdx, pageable);
                }
            }
        }else{ // 검색어 존재
            if(type.equals("최신순")){
                tips = tipRepository.findByCategoryAndTitleContainsOrderByIdxDesc(category, lastIdx, keyword, pageable);
            }else if(type.equals("좋아요순")){
                tips = tipRepository.findByCategoryAndTitleContainsOrderByLikeDescAndIdxDesc(category, lastLike, lastIdx, keyword, pageable);
            }else{
                tips = tipRepository.findByCategoryAndTitleContainsOrderByViewDescAndIdxDesc(category, lastView, lastIdx, keyword, pageable);
            }
        }

        if(tips != null) {
            boolean hasNext = tips.hasNext();
            List<TipViewDto> list = new ArrayList<>();
            for (TipEntity t : tips) {
                TipViewDto tipViewDto = TipViewDto.builder()
                        .idx(t.getIdx())
                        .userNickname(t.getUser().getNickname())
                        .userProfileImg(t.getUser().getProfileImg())
                        .title(t.getTitle())
                        .bannerImg(t.getBannerImg())
                        .category(t.getCategory())
                        .view(t.getView())
                        .likes(t.getLike())
                        .comment(t.getComment())
                        .build();

                list.add(tipViewDto);
            }

            result.put("list", list);
            result.put("hasNext", hasNext);

        }
        return result;
    }

    @Override
    public void updateTip(String decodeId, TipUpdateDto tipUpdateDto, Integer idx) {
        Optional<TipEntity> optionalTip = tipRepository.findByIdx(idx);
        UserEntity user = userRepository.findById(decodeId).get();

        if(optionalTip.isPresent()){
            TipEntity tip = optionalTip.get();
            if(user.getNickname().equals(tip.getUser().getNickname())){
                tipUpdateMapper.updateFromDto(tipUpdateDto, tip);
                tip.setUpdateTime(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
                tipRepository.save(tip);
            }
        }
    }

    @Override
    public void deleteTip(String decodeId, Integer idx) {
        TipEntity tip = tipRepository.findByIdx(idx).get();
        UserEntity user = userRepository.findById(decodeId).get();
        if(user.getNickname().equals(tip.getUser().getNickname())){
            tipRepository.delete(tip);
        }
    }

    @Override
    public TipDetailViewDto detailViewTip(Integer idx) {
        Optional<TipEntity> optionalTipEntity = tipRepository.findByIdx(idx);

        if(optionalTipEntity.isPresent()){
            TipEntity tipEntity = optionalTipEntity.get();
            TipDetailViewDto tipDto = tipDetailViewMapper.toDto(tipEntity);

            tipDto.setUserNickname(tipEntity.getUser().getNickname());
            tipDto.setUserProfileImg(tipEntity.getUser().getProfileImg());

            return tipDto;
        }

        return null;
    }

    @Override
    public void likeTip(String decodeId, Integer idx) {
        UserEntity user = userRepository.findById(decodeId).get();
        TipEntity tip = tipRepository.findByIdx(idx).get();

        Optional<UserLikeTipsEntity> userLikeTipsEntity = userLikeTipsRepository.findByUserAndTip(user, tip);

        if(userLikeTipsEntity.isPresent()){
            UserLikeTipsEntity userLikeTip = userLikeTipsEntity.get();
            userLikeTipsRepository.delete(userLikeTip);

            tip.setLike(tip.getLike() - 1);
            tipRepository.save(tip);

            Optional<NoticeEntity> notice = noticeRepository.findByNoticeTypeAndFromUserIdAndPostTypeAndPostIdx("like", user.getId(), "tip", tip.getIdx());
            if(notice.isPresent()){
                noticeRepository.delete(notice.get());
            }
        }else{
            UserLikeTipsEntity likeTipsEntity = UserLikeTipsEntity.builder()
                    .tip(tip)
                    .user(user)
                    .time(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
                    .build();

            userLikeTipsRepository.save(likeTipsEntity);

            tip.setLike(tip.getLike() + 1);
            tipRepository.save(tip);

            if(!tip.getUser().getId().equals(user.getId())){
                NoticeEntity notice = NoticeEntity.builder()
                        .noticeType("like")
                        .user(tip.getUser())
                        .fromUserId(user.getId())
                        .postType("tip")
                        .postIdx(tip.getIdx())
                        .time(likeTipsEntity.getTime())
                        .build();

                noticeRepository.save(notice);
            }
        }
    }

    @Override
    public boolean countUpView(Integer idx) {
        Optional<TipEntity> optionalTip = tipRepository.findByIdx(idx);
        if(optionalTip.isPresent()){
            TipEntity tip = optionalTip.get();
            tip.setView(tip.getView() + 1);
            tipRepository.save(tip);
            return true;
        }
        return false;
    }

    @Override
    public long getTotalCount() {
        return tipRepository.count();
    }

    @Override
    public boolean clickLikeButton(String decodeId, Integer idx) {
        UserEntity user = userRepository.findById(decodeId).get();
        TipEntity tip = tipRepository.findByIdx(idx).get();

        if(userLikeTipsRepository.findByUserAndTip(user,tip).isPresent()){
            return true;
        }
        return false;
    }

}
