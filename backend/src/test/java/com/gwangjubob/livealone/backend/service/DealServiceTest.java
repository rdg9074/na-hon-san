package com.gwangjubob.livealone.backend.service;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.gwangjubob.livealone.backend.domain.entity.DealCommentEntity;
import com.gwangjubob.livealone.backend.domain.entity.DealEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import com.gwangjubob.livealone.backend.domain.repository.DealCommentRepository;
import com.gwangjubob.livealone.backend.domain.repository.DealRepository;
import com.gwangjubob.livealone.backend.domain.repository.UserRepository;
import com.gwangjubob.livealone.backend.dto.Deal.DealCommentDto;
import com.gwangjubob.livealone.backend.dto.Deal.DealDto;
import com.gwangjubob.livealone.backend.mapper.DealCommentMapper;
import com.gwangjubob.livealone.backend.mapper.DealMapper;
import jdk.jfr.Category;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SpringBootTest
@Transactional
public class DealServiceTest {
    private DealRepository dealRepository;
    private DealMapper dealMapper;
    private DealCommentMapper dealCommentMapper;
    private UserRepository userRepository;

    private DealCommentRepository dealCommentRepository;
    private static final String okay = "SUCCESS";
    private static final String fail = "FAIL";

    @Autowired
    DealServiceTest(DealRepository dealRepository, DealMapper dealMapper, UserRepository userRepository, DealCommentMapper dealCommentMapper, DealCommentRepository dealCommentRepository){
        this.dealRepository = dealRepository;
        this.dealMapper = dealMapper;
        this.userRepository = userRepository;
        this.dealCommentMapper = dealCommentMapper;
        this.dealCommentRepository = dealCommentRepository;
    }

    @Test
    public void 꿀딜_게시글_작성(){
        Map<String, Object> resultMap = new HashMap<>();
        String userNickname = "비밀번호는 test 입니다.";
        String title = "제목이다.";
        String content = "내용입니다.";
        String category = "주방용품";
        byte[] bannerImg = null;
        String state = "거래중";
        String area = "광주";
        Integer view = 3;
        Optional<UserEntity> optionalUser = userRepository.findByNickname(userNickname);
        if (optionalUser.isPresent()){
            UserEntity user = optionalUser.get();
            DealDto input = DealDto.builder()
                    .userNickname(userNickname)
                    .title(title)
                    .content(content)
                    .category(category)
                    .bannerImg(bannerImg)
                    .state(state)
                    .area(area)
                    .view(view).build();
            DealEntity deal = dealMapper.toEntity(input);
            deal.setUser(user);
            DealEntity dealEntity = dealRepository.save(deal);
            DealDto dealDto = dealMapper.toDto(dealEntity);
            dealDto.setUserNickname(deal.getUser().getNickname());
            resultMap.put("data", dealDto);
            resultMap.put("message", okay);
        } else{
            resultMap.put("message", fail);
        }
        System.out.println(resultMap);
    }

    @Test
    public void 꿀딜_게시글_상세조회(){
        Map<String, Object> resultMap = new HashMap<>();
        Integer idx = 13;
        Optional<DealEntity> optionalDeal = dealRepository.findById(idx);
        if(optionalDeal.isPresent()){
            DealEntity dealEntity = optionalDeal.get();
            DealDto data = dealMapper.toDto(dealEntity);
            data.setUserNickname(dealEntity.getUser().getNickname());
            resultMap.put("data", data);
            resultMap.put("message", okay);
        } else{
            resultMap.put("message", fail);
        }
        System.out.println(resultMap);
    }

