package com.gwangjubob.livealone.backend.service.impl;

import com.gwangjubob.livealone.backend.domain.entity.NoticeEntity;
import com.gwangjubob.livealone.backend.domain.entity.TipCommentEntity;
import com.gwangjubob.livealone.backend.domain.entity.TipEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import com.gwangjubob.livealone.backend.domain.repository.NoticeRepository;
import com.gwangjubob.livealone.backend.domain.repository.TipCommentRepository;
import com.gwangjubob.livealone.backend.domain.repository.TipRepository;
import com.gwangjubob.livealone.backend.domain.repository.UserRepository;
import com.gwangjubob.livealone.backend.dto.tipcomment.TipCommentCreateDto;
import com.gwangjubob.livealone.backend.dto.tipcomment.TipCommentUpdateDto;
import com.gwangjubob.livealone.backend.dto.tipcomment.TipCommentViewDto;
import com.gwangjubob.livealone.backend.service.TipCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.OpAnd;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TipCommentServiceImpl implements TipCommentService {

    private UserRepository userRepository;
    private TipCommentRepository tipCommentRepository;
    private TipRepository tipRepository;
    private NoticeRepository noticeRepository;


    @Autowired
    public TipCommentServiceImpl(UserRepository userRepository, TipCommentRepository tipCommentRepository,
                                 NoticeRepository noticeRepository, TipRepository tipRepository){
        this.userRepository = userRepository;
        this.tipCommentRepository = tipCommentRepository;
        this.tipRepository = tipRepository;
        this.noticeRepository = noticeRepository;
    }
    @Override
    public void createTipComment(String decodeId, TipCommentCreateDto requestDto) {
        UserEntity user = userRepository.findById(decodeId).get();
        TipEntity tip = tipRepository.findByIdx(requestDto.getPostIdx()).get(); // 게시글 정보

        TipCommentEntity tipComment = TipCommentEntity.builder()
                .user(user)
                .tip(tip)
                .upIdx(requestDto.getUpIdx())
                .content(requestDto.getContent())
                .bannerImg(requestDto.getBannerImg())
                .time(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
                .build();

        tipCommentRepository.save(tipComment);

        tip.setComment(tip.getComment() + 1);
        tipRepository.save(tip);

        if(tipComment.getUpIdx() == 0 && !tip.getUser().getId().equals(user.getId())){ // 댓글 알림 등록
            NoticeEntity notice = NoticeEntity.builder()
                    .noticeType("comment")
                    .user(tip.getUser()) // 글작성자
                    .fromUserId(user.getId())
                    .postType("tip")
                    .commentIdx(tipComment.getIdx())
                    .postIdx(tip.getIdx())
                    .time(tipComment.getTime())
                    .build();

            noticeRepository.save(notice);
        }

        if(tipComment.getUpIdx() != 0){
            TipCommentEntity upTipComment = tipCommentRepository.findByIdx(tipComment.getUpIdx()).get();
            if(!upTipComment.getUser().getId().equals(user.getId())){
                NoticeEntity notice = NoticeEntity.builder()
                        .noticeType("reply")
                        .user(upTipComment.getUser()) // 댓글작성자
                        .fromUserId(user.getId())
                        .postType("tip")
                        .commentIdx(tipComment.getIdx())
                        .commentUpIdx(tipComment.getUpIdx())
                        .time(tipComment.getTime())
                        .postIdx(tip.getIdx())
                        .build();

                noticeRepository.save(notice);
            }
        }
    }

    @Override
    public void updateTipComment(String decodeId, Integer idx, TipCommentUpdateDto requestDto) {

        Optional<TipCommentEntity> optionalTipComment = tipCommentRepository.findByIdx(idx);

        if(optionalTipComment.isPresent()){
            TipCommentEntity tipComment = optionalTipComment.get();
            UserEntity user = userRepository.findById(decodeId).get();
            TipEntity tip = tipRepository.findByIdx(tipComment.getTip().getIdx()).get(); // 게시글 정보

            TipCommentEntity updateTipComment = TipCommentEntity.builder()
                    .idx(idx)
                    .user(user)
                    .tip(tip)
                    .upIdx(tipComment.getUpIdx())
                    .content(requestDto.getContent())
                    .bannerImg(requestDto.getBannerImg())
                    .time(tipComment.getTime())
                    .updateTime(LocalDateTime.now(ZoneId.of("Asia/Seoul")))

                    .build();

            tipCommentRepository.saveAndFlush(updateTipComment);
        }

    }

    @Override
    public List<TipCommentViewDto> viewTipComment(Integer idx) {
        TipEntity tipEntity = tipRepository.findByIdx(idx).get();
        List<TipCommentEntity> tipCommentEntity = tipCommentRepository.findByTipOrderBycomment(tipEntity);
        List<TipCommentViewDto> result = new ArrayList<>();

        for(TipCommentEntity t : tipCommentEntity){
            TipCommentViewDto dto = TipCommentViewDto.builder()
                    .idx(t.getIdx())
                    .upIdx(t.getUpIdx())
                    .userProfileImg(t.getUser().getProfileImg())
                    .userNickname(t.getUser().getNickname())
                    .content(t.getContent())
                    .bannerImg(t.getBannerImg())
                    .time(t.getTime())
                    .updateTime(t.getUpdateTime())
                    .build();

            result.add(dto);
        }

        return result;
    }

    @Override
    public void deleteTipComment(String decodeId, Integer idx) {
        UserEntity user = userRepository.findById(decodeId).get();
        Optional<TipCommentEntity> optionalTipCommentEntity = tipCommentRepository.findByIdx(idx);

        if(optionalTipCommentEntity.isPresent()){
            TipCommentEntity tipComment = optionalTipCommentEntity.get();
            TipEntity tip = tipRepository.findByIdx(tipComment.getTip().getIdx()).get();

            if(user.getNickname().equals(tipComment.getUser().getNickname())){
                if(tipComment.getUpIdx() != 0){
                    tip.setComment(tip.getComment() - 1);
                    tipRepository.save(tip);

                    tipCommentRepository.delete(tipComment);

                    Optional<NoticeEntity> notice = noticeRepository.findByNoticeTypeAndFromUserIdAndPostTypeAndPostIdxAndCommentIdx("reply", user.getId(), "tip", tip.getIdx(), tipComment.getIdx());
                    if(notice.isPresent()){
                        noticeRepository.delete(notice.get());
                    }
                }else{
                    List<TipCommentEntity> replyCommentList = tipCommentRepository.findByUpIdx(idx);
                    int size = replyCommentList.size();

                    if(!replyCommentList.isEmpty()){
                        tip.setComment(tip.getComment() - size);
                        tipRepository.save(tip);

                        tipCommentRepository.deleteAllInBatch(replyCommentList);

                        List<NoticeEntity> noticeList = noticeRepository.findAllByNoticeTypeAndPostTypeAndPostIdxAndCommentUpIdx("reply","tip", tip.getIdx(), tipComment.getIdx());

                        if(!noticeList.isEmpty()){
                            noticeRepository.deleteAllInBatch(noticeList);
                        }
                    }
                    tip.setComment(tip.getComment() - 1);
                    tipRepository.save(tip);

                    tipCommentRepository.delete(tipComment);

                    Optional<NoticeEntity> notice = noticeRepository.findByNoticeTypeAndPostTypeAndPostIdxAndCommentIdx("comment","tip", tip.getIdx(), tipComment.getIdx());

                    if(notice.isPresent()){
                        noticeRepository.delete(notice.get());
                    }
                }
            }
        }
    }
}
