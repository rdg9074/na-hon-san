package com.gwangjubob.livealone.backend.service;

import com.gwangjubob.livealone.backend.domain.entity.TipCommentEntity;
import com.gwangjubob.livealone.backend.domain.entity.TipEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserLikeTipsEntity;
import com.gwangjubob.livealone.backend.domain.repository.TipCommentRepository;
import com.gwangjubob.livealone.backend.domain.repository.TipRepository;
import com.gwangjubob.livealone.backend.domain.repository.UserLikeTipsRepository;
import com.gwangjubob.livealone.backend.domain.repository.UserRepository;
import com.gwangjubob.livealone.backend.dto.tip.TipCreateDto;
import com.gwangjubob.livealone.backend.dto.tip.TipDetailViewDto;
import com.gwangjubob.livealone.backend.dto.tip.TipUpdateDto;
import com.gwangjubob.livealone.backend.dto.tip.TipViewDto;
import com.gwangjubob.livealone.backend.dto.tipcomment.TipCommentCreateDto;
import com.gwangjubob.livealone.backend.dto.tipcomment.TipCommentUpdateDto;
import com.gwangjubob.livealone.backend.dto.tipcomment.TipCommentViewDto;
import com.gwangjubob.livealone.backend.dto.user.UserInfoDto;
import com.gwangjubob.livealone.backend.mapper.TipCreateMapper;
import com.gwangjubob.livealone.backend.mapper.TipDetailViewMapper;
import com.gwangjubob.livealone.backend.mapper.TipUpdateMapper;
import com.gwangjubob.livealone.backend.mapper.UserInfoMapper;
import org.apache.catalina.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
//@Transactional
public class TipServiceTest {
    private TipRepository tipRepository;
    private TipService tipService;
    private TipCommentService tipCommentService;
    private UserRepository userRepository;
    private TipCommentRepository tipCommentRepository;
    private TipCreateMapper tipCreateMapper;
    private TipUpdateMapper tipUpdateMapper;
    private TipDetailViewMapper tipDetailViewMapper;
    private UserLikeTipsRepository userLikeTipsRepository;

    @Autowired
    public TipServiceTest(TipCommentService tipCommentService, TipRepository tipRepository, TipService tipService,
                            UserRepository userRepository, TipCommentRepository tipCommentRepository, TipCreateMapper tipCreateMapper,
                          TipUpdateMapper tipUpdateMapper, TipDetailViewMapper tipDetailViewMapper, UserLikeTipsRepository userLikeTipsRepository){
        this.tipRepository = tipRepository;
        this.tipService = tipService;
        this.tipCommentService = tipCommentService;
        this.userRepository = userRepository;
        this.tipCommentRepository = tipCommentRepository;
        this.tipCreateMapper = tipCreateMapper;
        this.tipUpdateMapper = tipUpdateMapper;
        this.tipDetailViewMapper = tipDetailViewMapper;
        this.userLikeTipsRepository = userLikeTipsRepository;
    }

    @Test
    public void 게시글_등록_테스트() {
        // given
        String  testNickname = "비밀번호는 test 입니다.";

        Optional<UserEntity> optionalUser = userRepository.findByNickname(testNickname); // 사용자 정보

        String category = "tip";
        String title = "꿀팁 제목테스트";
        String content = "꿀팁 내용 테스트";
        byte[] bannerImg = null;

        // when
        if(optionalUser.isPresent()){ // 회원 정보가 있다면
            UserEntity user = optionalUser.get();
            TipCreateDto dto = TipCreateDto.builder()
                    .userNickname(testNickname)
                    .category(category)
                    .title(title)
                    .content(content)
                    .bannerImg(bannerImg)
                    .build();

            TipEntity tip = tipCreateMapper.toEntity(dto);
            tip.setUser(user);

            tipRepository.save(tip);

            TipCreateDto tipDto = tipCreateMapper.toDto(tip);
            tipDto.setUserNickname(tip.getUser().getNickname());

            System.out.println("게시글 등록 성공");
        }else{
            System.out.println("게시글 등록 실패 - 회원 정보 없음");
        }

        // then
        Optional<TipEntity> result = tipRepository.findByTitle(title);
        if(result.isPresent()) {
            TipEntity entity = result.get();
            System.out.println(entity.toString());
        }
    }

