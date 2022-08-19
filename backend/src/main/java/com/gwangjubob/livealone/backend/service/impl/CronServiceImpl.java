package com.gwangjubob.livealone.backend.service.impl;

import com.gwangjubob.livealone.backend.domain.entity.ApiActionEntity;
import com.gwangjubob.livealone.backend.domain.repository.ApiActionRepository;
import com.gwangjubob.livealone.backend.service.CronService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CronServiceImpl implements CronService {
    private ApiActionRepository apiActionRepository;
    @Autowired
    CronServiceImpl(ApiActionRepository apiActionRepository){
        this.apiActionRepository = apiActionRepository;
    }
    @Override
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void apiInit() {
        Optional<ApiActionEntity> optionalApiAction = apiActionRepository.findById(1);
        if(optionalApiAction.isPresent()){
            ApiActionEntity apiAction = optionalApiAction.get();
            apiAction.setApi(0);
            apiActionRepository.save(apiAction);
            System.out.println("api Log : api 사용 횟수 초기화 완료");
        } else{
            System.out.println("api Log : api 사용 횟수 초기화 실패");
        }
    }
}
