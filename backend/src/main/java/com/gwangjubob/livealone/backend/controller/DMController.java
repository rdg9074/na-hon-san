package com.gwangjubob.livealone.backend.controller;

import com.gwangjubob.livealone.backend.dto.dm.DMSendDto;
import com.gwangjubob.livealone.backend.dto.user.UserLoginDto;
import com.gwangjubob.livealone.backend.service.DMService;
import com.gwangjubob.livealone.backend.service.JwtService;
import com.gwangjubob.livealone.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DMController {
    private static final String okay = "SUCCESS";
    private static final String fail = "FAIL";

    private static HttpStatus status = HttpStatus.NOT_FOUND;
    private static Map<String, Object> resultMap;
    private final UserService userService;
    private final JwtService jwtService;
    private final DMService dmService;
    DMController(UserService userService,JwtService jwtService,DMService dmService){
        this.userService = userService;
        this.jwtService = jwtService;
        this.dmService = dmService;
    }
    @PostMapping("/dm")
    public ResponseEntity<?> sendDM(@RequestBody DMSendDto dmSendDto, HttpServletRequest request) throws Exception{
        String accessToken = request.getHeader("access-token");
        String decodeId = jwtService.decodeToken(accessToken);
        resultMap = new HashMap<>();
        dmSendDto.setFromId(decodeId);
        try {
            if(!decodeId.equals("timeout") && dmService.sendDM(dmSendDto)){
                resultMap.put("message",okay);
            }else {
                resultMap.put("message",fail);
            }
            status = HttpStatus.OK;
        }catch (Exception e){
            status = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<>(resultMap, status);
    }
}
