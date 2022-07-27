package com.gwangjubob.livealone.backend.service;

import com.gwangjubob.livealone.backend.domain.entity.TipCommentEntity;
import com.gwangjubob.livealone.backend.domain.entity.TipEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import com.gwangjubob.livealone.backend.domain.repository.TipCommentRepository;
import com.gwangjubob.livealone.backend.domain.repository.TipRepository;
import com.gwangjubob.livealone.backend.domain.repository.UserRepository;
import com.gwangjubob.livealone.backend.dto.tip.TipCreateDto;
import com.gwangjubob.livealone.backend.dto.tip.TipUpdateDto;
import com.gwangjubob.livealone.backend.dto.tip.TipViewDto;
import com.gwangjubob.livealone.backend.dto.tipcomment.TipCommentCreateDto;
import com.gwangjubob.livealone.backend.dto.tipcomment.TipCommentUpdateDto;
import com.gwangjubob.livealone.backend.dto.user.UserInfoDto;
import com.gwangjubob.livealone.backend.mapper.TipCreateMapper;
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

    @Autowired
    public TipServiceTest(TipCommentService tipCommentService, TipRepository tipRepository, TipService tipService,
                            UserRepository userRepository, TipCommentRepository tipCommentRepository, TipCreateMapper tipCreateMapper,
                          TipUpdateMapper tipUpdateMapper){
        this.tipRepository = tipRepository;
        this.tipService = tipService;
        this.tipCommentService = tipCommentService;
        this.userRepository = userRepository;
        this.tipCommentRepository = tipCommentRepository;
        this.tipCreateMapper = tipCreateMapper;
        this.tipUpdateMapper = tipUpdateMapper;
    }

    @Test
    public void 게시글_등록_테스트() {
        // given
        String  testNickname = "비밀번호는 test 입니다.";

        Optional<UserEntity> optionalUser = userRepository.findByNickname(testNickname); // 사용자 정보

        String category = "item";
        String title = "꿀템테스트333";
        String content = "게시글테스트1 텍스트";
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
        String category = "tip";

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
        Integer idx = 22;

        // when

        // then

    }

    @Test
    public void 게시글_수정_테스트() {
        // given
        String testNickname = "비밀번호는 ssafy 입니다.";
        Integer testIdx = 26;

        // 수정할 수 있는데이터
        String category = null;
        String title = "꿀템수정제목만";
        String content = null;
        byte[] bannerImg = null;

        TipEntity tip = tipRepository.findByIdx(testIdx).get(); // idx에 해당하는 게시글 가져오기
        UserEntity user = userRepository.findByNickname(testNickname).get();
        // when
        if(testNickname.equals(tip.getUser().getNickname())){
            // 로그인 한 닉네임과 글 작성자가 같으면 수정 가능
            TipUpdateDto updateDto = TipUpdateDto.builder()
                    .idx(testIdx)
                    .category(category)
                    .title(title)
                    .content(content)
                    .bannerImg(bannerImg)
                    .build();

            TipEntity updateEntity = tipUpdateMapper.toEntity(updateDto);
            updateEntity.setUser(user);
            updateEntity.setTime(LocalDateTime.now());

            tipRepository.save(updateEntity);
        }

        // then
//        Optional<TipEntity> result = tipRepository.findByTitle(title);
//        if(result.isPresent()) {
//            TipEntity entity = result.get();
//            System.out.println(entity.toString());
//        }
    }

    @Test
    public void 게시글_삭제_테스트(){
        String testId = "ssafy";
        Integer idx = 22;

        TipEntity tip = tipRepository.findByIdx(idx).get();
        // when
        if(testId.equals(tip.getUser().getId())){
            // 작성자와 로그인한 사용자의 아이디가 같다면 삭제
            // 삭제 시 댓/대댓글도 모두 삭제
            // cascade 걸려있으니 그냥 삭제?
            tipRepository.delete(tip);
        }
    }

    @Test
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
        byte[] bannerImg = null;
//        String bannerImg = null;
        Integer idx = 19;
        Integer postIdx = 22;
        Integer upIdx = 19;

        UserEntity user = userRepository.findByNickname(nickname).get();
        TipEntity tip = tipRepository.findByIdx(postIdx).get();
        TipCommentEntity tipComment = tipCommentRepository.findByIdx(idx);


//        // when
//        if(nickname.equals(tipComment.getUser().getNickname())){
//            TipCommentUpdateDto dto = new TipCommentUpdateDto();
//            dto.setContent(content);
//            dto.setBannerImg(bannerImg);
////            dto.setTime(LocalDate.now());
//            tipComment = TipCommentEntity.builder()
//                    .idx(idx)
//                    .user(user)
//                    .tip(tip)
//                    .content(dto.getContent())
//                    .bannerImg(dto.getBannerImg())
//                    .time(LocalDate.now())
//                    .upIdx(tipComment.getUpIdx())
//                    .build();
//
//            tipCommentRepository.saveAndFlush(tipComment);
//        }

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