    @Test
    public void 카테고리별_리스트_조회_테스트(){
        // given
        String category = "item";

        // when
        List<TipViewDto> result = tipService.viewTip(category);
        // then

        for(TipViewDto dto : result){
            System.out.println(dto.toString());
        }
    }

    @Test
    public void 게시글_상세_조회_테스트() {
        // given
        Integer idx = 47;

        Optional<TipEntity> testTip = tipRepository.findByIdx(idx);

        if(testTip.isPresent()){
            // when
            TipEntity tipEntity = testTip.get();
            TipDetailViewDto tipDto = tipDetailViewMapper.toDto(tipEntity);
            tipDto.setUserNickname(tipEntity.getUser().getNickname());

            // 조회수 증가
            tipEntity.setView(tipEntity.getView() + 1);
            tipRepository.save(tipEntity);

            // then
            System.out.println(tipDto.toString());

            // 게시글 관련된 댓글 조회
            List<TipCommentEntity> tipCommentEntity = tipCommentRepository.findByTip(tipEntity);
            List<TipCommentViewDto> result = new ArrayList<>();

            for(TipCommentEntity t : tipCommentEntity){
                TipCommentViewDto dto = TipCommentViewDto.builder()
                        .idx(t.getIdx())
                        .userProfileImg(t.getUser().getProfileImg())
                        .userNickname(t.getUser().getNickname())
                        .content(t.getContent())
                        .bannerImg(t.getBannerImg())
                        .time(t.getTime())
                        .build();

                result.add(dto);
            }

            // then
            System.out.println(tipDto.toString());
            for(TipCommentViewDto t : result){
                System.out.println(t.toString());
            }
        }

    }

    @Test
    public void 게시글_수정_테스트() {
        // given
        String testNickname = "test";
        Integer testIdx = 47;

        // 수정할 수 있는데이터
        String category = "tip";
        String title = "꿀팁 게시글 수정";
        String content = "꿀팁 내용 테스트 수정22";
        byte[] bannerImg = null;

        TipUpdateDto updateDto = new TipUpdateDto(category,title,content,bannerImg);

        Optional<TipEntity> optionalTip = tipRepository.findByIdx(testIdx); // idx에 해당하는 게시물 조회
        UserEntity userEntity = userRepository.findById(testNickname).get();

        if(optionalTip.isPresent()){
            TipEntity tipEntity = optionalTip.get();
            // 게시글 작성자와 로그인 아이디가 일치하면
            if(userEntity.getId().equals(tipEntity.getUser().getId())){
                tipUpdateMapper.updateFromDto(updateDto,tipEntity); // 업데이트

                tipEntity.setUpdateTime(LocalDateTime.now());
                tipRepository.saveAndFlush(tipEntity);
            }
        }
        // then
        Optional<TipEntity> result = tipRepository.findByTitle(title);
        if(result.isPresent()) {
            TipEntity entity = result.get();
            System.out.println(entity.toString());
        }
    }

    @Test
    public void 게시글_삭제_테스트(){
        // given
        String testId = "test";
        Integer idx = 46;

        Optional<TipEntity> testTip = tipRepository.findByIdx(idx);

        if(testTip.isPresent()){
            TipEntity tip = testTip.get();
            // when
            if(testId.equals(tip.getUser().getId())){
                // 작성자와 로그인한 사용자의 아이디가 같다면 삭제
                // 삭제 시 댓/대댓글도 모두 삭제
                // cascade 걸려있으니 그냥 삭제?
                tipRepository.delete(tip);
            }
        }
    }


    @Test
    public void 댓글_대댓글_등록_테스트(){
        // given
        int upIdx = 0;

        String testNickname = "ssafy";
        UserEntity user = userRepository.findByNickname(testNickname).get();

        String content = "ㅎㅇㅎㅇ22";
        byte[] bannerImg = null;

        int postIdx = 47;
        Optional<TipEntity> optionalTipEntity = tipRepository.findByIdx(postIdx);

        if(optionalTipEntity.isPresent()){
            TipEntity tip = optionalTipEntity.get();

            TipCommentCreateDto dto = TipCommentCreateDto.builder()
                    .content(content)
                    .bannerImg(bannerImg)
                    .build();

            TipCommentEntity entity = TipCommentEntity.builder()
                    .user(user)
                    .upIdx(upIdx)
                    .tip(tip)
                    .content(dto.getContent())
                    .bannerImg(dto.getBannerImg())
                    .build();

            tipCommentRepository.save(entity); // 댓글 등록
            
            tip.setComment(tip.getComment() + 1); // 댓글 수 1 증가
            tipRepository.save(tip); // 수정
        }
    }

