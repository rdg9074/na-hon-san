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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public void createTip(String decodeId, TipCreateDto tipCreateDto) {
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

        tipRepository.save(tipEntity);
    }

    @Override
    public List<TipViewDto> viewTip(TipListDto tipListDto) {
        String keyword = tipListDto.getKeyword();
        String category = tipListDto.getCategory();
        String type = tipListDto.getType();
        int pageNum = tipListDto.getPageNum();
        int pageSize = tipListDto.getPageSize();

        Pageable pageable = PageRequest.of(pageNum, pageSize);
        List<TipEntity> tips = null;

        if(keyword == null){
            if(type.equals("조회순")){
                tips = tipRepository.findByCategoryOrderByViewDesc(category,pageable);
            }else if(type.equals("좋아요순")){
                tips = tipRepository.findByCategoryOrderByLikeDesc(category, pageable);
            }else{
                tips = tipRepository.findByCategoryOrderByIdxDesc(category, pageable);
            }
        }else{
            if(type.equals("조회순")){
                tips = tipRepository.findByCategoryAndTitleContainsOrderByViewDesc(category, keyword, pageable);
            }else if(type.equals("좋아요순")){
                tips = tipRepository.findByCategoryAndTitleContainsOrderByLikeDesc(category, keyword, pageable);
            }else{
                tips = tipRepository.findByCategoryAndTitleContainsOrderByIdxDesc(category, keyword, pageable);
            }
        }

        if(tips != null){
            List<TipViewDto> result = new ArrayList<>();
            for(TipEntity t : tips){
                TipViewDto tipViewDto = TipViewDto.builder()
                        .idx(t.getIdx())
                        .category(t.getCategory())
                        .userNickname(t.getUser().getNickname())
                        .userProfileImg(t.getUser().getProfileImg())
                        .title(t.getTitle())
                        .bannerImg(t.getBannerImg())
                        .view(t.getView())
                        .comment(t.getComment())
                        .build();

                result.add(tipViewDto);
            }

            return result;
        }

        return null;
    }

    @Override
    public void updateTip(String decodeId, TipUpdateDto tipUpdateDto, Integer idx) {
        Optional<TipEntity> optionalTip = tipRepository.findByIdx(idx);
        UserEntity user = userRepository.findById(decodeId).get();

        if(optionalTip.isPresent()){
            TipEntity tip = optionalTip.get();
            if(user.getNickname().equals(tip.getUser().getNickname())){
                tipUpdateMapper.updateFromDto(tipUpdateDto, tip);
                tip.setUpdateTime(LocalDateTime.now());
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
                    .time(LocalDateTime.now())
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

}
