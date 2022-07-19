package com.gwangjubob.livealone.backend.controller;

import com.gwangjubob.livealone.backend.dto.user.UserRegistDto;
import com.gwangjubob.livealone.backend.dto.user.UserUpdateDto;
import com.gwangjubob.livealone.backend.service.JwtService;
import com.gwangjubob.livealone.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
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
    private final UserService userService;
    private final JwtService jwtService;
    @Autowired
    UserController(UserService userService ,JwtService jwtService){
        this.userService = userService;
        this.jwtService = jwtService;
    }
    @PostMapping("/user")
    public ResponseEntity<?> registUser(@RequestBody UserRegistDto userRegistDto) throws Exception{
        boolean result = userService.registUser(userRegistDto);
        if(result){
            HttpStatus status = HttpStatus.ACCEPTED;
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
            status = HttpStatus.ACCEPTED;
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
                String accessToken = jwtService.createAccessToken("user_id", userLoginDto.getId());// key, data
                String refreshToken = jwtService.createRefreshToken("user_id", userLoginDto.getId());
                resultMap.put("access-token", accessToken);
//                resultMap.put("refresh-token", refreshToken);
                resultMap.put("message", "로그인 성공");
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
                resultMap.put("message","로그인 실패");
            }
            resultMap.put("message",okay);
            status = HttpStatus.ACCEPTED;
        }catch (Exception e){
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
    @DeleteMapping("user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) throws Exception{
        HttpStatus status;
        Map<String, Object> resultMap = new HashMap<>();
        try {
            userService.userDelete(id);
            resultMap.put("message", okay);
            status = HttpStatus.ACCEPTED;
        } catch (Exception e){
            resultMap.put("message", fail);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(resultMap, status);
    }

}
