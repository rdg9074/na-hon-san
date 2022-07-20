package com.gwangjubob.livealone.backend.controller;

import com.gwangjubob.livealone.backend.dto.mail.MailCheckDto;
import com.gwangjubob.livealone.backend.dto.mail.MailSendDto;
import com.gwangjubob.livealone.backend.dto.user.UserRegistDto;
import com.gwangjubob.livealone.backend.dto.user.UserUpdateDto;
import com.gwangjubob.livealone.backend.service.JwtService;
import com.gwangjubob.livealone.backend.service.impl.MailService;
import com.gwangjubob.livealone.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.gwangjubob.livealone.backend.dto.user.UserLoginDto;

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
    @Autowired
    UserController(UserService userService ,JwtService jwtService,MailService mailService){
        this.userService = userService;
        this.jwtService = jwtService;
        this.mailService = mailService;
    }
    @PostMapping("/user")
    public ResponseEntity<?> registUser(@RequestBody UserRegistDto userRegistDto) throws Exception{
        boolean result = userService.registUser(userRegistDto);
        if(result){
            HttpStatus status = HttpStatus.OK;
            return new ResponseEntity<>(okay, status);
        }else {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            return new ResponseEntity<>(fail, status);
        }
    }

    @GetMapping("/user/check/{nickname}")
    public ResponseEntity<?> checkNickName(@PathVariable String nickname){
        HttpStatus status;
        Map<String, Object> resultMap = new HashMap<>();
        try {
            if(userService.checkNickName(nickname) == true){
                resultMap.put("message", fail);
            } else{
                resultMap.put("message", okay);
            }
            status = HttpStatus.OK;
        }catch (Exception e){
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(resultMap, status);
    }
    @PostMapping("/user/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDto userLoginDto, HttpServletRequest request, HttpServletResponse response) throws Exception{
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;
        try {
            if(userService.loginUser(userLoginDto) == true){
                String accessToken = jwtService.createAccessToken("id", userLoginDto.getId());// key, data
                String refreshToken = jwtService.createRefreshToken("id", userLoginDto.getId());
                resultMap.put("access-token", accessToken);
                resultMap.put("message", okay);
                // create a cookie
                ResponseCookie cookie = ResponseCookie.from("refresh-token",refreshToken)
                        .maxAge(7 * 24 * 60 * 60)
                        .path("/")
                        .secure(true)
                        .sameSite("None")
                        .httpOnly(true)
                        .build();
                response.setHeader("Set-Cookie",cookie.toString());
            }else {
                resultMap.put("message",fail);
            }
            status = HttpStatus.OK;
        }catch (Exception e){
            status = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<>(resultMap, status);
    }
    @PutMapping("/user/password")
    public ResponseEntity<?> updatePassword(@RequestBody UserLoginDto userLoginDto) throws Exception{
        HttpStatus status;
        Map<String, Object> resultMap = new HashMap<>();
        try {
            boolean res = userService.updatePassword(userLoginDto);
            if(res){
                status = HttpStatus.OK;
                resultMap.put("message", okay);
            } else{
                status = HttpStatus.NO_CONTENT;
                resultMap.put("message", fail);
            }

            return new ResponseEntity<>(resultMap, status);
        } catch(Exception e){
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            return new ResponseEntity<>(resultMap, status);
        }
    }



    @PostMapping("/user/auth")
    public ResponseEntity<?> sendMail(@RequestBody MailSendDto mailSendDto) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;
        System.out.println("test");
        try {
            if (mailService.sendMail(mailSendDto) == true) {
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
    @GetMapping("/user/auth")
    public ResponseEntity<?> checkMail(@ModelAttribute("MailCheckDto") MailCheckDto mailCheckDto) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;
        try {
            if (mailService.checkAuthNumber(mailCheckDto) == true) {
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
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateDto userUpdateDto) throws Exception{
        HttpStatus status;
        Map<String, Object> resultMap = new HashMap<>();
        try {
            UserUpdateDto user = userService.updateUser(userUpdateDto);
            status = HttpStatus.ACCEPTED;
            return new ResponseEntity<>(user, status);

        } catch (Exception e){
            resultMap.put("message", fail);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            return new ResponseEntity<>(resultMap, status);
        }

    }
    @DeleteMapping("/user")
    public ResponseEntity<?> deleteUser(HttpServletRequest request) throws Exception{
        String accessToken = request.getHeader("access-token");
        String decodeId = jwtService.decodeToken(accessToken);
        HttpStatus status;
        Map<String, Object> resultMap = new HashMap<>();
        if(!decodeId.equals("timeout")){
            try {
                userService.userDelete(decodeId);
                resultMap.put("message", okay);
                status = HttpStatus.OK;
            } catch (Exception e){
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }else{
            resultMap.put("message", timeOut);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(resultMap, status);
    }
    @PostMapping("/user/password")
    public ResponseEntity<?> passwordCheckUser(@RequestBody UserLoginDto userLoginDto,HttpServletRequest request) throws Exception{
        String accessToken = request.getHeader("access-token");
        String decodeId = jwtService.decodeToken(accessToken);
        HttpStatus status;
        Map<String, Object> resultMap = new HashMap<>();
        if(!decodeId.equals("timeout")){
            try {
                if(userService.passwordCheckUser(decodeId, userLoginDto.getPassword())){
                    resultMap.put("message", okay);
                }else{
                    resultMap.put("message", fail);
                }
                    status = HttpStatus.OK;
            } catch (Exception e){
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }else{
            resultMap.put("message", timeOut);
=======
>>>>>>> backend/src/main/java/com/gwangjubob/livealone/backend/controller/UserController.java
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(resultMap, status);
    }
}

