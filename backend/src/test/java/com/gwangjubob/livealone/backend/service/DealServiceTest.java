package com.gwangjubob.livealone.backend.service;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.gwangjubob.livealone.backend.domain.entity.*;
import com.gwangjubob.livealone.backend.domain.repository.*;
import com.gwangjubob.livealone.backend.dto.Deal.DealCommentDto;
import com.gwangjubob.livealone.backend.dto.Deal.DealDto;
import com.gwangjubob.livealone.backend.mapper.DealCommentMapper;
import com.gwangjubob.livealone.backend.mapper.DealMapper;
import jdk.jfr.Category;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.*;

@SpringBootTest
@Transactional
public class DealServiceTest {
    private DealRepository dealRepository;
    private DealMapper dealMapper;
    private DealCommentMapper dealCommentMapper;
    private UserRepository userRepository;

    private DealCommentRepository dealCommentRepository;
    private UserLikeDealsRepository userLikeDealsRepository;
    private NoticeRepository noticeRepository;
    private static final String okay = "SUCCESS";
    private static final String fail = "FAIL";

    @Autowired
    DealServiceTest(DealRepository dealRepository, DealMapper dealMapper, UserRepository userRepository, DealCommentMapper dealCommentMapper,
                    DealCommentRepository dealCommentRepository, UserLikeDealsRepository userLikeDealsRepository, NoticeRepository noticeRepository){
        this.dealRepository = dealRepository;
        this.dealMapper = dealMapper;
        this.userRepository = userRepository;
        this.dealCommentMapper = dealCommentMapper;
        this.dealCommentRepository = dealCommentRepository;
        this.userLikeDealsRepository = userLikeDealsRepository;
        this.noticeRepository = noticeRepository;
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

            // 해당 게시글 관련 모든 알림 삭제
            List<NoticeEntity> noticeEntityList = noticeRepository.findAllByPostIdxAndPostType(idx, "deal");
            if(!noticeEntityList.isEmpty()){
                noticeRepository.deleteAllInBatch(noticeEntityList);
            }
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
        Integer postIdx = 43;
        Integer idx = 11;
        Optional<DealCommentEntity> optionalDealComment = dealCommentRepository.findById(idx);
        DealEntity deal = dealRepository.findByIdx(postIdx).get();

        if(optionalDealComment.isPresent()){
            DealCommentEntity dealCommentEntity = optionalDealComment.get();

            // 대댓글이라면
            if(dealCommentEntity.getUpIdx() != 0 ){
                deal.setComment(deal.getComment() - 1);
                dealRepository.save(deal);

                dealCommentRepository.delete(dealCommentEntity);

                // 알림이 있다면 알림도 삭제
                NoticeEntity noticeEntity = noticeRepository.findByNoticeTypeAndFromUserIdAndPostTypeAndPostIdx("reply", dealCommentEntity.getUser().getId(), "deal", postIdx);
                if(noticeEntity != null){
                    noticeRepository.delete(noticeEntity);
                }
            }else{ // 댓글이라면 관련된 대댓글들 모두 삭제
                List<DealCommentEntity> replyCommenyList = dealCommentRepository.findByUpIdx(idx);
                int size = replyCommenyList.size();

                if(!replyCommenyList.isEmpty()){
                    deal.setComment(deal.getComment() - size - 1);
                    dealRepository.save(deal);

                    dealCommentRepository.deleteAllInBatch(replyCommenyList);

                    // 대댓글관련 알림까지 삭제
                    List<NoticeEntity> noticeEntityList = noticeRepository.findAllByNoticeTypeAndFromUserIdAndPostTypeAndPostIdx("reply",dealCommentEntity.getUser().getId(), "deal", postIdx);
                    if(!noticeEntityList.isEmpty()){
                        noticeRepository.deleteAllInBatch(noticeEntityList);
                    }
                }

                // 댓글 알림 삭제
                NoticeEntity noticeEntity = noticeRepository.findByNoticeTypeAndFromUserIdAndPostTypeAndPostIdx("comment",dealCommentEntity.getUser().getId(),"deal",postIdx);
                if(noticeEntity != null){
                    noticeRepository.delete(noticeEntity);
                }
            }

            dealCommentRepository.delete(dealCommentEntity);
            resultMap.put("message", okay);
        } else{
            resultMap.put("message", fail);
        }
        System.out.println(resultMap);
    }

