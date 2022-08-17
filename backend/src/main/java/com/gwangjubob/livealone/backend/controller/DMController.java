package com.gwangjubob.livealone.backend.controller;

import com.gwangjubob.livealone.backend.dto.dm.DMSendDto;
import com.gwangjubob.livealone.backend.dto.dm.DMViewDto;
import com.gwangjubob.livealone.backend.service.DMService;
import com.gwangjubob.livealone.backend.service.JwtService;
import com.gwangjubob.livealone.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    private final DMService dmService;
    DMController(UserService userService,JwtService jwtService,DMService dmService){
        this.userService = userService;
        this.jwtService = jwtService;
        this.dmService = dmService;
    }
    @PostMapping("/dm") // DM 메시지 전송
    public ResponseEntity<?> sendDM(@RequestBody DMSendDto dmSendDto, HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        String decodeId = checkToken(request, resultMap);
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
    @GetMapping("/dm") // DM 리스트 조회
    public ResponseEntity<?> listDM(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        String decodeId = checkToken(request, resultMap);
        if(decodeId != null){
            try {
                List<DMViewDto> dmViewDtoList =dmService.listDM(decodeId);
                resultMap.put("message",okay);
                resultMap.put("data",dmViewDtoList);
                status = HttpStatus.OK;
            }catch (Exception e){
                resultMap.put("message",fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return new ResponseEntity<>(resultMap, status);
    }
    @GetMapping("/dm/{fromNickname}") // DM 세부조회
    public ResponseEntity<?> listDetailDM(@PathVariable("fromNickname")String fromNickname, @RequestParam("lastIdx") int lastIdx, @RequestParam("pageSize") int pageSize, HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        String fromId = userService.NicknameToId(fromNickname);
        String decodeId = checkToken(request, resultMap);
        try {
            if(decodeId != null){
                Map dmViewDtoList =dmService.listDetailDM(decodeId,fromId,lastIdx,pageSize);
                resultMap.put("message",okay);
                resultMap.put("data",dmViewDtoList);
                status = HttpStatus.OK;
            }
        }catch (Exception e){
            resultMap.put("message",fail);
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
