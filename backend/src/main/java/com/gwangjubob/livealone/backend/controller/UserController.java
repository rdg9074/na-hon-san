package com.gwangjubob.livealone.backend.controller;

import com.gwangjubob.livealone.backend.dto.user.UserRegistDto;
import com.gwangjubob.livealone.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.gwangjubob.livealone.backend.dto.user.UserLoginDto;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    private static final String okay = "SUCCESS";
    private static final String fail = "FAIL";
    private UserService userService;
    @Autowired
    UserController(UserService userService){
        this.userService = userService;
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
    public ResponseEntity<?> refreshToken(@RequestBody UserLoginDto userLoginDto, HttpServletRequest request) throws Exception{
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;
        try {
            if(userService.loginUser(userLoginDto) == true){
                resultMap.put("result","로그인 성공");
            }else {
                resultMap.put("userId","비밀번호 틀림");
            }
            resultMap.put("message",okay);

            status = HttpStatus.ACCEPTED;
        }catch (Exception e){
            status = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<>(resultMap, status);
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
