package com.gwangjubob.livealone.backend.controller;

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
    private UserService userService;
    @Autowired
    UserController(UserService userService){
        this.userService = userService;
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
}