    @Test
    public void 꿀딜_게시글_수정(){
        Map<String, Object> resultMap = new HashMap<>();
        Integer idx = 10;
        String title = "update";
        String content = "update";
        String category = "update";
        byte[] bannerImg = null;
        String state = "거래중";
        DealDto dealDto = new DealDto()
                .builder()
                .title(title)
                .content(content)
                .category(category)
                .bannerImg(bannerImg)
                .state(state)
                .build();
        Optional<DealEntity> optionalDeal = dealRepository.findById(idx);
        if(optionalDeal.isPresent()){
            DealEntity dealEntity = optionalDeal.get();
            dealMapper.updateFromDto(dealDto, dealEntity);
            DealEntity deal = dealRepository.save(dealEntity);
            DealDto data = dealMapper.toDto(deal);
            resultMap.put("data", data);
            resultMap.put("message", okay);
        }else{
            resultMap.put("message", fail);
        }
        System.out.println(resultMap);
    }

    @Test
    public void 꿀딜_게시글_삭제(){
        Map<String, Object> resultMap = new HashMap<>();
        Integer idx = 13;
        Optional<DealEntity> optionalDeal = dealRepository.findById(idx);
        if(optionalDeal.isPresent()) {
            DealEntity dealEntity = optionalDeal.get();
            dealRepository.delete(dealEntity);
            resultMap.put("message", okay);
        } else{
            resultMap.put("message", fail);
        }
        System.out.println(resultMap);
    }

    @Test
    public void 꿀딜_댓글_등록(){
        Map<String, Object> resultMap = new HashMap<>();
        Integer postIdx = 10;
        Integer upidx = 0;
        String userNickname = "비밀번호는 test 입니다.";
        String content = "댓글 내용입니다.";
        byte[] bannerImg = null;
        Optional<UserEntity> optionalUser = userRepository.findByNickname(userNickname);
        Optional<DealEntity> optionalDeal = dealRepository.findById(postIdx);
        if (optionalUser.isPresent() && optionalDeal.isPresent()){
            UserEntity user = optionalUser.get();
            DealEntity deal = optionalDeal.get();
            DealCommentDto input = DealCommentDto.builder()
                    .content(content)
                    .bannerImg(bannerImg)
                    .build();
            DealCommentEntity inputEntity = dealCommentMapper.toEntity(input);
            inputEntity.setDeal(deal);
            inputEntity.setUser(user);
            DealCommentEntity dealCommentEntity  = dealCommentRepository.save(inputEntity);
            DealCommentDto data = dealCommentMapper.toDto(dealCommentEntity);
            data.setPostIdx(deal.getIdx());
            data.setUserNickname(user.getNickname());
            resultMap.put("data", data);
            resultMap.put("message", okay);
        } else{
            resultMap.put("message", fail);
        }
        System.out.println(resultMap);
    }

    @Test
    public void 꿀딜_댓글_수정(){
        Map<String, Object> resultMap = new HashMap<>();
        Integer idx = 11;
        String content = "update";
        byte[] bannerImg = null;
        DealCommentDto dealCommentDto = DealCommentDto
                .builder()
                .content(content)
                .bannerImg(bannerImg)
                .build();
        Optional<DealCommentEntity> optionalDealComment = dealCommentRepository.findById(idx);
        if(optionalDealComment.isPresent()){
            DealCommentEntity dealCommentEntity = optionalDealComment.get();
            dealCommentMapper.updateFromDto(dealCommentDto, dealCommentEntity);
            DealCommentEntity updateDeal = dealCommentRepository.save(dealCommentEntity);
            DealCommentDto data = dealCommentMapper.toDto(updateDeal);
            resultMap.put("data", data);
            resultMap.put("message", okay);
        }else{
            resultMap.put("message", fail);
        }
        System.out.println(resultMap);
    }

    @Test
    public void 꿀딜_댓글_삭제(){
        Map<String, Object> resultMap = new HashMap<>();
        Integer idx = 11;
        Optional<DealCommentEntity> optionalDealComment = dealCommentRepository.findById(idx);
        if(optionalDealComment.isPresent()){
            DealCommentEntity dealCommentEntity = optionalDealComment.get();
            dealCommentRepository.delete(dealCommentEntity);
            resultMap.put("message", okay);
        } else{
            resultMap.put("message", fail);
        }
        System.out.println(resultMap);
    }

}
