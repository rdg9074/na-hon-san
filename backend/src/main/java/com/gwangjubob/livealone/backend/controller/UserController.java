package com.gwangjubob.livealone.backend.controller;

import com.gwangjubob.livealone.backend.dto.mail.MailCheckDto;
import com.gwangjubob.livealone.backend.dto.mail.MailSendDto;
import com.gwangjubob.livealone.backend.dto.user.UserMoreDTO;
import com.gwangjubob.livealone.backend.dto.user.UserRegistDto;
import com.gwangjubob.livealone.backend.dto.user.UserInfoDto;
import com.gwangjubob.livealone.backend.service.JwtService;
import com.gwangjubob.livealone.backend.service.impl.MailService;
import com.gwangjubob.livealone.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.gwangjubob.livealone.backend.dto.user.UserLoginDto;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    private static final String okay = "SUCCESS";
    private static final String fail = "FAIL";
    private static final String timeOut = "access-token timeout";
    private final UserService userService;
    private final JwtService jwtService;
    private final MailService mailService;
    private static HttpStatus status = HttpStatus.NOT_FOUND;
    @Autowired
    UserController(UserService userService ,JwtService jwtService,MailService mailService){
        this.userService = userService;
        this.jwtService = jwtService;
        this.mailService = mailService;
    }

    @PostMapping("/user") // 회원 가입
    public ResponseEntity<?> registUser(@RequestBody UserRegistDto userRegistDto) throws Exception{
        Map<String, Object> resultMap = new HashMap<>();
        try {
            userService.registUser(userRegistDto); // 회원 등록 서비스 호출
            resultMap.put("message", okay);
            status = HttpStatus.OK;
        } catch (Exception e){
            resultMap.put("message", fail);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @GetMapping("/user/check/{nickname}") // 닉네임 체크
    public ResponseEntity<?> checkNickName(@PathVariable String nickname){
        Map<String, Object> resultMap = new HashMap<>();
        try {
            if(userService.checkNickName(nickname)){ // 닉네임 서비스 호출
                resultMap.put("message", fail);
            } else{
                resultMap.put("message", okay);
            }
            status = HttpStatus.OK;
        }catch (Exception e){
            resultMap.put("message", fail);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(resultMap, status);
    }
    @PostMapping("/user/login") // 로그인
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDto userLoginDto, HttpServletRequest request, HttpServletResponse response) throws Exception{
        Map<String, Object> resultMap = new HashMap<>();
        try {
            if(userService.loginUser(userLoginDto)){//로그인 서비스 호출
                String accessToken = jwtService.createAccessToken("id", userLoginDto.getId());
                String refreshToken = jwtService.createRefreshToken("id", userLoginDto.getId());
                resultMap.put("access-token", accessToken);
                resultMap.put("message", okay);
                // create a cookie

                Cookie refreshCookie = new Cookie("refresh-token",refreshToken);
                refreshCookie.setMaxAge(1*60*60);
                refreshCookie.setPath("/");
                refreshCookie.setHttpOnly(true);
                refreshCookie.setSecure(true);

                response.addCookie(refreshCookie);
            }else{
                resultMap.put("message", fail);
            }

            status = HttpStatus.OK;
        }catch (Exception e){
            status = HttpStatus.OK;
            resultMap.put("message",fail);
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @GetMapping("/user/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response){
        Map<String, Object> resultMap = new HashMap<>();
        try{
            Cookie oldCookie = null;
            Cookie[] cookies = request.getCookies();
            if (cookies != null){
                for (Cookie c: cookies) {
                    if(c.getName().equals("refresh-token")){
                        oldCookie = c;
                    }
                }
            }
            if(oldCookie != null){
                oldCookie.setValue(null);
                oldCookie.setPath("/");
                oldCookie.setMaxAge(0);
                response.addCookie(oldCookie);
            }
            status = HttpStatus.OK;
            resultMap.put("message", okay);
        }catch (Exception e){
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            resultMap.put("message", fail);
        }
        return new ResponseEntity<>(resultMap, status);
    }
    @GetMapping("/user/login") // 엑세스 토큰 재발급
    public  ResponseEntity<?> updateAccessToken(HttpServletRequest request, @CookieValue("refresh-token") String refreshToken){
        Map<String, Object> resultMap = new HashMap<>();
        String decodeId = jwtService.decodeToken(refreshToken);
        if(!decodeId.equals("timeout")){
            String accessToken = jwtService.createAccessToken("id", decodeId);
            resultMap.put("access-token", accessToken);
            resultMap.put("message", okay);
            status = HttpStatus.OK;
        }else{
            status = HttpStatus.UNAUTHORIZED;
            resultMap.put("message", "refreshTimeout");
        }
        return new ResponseEntity<>(resultMap,status);
    }
    @PutMapping("/user/password") // 비밀번호 수정
    public ResponseEntity<?> updatePassword(@RequestBody UserLoginDto userLoginDto) throws Exception{
        Map<String, Object> resultMap = new HashMap<>();
        try {
            boolean res = userService.updatePassword(userLoginDto); // 회원 수정 서비스 호출
            if(res){
                resultMap.put("message", okay);
            } else{
                resultMap.put("message", fail);
            }
            status = HttpStatus.OK;
        } catch(Exception e){
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            resultMap.put("message", fail);
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @PostMapping("/user/auth") // 인증 메일 발송
    public ResponseEntity<?> sendMail(@RequestBody MailSendDto mailSendDto) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            if (mailService.emailCheck(mailSendDto)) {
                mailService.mailSend(mailSendDto);
                resultMap.put("message", okay);
            }else {
                resultMap.put("message", fail);
            }
            status = HttpStatus.OK;
        } catch (Exception e) {
            status = HttpStatus.UNAUTHORIZED;
        }
        return new ResponseEntity<>(resultMap, status);
    }
    @GetMapping("/user/auth") // 인증 메일 체크
    public ResponseEntity<?> checkMail(@ModelAttribute("MailCheckDto") MailCheckDto mailCheckDto) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            if (mailService.checkAuthNumber(mailCheckDto)) {
                resultMap.put("message", okay);
            } else {
                resultMap.put("message", fail);
            }
            status = HttpStatus.OK;
        } catch (Exception e) {
            status = HttpStatus.UNAUTHORIZED;
        }
        return new ResponseEntity<>(resultMap, status);
    }
    @PutMapping("/user") // 회원 정보 수정
    public ResponseEntity<?> updateUser(@RequestBody UserInfoDto userInfoDto, HttpServletRequest request) throws Exception{
        Map<String, Object> resultMap = new HashMap<>();
        String decodeId = checkToken(request, resultMap);
        if (decodeId != null){
            try {
                userInfoDto.setId(decodeId);
                UserInfoDto user = userService.updateUser(userInfoDto);
                if (user != null){
                    resultMap.put("data", user);
                    resultMap.put("message", okay);
                } else{
                    resultMap.put("message", fail);
                }
                status = HttpStatus.OK;
            } catch (Exception e){
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return new ResponseEntity<>(resultMap, status);
    }
    @PutMapping("/user/more") // 회원 추가 정보 수정
    public ResponseEntity<?> moreUpdateUser(@RequestBody UserMoreDTO userMoreDTO, HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        String decodeId = checkToken(request, resultMap);
        if (decodeId != null){
            try {
                userMoreDTO.setUserId(decodeId);
                userService.moreUpdate(userMoreDTO);
                resultMap.put("message", okay);
                status = HttpStatus.OK;
            } catch (Exception e){
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        } else{
            try {
                userService.moreUpdate(userMoreDTO);
                resultMap.put("message", okay);
                status = HttpStatus.OK;
            } catch (Exception e){
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return new ResponseEntity<>(resultMap, status);
    }
    @DeleteMapping("/user") // 회원 탈퇴
    public ResponseEntity<?> deleteUser(HttpServletRequest request) throws Exception{
        Map<String, Object> resultMap = new HashMap<>();
        String decodeId = checkToken(request, resultMap);
        if(decodeId != null){
            try {
                userService.userDelete(decodeId);
                resultMap.put("message", okay);
                status = HttpStatus.OK;
            } catch (Exception e){
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return new ResponseEntity<>(resultMap, status);
    }
    @PostMapping("/user/password") // 비밀번호 확인
    public ResponseEntity<?> passwordCheckUser(@RequestBody UserLoginDto userLoginDto,HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        String decodeId = checkToken(request, resultMap);
        if (decodeId != null) {
            try {
                if (userService.passwordCheckUser(decodeId, userLoginDto.getPassword())) {
                    resultMap.put("message", okay);
                } else {
                    resultMap.put("message", fail);
                }
                status = HttpStatus.OK;
            }catch (Exception e){
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return new ResponseEntity<>(resultMap,status);
    }
    @GetMapping("/user") // 회원 정보 조회
    public ResponseEntity<?> infoUser(HttpServletRequest request) throws Exception{
        Map<String, Object> resultMap = new HashMap<>();
        String decodeId = checkToken(request, resultMap);
        if (decodeId != null){
            try {
                UserInfoDto user = userService.infoUser(decodeId);
                if(user != null){
                    resultMap.put("message", okay);
                    resultMap.put("data", user);
                } else{
                    resultMap.put("message", fail);
                }
                status = HttpStatus.OK;
            } catch (Exception e){
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @GetMapping("/user/more") // 회원 추가 정보 조회
    public ResponseEntity<?> infoMore(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        String decodeId = checkToken(request, resultMap);
        if(decodeId != null) {
            try {
                UserMoreDTO user = userService.infoMore(decodeId);
                if(user != null){
                    resultMap.put("message", okay);
                    resultMap.put("data", user);
                } else{
                    resultMap.put("message", fail);
                }
                status = HttpStatus.OK;
            } catch (Exception e){
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
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

