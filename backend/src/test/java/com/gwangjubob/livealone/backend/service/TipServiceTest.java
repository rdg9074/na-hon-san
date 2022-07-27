package com.gwangjubob.livealone.backend.service;

import com.gwangjubob.livealone.backend.domain.entity.TipCommentEntity;
import com.gwangjubob.livealone.backend.domain.entity.TipEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import com.gwangjubob.livealone.backend.domain.repository.TipCommentRepository;
import com.gwangjubob.livealone.backend.domain.repository.TipRepository;
import com.gwangjubob.livealone.backend.domain.repository.UserRepository;
import com.gwangjubob.livealone.backend.dto.tip.TipCreateDto;
import com.gwangjubob.livealone.backend.dto.tip.TipViewDto;
import com.gwangjubob.livealone.backend.dto.tipcomment.TipCommentCreateDto;
import com.gwangjubob.livealone.backend.dto.tipcomment.TipCommentUpdateDto;
import com.gwangjubob.livealone.backend.dto.user.UserInfoDto;
import com.gwangjubob.livealone.backend.mapper.UserInfoMapper;
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
    private UserService userService;
    private UserInfoMapper userInfoMapper;
    private TipCommentService tipCommentService;
    private UserRepository userRepository;
    private TipCommentRepository tipCommentRepository;

    @Autowired
    public TipServiceTest(TipCommentService tipCommentService, TipRepository tipRepository, TipService tipService, UserInfoMapper userInfoMapper, UserService userService
    , UserRepository userRepository, TipCommentRepository tipCommentRepository){
        this.tipRepository = tipRepository;
        this.tipService = tipService;
        this.userService = userService;
        this.userInfoMapper = userInfoMapper;
        this.tipCommentService = tipCommentService;
        this.userRepository = userRepository;
        this.tipCommentRepository = tipCommentRepository;
    }

    @Test
    @Disabled
    public void 게시글_등록_테스트() {
        // given
        String  testId = "test";

        UserEntity testUser = userInfoMapper.toEntity(userService.infoUser(testId));

        String category = "recipe";
        String title = "꿀템추천sss";
        String content = "이거 좋음 ㄱㄱ";
        String bannerImg = "src/img/item/img.jpg";

        TipCreateDto testDto = new TipCreateDto(testUser, category, title, content, bannerImg);
//        TipEntity testEntity = testDto.toEntity();
//
//        // when
//        tipRepository.save(testEntity);

        // then
//        Optional<TipEntity> result = tipRepository.findByIdx(test.getIdx());
//        System.out.println(result.toString());
    }

    @Test
    @Disabled
    public void 카테고리별_리스트_조회_테스트(){
        // given
        String category = "tip";

        // when
        List<TipViewDto> result = tipService.viewTip(category);

        // then
//
        for(TipViewDto dto : result){
            System.out.println(dto.toString());
        }

    }

    @Test
    @Disabled
    public void 댓글_대댓글_등록_테스트(){
        // given
        String nickname = "비밀번호는 ssafy 입니다.";
        UserEntity user = userRepository.findByNickname(nickname).get();

        TipEntity tip = tipRepository.findByIdx(22).get(); // 게시글
        Integer upIdx = 19; // 댓글 번호
        String content = "대댓글테스트.";

        // when
        TipCommentCreateDto dto = new TipCommentCreateDto();
        dto.setUpIdx(upIdx);
        dto.setPostIdx(tip.getIdx());
        dto.setContent(content);

        tipCommentService.createTipComment(user.getId(), dto);

        // then
//        TipCommentEntity result = tipCommentRepository.findByIdx(13);

//        System.out.println(result.toString());
    }

    @Test
    public void 댓글_대댓글_수정_테스트(){
        // given
        String nickname = "비밀번호는 ssafy 입니다.";
        String content = "대댓글 테스트";
        String bannerImg = "이미지형식";
//        String bannerImg = null;
        Integer idx = 19;
        Integer postIdx = 22;
        Integer upIdx = 19;

        UserEntity user = userRepository.findByNickname(nickname).get();
        TipEntity tip = tipRepository.findByIdx(postIdx).get();
        TipCommentEntity tipComment = tipCommentRepository.findByIdx(idx);


        // when
        if(nickname.equals(tipComment.getUser().getNickname())){
            TipCommentUpdateDto dto = new TipCommentUpdateDto();
            dto.setContent(content);
            dto.setBannerImg(bannerImg);
//            dto.setTime(LocalDate.now());
            tipComment = TipCommentEntity.builder()
                    .idx(idx)
                    .user(user)
                    .tip(tip)
                    .content(dto.getContent())
                    .bannerImg(dto.getBannerImg())
                    .time(LocalDate.now())
                    .upIdx(tipComment.getUpIdx())
                    .build();

            tipCommentRepository.saveAndFlush(tipComment);
        }

        // then
//        TipCommentEntity result = tipCommentRepository.findByIdx(13);
//        System.out.println(result.toString());
        System.out.println(LocalDateTime.now());
    }

    @Test
    public void 댓글_대댓글_삭제_테스트() {
        // given
        Integer idx = 20;
        String testId = "ssafy";
        TipCommentEntity tipComment = tipCommentRepository.findByIdx(idx);

        // when
        if(testId.equals(tipComment.getUser().getId())){ // 아이디랑 댓글작성자 아이디가 같으면 삭제 가능
            if(tipComment.getUpIdx() != 0){ // 0이 아니면 대댓글이므로 그냥 삭제 가능
                tipCommentRepository.delete(tipComment);
            }else{ // 댓글이랑 엮인 대댓글까지 삭제해야함
                while(true){

                }
            }
        }

        // then
    }
}