    @Test
    public void 꿀딜_게시글_좋아요(){
        Map<String, Object> resultMap = new HashMap<>();
        Integer postIdx = 49;
        String userId = "ssafy";
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        Optional<DealEntity> optionalDeal = dealRepository.findById(postIdx);
        if(optionalUser.isPresent() && optionalDeal.isPresent()){
            UserEntity user = optionalUser.get();
            DealEntity deal = optionalDeal.get();
            Optional<UserLikeDealsEntity> optionalUserLikeDeals = userLikeDealsRepository.findByDealAndUser(deal, user);
            if(optionalUserLikeDeals.isPresent()){
                UserLikeDealsEntity userLikeDeals = optionalUserLikeDeals.get();
                userLikeDealsRepository.delete(userLikeDeals);
                deal.setLikes(deal.getLikes() - 1);
                dealRepository.save(deal);
                resultMap.put("message", okay);
                resultMap.put("data", "좋아요 취소");

                // 좋아요 취소 누르면 알림까지 삭제
                NoticeEntity noticeEntity = noticeRepository.findByNoticeTypeAndFromUserIdAndPostTypeAndPostIdx("like",userId,"deal",postIdx);
                if(noticeEntity != null){
                    noticeRepository.delete(noticeEntity);
                }
            } else{
                UserLikeDealsEntity userLikeDeals = UserLikeDealsEntity
                        .builder()
                        .deal(deal)
                        .user(user)
                        .build();
               userLikeDealsRepository.save(userLikeDeals);
               deal.setLikes(deal.getLikes() + 1);
               dealRepository.save(deal);
               resultMap.put("message", "좋아요");

               // 알림 등록
                NoticeEntity noticeEntity = NoticeEntity.builder()
                        .noticeType("like")
                        .user(deal.getUser())
                        .fromUserId(userId)
                        .postType("deal")
                        .postIdx(deal.getIdx())
                        .time(userLikeDeals.getTime())
                        .build();

                noticeRepository.save(noticeEntity);
            }
        } else{
            resultMap.put("message", fail);
        }
        System.out.println(resultMap);
    }

    @Test
    public void 꿀딜_게시글_조회(){
        Map<String, Object> resultMap = new HashMap<>();
        String keyword = null;
        String state = "거래 대기"; //거래중, 거래 대기, 거래 완료
        List<String> categorys = new ArrayList<>(); //"전체", "의류","식품","주방용품","생활용품","홈인테리어","가전디지털","취미용품","기타"
        categorys.add("전체");
        categorys.add("식품");
        String type = "좋아요순"; //조회순, 좋아요순, 최신순
        List<DealEntity> deals = null;
        Pageable pageable = PageRequest.of(1, 6);
        if(categorys.contains("전체")){
            if(keyword == null){
                if (type.equals("조회순")){
                    deals = dealRepository.findByStateOrderByViewDesc(state, pageable);
                } else if (type.equals("좋아요순")){
                    deals = dealRepository.findByStateOrderByLikesDesc(state, pageable);
                } else{
                    deals = dealRepository.findByStateOrderByIdxDesc(state, pageable);
                }
            } else{
                if (type.equals("조회순")){
                    deals = dealRepository.findByStateAndTitleContainsOrderByViewDesc(state, keyword, pageable);
                } else if (type.equals("좋아요순")){
                    deals = dealRepository.findByStateAndTitleContainsOrderByLikesDesc(state, keyword, pageable);
                } else{
                    deals = dealRepository.findByStateAndTitleContainsOrderByIdxDesc(state, keyword, pageable);
                }
            }
        } else{
            if(keyword == null){
                if(type.equals("조회순")){
                    deals = dealRepository.findByStateAndCategoryInOrderByViewDesc(state, categorys, pageable);
                } else if (type.equals("좋아요순")){
                    deals = dealRepository.findByStateAndCategoryInOrderByLikesDesc(state, categorys, pageable);
                } else{
                    deals = dealRepository.findByStateAndCategoryInOrderByIdxDesc(state, categorys, pageable);
                }
            } else{
                if(type.equals("조회순")){
                    deals = dealRepository.findByStateAndCategoryInAndTitleContainsOrderByViewDesc(state, categorys, keyword, pageable);
                } else if (type.equals("좋아요순")){
                    deals = dealRepository.findByStateAndCategoryInAndTitleContainsOrderByLikesDesc(state, categorys, keyword, pageable);
                } else{
                    deals = dealRepository.findByStateAndCategoryInAndTitleContainsOrderByIdxDesc(state, categorys,keyword, pageable);
                }
            }
        }
        if(deals != null){
            List<DealDto> result = dealMapper.toDtoList(deals);
            resultMap.put("message", okay);
            resultMap.put("data", result);
        } else{
            resultMap.put("message", fail);
        }
        System.out.println(resultMap);
    }
}
