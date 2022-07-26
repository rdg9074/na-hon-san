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
import com.gwangjubob.livealone.backend.dto.user.UserInfoDto;
import com.gwangjubob.livealone.backend.mapper.UserInfoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
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
    public void 게시글_등록_테스트() {
        // given
        String  testId = "ssafy";
        UserInfoDto testUser = userService.infoUser(testId); // 사용자 정보 가져오기?
        UserEntity user = userInfoMapper.toEntity(testUser);

        System.out.println(testUser.toString());
        String category = "tip";
        String title = "게시글테스트1";
        String content = "게시글테스트1 텍스트";
        String bannerImg = "src/img/img1.jpg";

        TipCreateDto testDto = new TipCreateDto(user, category, title, content, bannerImg);
        TipEntity testEntity = testDto.toEntity();

        // when
        tipRepository.save(testEntity);

        // then
//        Optional<TipEntity> result = tipRepository.findByIdx(test.getIdx());
//        System.out.println(result.toString());
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
    public void 댓글_대댓글_등록_테스트(){
        // given
        String nickname = "비밀번호는 test 입니다.";
        UserEntity user = userRepository.findByNickname(nickname).get();

        TipEntity tip = tipRepository.findByIdx(12).get(); // 게시글
        Integer upIdx = 3; // 댓글 번호
        String content = "별말씀을";

        // when
        TipCommentCreateDto dto = new TipCommentCreateDto();
        dto.setPostIdx(tip.getIdx());
        dto.setContent(content);
        dto.setUpIdx(upIdx);

        tipCommentService.createTipComment("ssafy", dto);

        // then
        List<TipCommentEntity> result = tipCommentRepository.findAll();

        for(TipCommentEntity t : result){
            System.out.println(t.toString());
        }
    }
}
