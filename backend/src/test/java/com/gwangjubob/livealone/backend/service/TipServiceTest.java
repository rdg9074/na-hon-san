package com.gwangjubob.livealone.backend.service;

import com.gwangjubob.livealone.backend.domain.repository.TipRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class TipServiceTest {
    private TipRepository tipRepository;
    private TipService tipService;

    @Autowired
    public TipServiceTest(TipRepository tipRepository, TipService tipService){
        this.tipRepository = tipRepository;
        this.tipService = tipService;
    }

    @Test
    public void 게시글_등록_테스트() {
        // given

        // when

        // then
    }
}
