package com.gwangjubob.livealone.backend.controller;

import com.gwangjubob.livealone.backend.dto.dm.DMSendDto;
import com.gwangjubob.livealone.backend.dto.dm.DMViewDto;
import com.gwangjubob.livealone.backend.dto.dm.DMViewDto;
import com.gwangjubob.livealone.backend.dto.user.UserLoginDto;
import com.gwangjubob.livealone.backend.service.DMService;
import com.gwangjubob.livealone.backend.service.JwtService;
import com.gwangjubob.livealone.backend.service.UserService;
import com.gwangjubob.livealone.backend.service.impl.MailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class DMController {
    private static final String okay = "SUCCESS";
    private static final String fail = "FAIL";

    private static final String timeOut = "access-token timeout";
    private final UserService userService;
    private final JwtService jwtService;
    private static HttpStatus status = HttpStatus.NOT_FOUND;
    private static Map<String, Object> resultMap;

    private final DMService dmService;
    DMController(UserService userService,JwtService jwtService,DMService dmService){
        this.userService = userService;
        this.jwtService = jwtService;
        this.dmService = dmService;
    }
    @PostMapping("/dm")
    public ResponseEntity<?> sendDM(@RequestBody DMSendDto dmSendDto, HttpServletRequest request){
        resultMap = new HashMap<>();
        String decodeId = checkToken(request);
        try {
            if(decodeId != null){
                dmSendDto.setFromId(decodeId);
                if(dmService.sendDM(dmSendDto)) {
                    resultMap.put("message", okay);
                }else {
                    resultMap.put("message",fail);
                }
                status = HttpStatus.OK;
            }
        }catch (Exception e){
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }
    @GetMapping("/dm")
    public ResponseEntity<?> listDM(HttpServletRequest request){
        resultMap = new HashMap<>();
        String decodeId = checkToken(request);
        try {
            if(decodeId != null){
                System.out.println(decodeId);
                List<DMViewDto> dmViewDtoList =dmService.listDM(decodeId);
                resultMap.put("message",okay);
                resultMap.put("data",dmViewDtoList);
                status = HttpStatus.OK;
            }
        }catch (Exception e){
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }
    @GetMapping("/dm/{fromId}")
    public ResponseEntity<?> listDetailDM(@PathVariable("fromId")String fromId, HttpServletRequest request){
        resultMap = new HashMap<>();
        String decodeId = checkToken(request);
        try {
            if(decodeId != null){
                List<DMViewDto> dmViewDtoList =dmService.listDetailDM(decodeId,fromId);
                resultMap.put("message",okay);
                resultMap.put("data",dmViewDtoList);
                status = HttpStatus.OK;
            }
        }catch (Exception e){
            status = HttpStatus.INTERNAL_SERVER_ERROR;
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
