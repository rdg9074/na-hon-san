package com.gwangjubob.livealone.backend.service;

import com.gwangjubob.livealone.backend.domain.entity.TipEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import com.gwangjubob.livealone.backend.domain.repository.TipRepository;
import com.gwangjubob.livealone.backend.domain.repository.UserRepository;
import com.gwangjubob.livealone.backend.dto.tip.TipViewDto;
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

    @Autowired
    public TipServiceTest(TipRepository tipRepository, TipService tipService, UserInfoMapper userInfoMapper, UserService userService){
        this.tipRepository = tipRepository;
        this.tipService = tipService;
        this.userService = userService;
        this.userInfoMapper = userInfoMapper;
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

        TipEntity test = TipEntity.builder()
                .user(user)
                .category(category)
                .title(title)
                .content(content)
                .bannerImg(bannerImg)
                .build();

        // when
        tipRepository.save(test);

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
}
