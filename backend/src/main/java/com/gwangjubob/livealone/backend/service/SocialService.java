package com.gwangjubob.livealone.backend.service;

import com.gwangjubob.livealone.backend.dto.notice.NoticeViewDto;
import org.json.simple.parser.ParseException;

import java.util.List;

public interface SocialService {
    String[] kakaoLogin(String authToken);

    String[] naverLogin(String authToken) throws ParseException;

    String[] googleLogin(String authToken);
}
