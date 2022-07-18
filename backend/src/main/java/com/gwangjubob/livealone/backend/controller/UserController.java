package com.gwangjubob.livealone.backend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    private static final String okay = "SUCCESS";
    @PostMapping("/user/login")
    public ResponseEntity<?> refreshToken(@RequestParam("userId")String userId, HttpServletRequest request) throws Exception{
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;
        try {
            resultMap.put("message",okay);
            resultMap.put("userId",userId);
            status = HttpStatus.ACCEPTED;
        }catch (Exception e){
            status = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<>(resultMap, status);
    }
}
