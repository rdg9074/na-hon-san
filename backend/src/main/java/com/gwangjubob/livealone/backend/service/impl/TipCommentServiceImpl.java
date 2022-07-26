package com.gwangjubob.livealone.backend.service.impl;

import com.gwangjubob.livealone.backend.domain.entity.TipCommentEntity;
import com.gwangjubob.livealone.backend.domain.entity.TipEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import com.gwangjubob.livealone.backend.domain.repository.TipCommentRepository;
import com.gwangjubob.livealone.backend.domain.repository.TipRepository;
import com.gwangjubob.livealone.backend.domain.repository.UserRepository;
import com.gwangjubob.livealone.backend.dto.tipcomment.TipCommentCreateDto;
import com.gwangjubob.livealone.backend.dto.user.UserInfoDto;
import com.gwangjubob.livealone.backend.service.TipCommentService;
import com.gwangjubob.livealone.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TipCommentServiceImpl implements TipCommentService {

    private UserRepository userRepository;

    private TipCommentRepository tipCommentRepository;
    private TipRepository tipRepository;


    @Autowired
    public TipCommentServiceImpl(UserRepository userRepository, TipCommentRepository tipCommentRepository, TipRepository tipRepository){
        this.userRepository = userRepository;
        this.tipCommentRepository = tipCommentRepository;
        this.tipRepository = tipRepository;
    }
    @Override
    public void createTipComment(String decodeId, TipCommentCreateDto requestDto) {
        UserEntity user = userRepository.findById(decodeId).get();
        TipEntity tip = tipRepository.findByIdx(requestDto.getPostIdx()).get(); // 게시글 정보

        TipCommentEntity entity = TipCommentEntity.builder()
                .user(user)
                .tip(tip)
                .upIdx(requestDto.getUpIdx())
                .content(requestDto.getContent())
                .bannerImg(requestDto.getBannerImg())
                .build();

        tipCommentRepository.save(entity);
    }
}