    @Test
    public void 댓글_대댓글_수정_테스트(){
        // given
        String nickname = "test";
        String content = "댓글 수정 테스트2222";
        byte[] bannerImg = null;
        Integer idx = 61;
        Integer upIdx = 60; // 댓글수정이면 0, 대댓글수정이면 댓글 글번호

        UserEntity user = userRepository.findByNickname(nickname).get();
        Optional<TipCommentEntity> optionalTipComment = tipCommentRepository.findByIdx(idx);

        if(optionalTipComment.isPresent()){ // 댓글이 있다면 수정
            TipCommentEntity testTipComment = optionalTipComment.get();
            TipEntity tip = tipRepository.findByIdx(testTipComment.getTip().getIdx()).get();

            TipCommentEntity tipComment = TipCommentEntity.builder()
                    .idx(idx)
                    .user(user)
                    .tip(tip)
                    .upIdx(upIdx)
                    .content(content)
                    .time(testTipComment.getTime())
                    .updateTime(LocalDateTime.now())
                    .bannerImg(bannerImg)
                    .build();

            tipCommentRepository.saveAndFlush(tipComment);
        }

        // then
        Optional<TipCommentEntity> result = tipCommentRepository.findByIdx(idx);
        if(result.isPresent()){
            System.out.println("테스트 통과");
        }

    }

    @Test
    public void 댓글_대댓글_삭제_테스트() {
        // given
        Integer postIdx = 47;
        Integer idx = 72; // 댓글 번호

        String testNickname = "test";
        Optional<TipCommentEntity> optionalTipComment = tipCommentRepository.findByIdx(idx);
        TipEntity tip = tipRepository.findByIdx(postIdx).get();

        if(optionalTipComment.isPresent()){
            TipCommentEntity tipComment = optionalTipComment.get();
            // when
            if(testNickname.equals(tipComment.getUser().getNickname())){ // 아이디랑 댓글작성자 아이디가 같으면 삭제 가능
                if(tipComment.getUpIdx() != 0){ // 0이 아니면 대댓글이므로 그냥 삭제 가능
                    tip.setComment(tip.getComment() - 1);
                    tipRepository.save(tip);

                    tipCommentRepository.delete(tipComment);

                }else{ // 댓글이랑 엮인 대댓글"들"까지 삭제해야함
                    List<TipCommentEntity> replyCommentList = tipCommentRepository.findByUpIdx(idx); // 대댓글 리스트 조회
                    int size = replyCommentList.size();

                    if(!replyCommentList.isEmpty()){
                        tip.setComment(tip.getComment() - size - 1);
                        tipRepository.save(tip);

                        tipCommentRepository.deleteAllInBatch(replyCommentList);
                    }
                    tipCommentRepository.delete(tipComment); // 댓글 삭제
                }
            }
        }

        // then
    }

    @Test
    public void 게시글_좋아요_테스트(){
        // givn
        Integer postIdx = 47; // 게시글 번호
        String userId = "test"; // 로그인 한 사용자 아이디

        UserEntity userEntity = userRepository.findById(userId).get();
        TipEntity tipEntity = tipRepository.findByIdx(postIdx).get(); // 해당 게시물로 이동
        // when
        // 좋아요를 눌렀는지 확인
        Optional<UserLikeTipsEntity> userLikeTipsEntity = userLikeTipsRepository.findByUserAndTip(userEntity, tipEntity);

        if(userLikeTipsEntity.isPresent()){
            // 회원이 게시물에 좋아요를 이미 누른 상태 -> 한 번 더 클릭하면 좋아요 취소
            userLikeTipsRepository.delete(userLikeTipsEntity.get());
            tipEntity.setLike(tipEntity.getLike() - 1); // 좋아요 취소
            tipRepository.save(tipEntity);
        }else{
            // 좋아요 누르지 않은 상태 -> 좋아요
            // 좋아요 버튼을 한 번 누르면 -> 좋아요 등록
            UserLikeTipsEntity likeTipsEntity = UserLikeTipsEntity.builder()
                    .tip(tipEntity)
                    .user(userEntity)
                    .time(LocalDateTime.now())
                    .build();
            userLikeTipsRepository.save(likeTipsEntity);

            tipEntity.setLike(tipEntity.getLike() + 1);
            tipRepository.save(tipEntity);

        }



    }
}
