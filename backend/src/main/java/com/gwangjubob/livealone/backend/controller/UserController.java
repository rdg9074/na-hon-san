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
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;
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
    private static Map<String, Object> resultMap;
    @Autowired
    UserController(UserService userService ,JwtService jwtService,MailService mailService){
        this.userService = userService;
        this.jwtService = jwtService;
        this.mailService = mailService;
    }
    @PostMapping("/user")
    public ResponseEntity<?> registUser(@RequestBody UserRegistDto userRegistDto) throws Exception{
        resultMap = new HashMap<>();
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

    @GetMapping("/user/check/{nickname}")
    public ResponseEntity<?> checkNickName(@PathVariable String nickname){
        resultMap = new HashMap<>();
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
    @PostMapping("/user/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDto userLoginDto, HttpServletRequest request, HttpServletResponse response) throws Exception{
        resultMap = new HashMap<>();
        try {
            userService.loginUser(userLoginDto); //로그인 서비스 호출
            String accessToken = jwtService.createAccessToken("id", userLoginDto.getId());
            String refreshToken = jwtService.createRefreshToken("id", userLoginDto.getId());
            resultMap.put("access-token", accessToken);
            resultMap.put("message", okay);
            // create a cookie

            Cookie refreshCookie = new Cookie("refresh-token",refreshToken);
            refreshCookie.setMaxAge(1*60*60);
            refreshCookie.setPath("/");
            refreshCookie.setHttpOnly(true);

            response.addCookie(refreshCookie);

            status = HttpStatus.OK;
        }catch (Exception e){
            status = HttpStatus.OK;
            resultMap.put("message",fail);
        }

        return new ResponseEntity<>(resultMap, status);
    }
    @GetMapping("/user/login")
    public  ResponseEntity<?> updateAccessToken(HttpServletRequest request){
        resultMap = new HashMap<>();
        String refreshToken = request.getHeader("Set-Cookie").split(";")[0].replace("refresh-token=","");
        String decodeId = jwtService.decodeToken(refreshToken);
        if(decodeId != null){
            String accessToken = jwtService.createAccessToken("id", decodeId);
            resultMap.put("access-token", accessToken);
            resultMap.put("message", okay);
            status = HttpStatus.OK;
        }else{
            status = HttpStatus.UNAUTHORIZED;
            resultMap.put("message", fail);
        }
        return new ResponseEntity<>(resultMap,status);
    }
    @PutMapping("/user/password")
    public ResponseEntity<?> updatePassword(@RequestBody UserLoginDto userLoginDto) throws Exception{
        resultMap = new HashMap<>();
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

    @PostMapping("/user/auth")
    public ResponseEntity<?> sendMail(@RequestBody MailSendDto mailSendDto) throws Exception {
        resultMap = new HashMap<>();
        try {
            if (mailService.emailCheck(mailSendDto)) { // 이메일 체크 함수 호출
                mailService.mailSend(mailSendDto); // 이메일 전송 - 비동기
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
    @GetMapping("/user/auth")
    public ResponseEntity<?> checkMail(@ModelAttribute("MailCheckDto") MailCheckDto mailCheckDto) throws Exception {
        resultMap = new HashMap<>();
        try {
            if (mailService.checkAuthNumber(mailCheckDto)) { // 인증번호 체크 서비스 호출
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
    @PutMapping("/user")
    public ResponseEntity<?> updateUser(@RequestBody UserInfoDto userInfoDto, HttpServletRequest request) throws Exception{
        resultMap = new HashMap<>();
        String decodeId = checkToken(request);
        if (decodeId != null){
            try {
                userInfoDto.setId(decodeId);
                UserInfoDto user = userService.updateUser(userInfoDto); //회원 수정 서비스 호출
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
    @PutMapping("/user/more")
    public ResponseEntity<?> moreUpdateUser(@RequestBody UserMoreDTO userMoreDTO, HttpServletRequest request){
        resultMap = new HashMap<>();
        String decodeId = checkToken(request);
        if (decodeId != null){
            try {
                userMoreDTO.setUserId(decodeId);
                userService.moreUpdate(userMoreDTO); //추가 정보 수정 서비스 호출
                resultMap.put("message", okay);
                status = HttpStatus.OK;
            } catch (Exception e){
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return new ResponseEntity<>(resultMap, status);
    }
    @DeleteMapping("/user")
    public ResponseEntity<?> deleteUser(HttpServletRequest request) throws Exception{
        resultMap = new HashMap<>();
        String decodeId = checkToken(request);
        if(decodeId != null){
            try {
                userService.userDelete(decodeId); // 회원 탈퇴 서비스 호출
                resultMap.put("message", okay);
                status = HttpStatus.OK;
            } catch (Exception e){
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return new ResponseEntity<>(resultMap, status);
    }
    @PostMapping("/user/password")
    public ResponseEntity<?> passwordCheckUser(@RequestBody UserLoginDto userLoginDto,HttpServletRequest request) {
        resultMap = new HashMap<>();
        String decodeId = checkToken(request);
        if (decodeId != null) {
            try {
                if (userService.passwordCheckUser(decodeId, userLoginDto.getPassword())) { //비밀번호 확인 서비스 호출
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
    @GetMapping("user")
    public ResponseEntity<?> infoUser(HttpServletRequest request) throws Exception{
        resultMap = new HashMap<>();
        String decodeId = checkToken(request);
        if (decodeId != null){
            try {
                UserInfoDto user = userService.infoUser(decodeId); //회원 조회 서비스 호출
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

    @GetMapping("/user/more")
    public ResponseEntity<?> infoMore(HttpServletRequest request){
        resultMap = new HashMap<>();
        String decodeId = checkToken(request);
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

    public String checkToken(HttpServletRequest request){
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

