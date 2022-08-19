package com.gwangjubob.livealone.backend.controller;

import com.gwangjubob.livealone.backend.service.JwtService;
import com.gwangjubob.livealone.backend.service.SocialService;
import com.gwangjubob.livealone.backend.service.impl.MailService;
import com.gwangjubob.livealone.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
public class SocialController {
    private static final String okay = "SUCCESS";
    private static final String fail = "FAIL";
    private static final String timeOut = "access-token timeout";
    private final UserService userService;
    private final JwtService jwtService;
    private final SocialService socialService;
    private final MailService mailService;
    private static HttpStatus status = HttpStatus.NOT_FOUND;
    @Autowired
    SocialController(UserService userService , SocialService socialService, JwtService jwtService, MailService mailService){
        this.userService = userService;
        this.jwtService = jwtService;
        this.mailService = mailService;
        this.socialService = socialService;
    }
    @PostMapping("/google") // 소셜로그인 - 구글
    public ResponseEntity<?> authGoogle(HttpServletRequest request ,HttpServletResponse response) throws Exception{
        Map<String, Object> resultMap = new HashMap<>();
        String authToken = request.getHeader("authToken");
        try {
            String[] res = socialService.googleLogin(authToken);
            if(res!=null) {
                String accessToken = jwtService.createAccessToken("id", res[0]);
                String refreshToken = jwtService.createRefreshToken("id", res[0]);
                resultMap.put("isRegist",res[1]);
                resultMap.put("access-token", accessToken);
                resultMap.put("message", okay);

                Cookie refreshCookie = new Cookie("refresh-token",refreshToken);
                refreshCookie.setMaxAge(1*60*60);
                refreshCookie.setPath("/");
                refreshCookie.setHttpOnly(true);

                response.addCookie(refreshCookie);
                status = HttpStatus.OK;
            }else{
                resultMap.put("message", fail);
                status = HttpStatus.UNAUTHORIZED;
            }
        } catch (Exception e){
            resultMap.put("message", fail);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(resultMap, status);
    }
    @PostMapping("/naver") // 소셜로그인 - 네이버
    public ResponseEntity<?> authNaver(HttpServletRequest request ,HttpServletResponse response) throws Exception{
        Map<String, Object> resultMap = new HashMap<>();
        String authToken = request.getHeader("authToken");
        try {
            String []res = socialService.naverLogin(authToken);
            if(res!=null) {
                String accessToken = jwtService.createAccessToken("id", res[0]);
                String refreshToken = jwtService.createRefreshToken("id", res[0]);
                resultMap.put("isRegist",res[1]);
                resultMap.put("access-token", accessToken);
                resultMap.put("message", okay);

                Cookie refreshCookie = new Cookie("refresh-token",refreshToken);
                refreshCookie.setMaxAge(1*60*60);
                refreshCookie.setPath("/");
                refreshCookie.setHttpOnly(true);

                response.addCookie(refreshCookie);
                status = HttpStatus.OK;
            }else{
                resultMap.put("message", fail);
                status = HttpStatus.UNAUTHORIZED;
            }
        } catch (Exception e){
            resultMap.put("message", fail);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(resultMap, status);
    }
    @PostMapping("/kakao") // 소셜로그인 - 카카오
    public ResponseEntity<?> authKakao(HttpServletRequest request ,HttpServletResponse response) throws Exception{
        Map<String, Object> resultMap = new HashMap<>();
        String authToken = request.getHeader("authToken");
        try {
            String[] res = socialService.kakaoLogin(authToken);
            if(res!=null) {
                String accessToken = jwtService.createAccessToken("id", res[0]);
                String refreshToken = jwtService.createRefreshToken("id", res[0]);
                resultMap.put("isRegist",res[1]);
                resultMap.put("access-token", accessToken);

                resultMap.put("message", okay);

                Cookie refreshCookie = new Cookie("refresh-token",refreshToken);
                refreshCookie.setMaxAge(1*60*60);
                refreshCookie.setPath("/");
                refreshCookie.setHttpOnly(true);

                response.addCookie(refreshCookie);
                status = HttpStatus.OK;
            }else{
                resultMap.put("message", fail);
                status = HttpStatus.UNAUTHORIZED;
            }
        } catch (Exception e){
            resultMap.put("message", fail);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(resultMap, status);
    }

    public String checkToken(HttpServletRequest request, Map<String, Object> resultMap){
        String accessToken = request.getHeader("Authorization");
        String decodeId = jwtService.decodeToken(accessToken);
        if(!decodeId.equals("timeout")){
            return decodeId;
        }else{
            resultMap.put("message", timeOut);
            status = HttpStatus.UNAUTHORIZED;
            return null;
        }
    }


}

