package com.gwangjubob.livealone.backend.controller;

import com.gwangjubob.livealone.backend.dto.notice.NoticeViewDto;
import com.gwangjubob.livealone.backend.service.DMService;
import com.gwangjubob.livealone.backend.service.JwtService;
import com.gwangjubob.livealone.backend.service.NoticeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class NoticeController {

    private static final String okay = "SUCCESS";
    private static final String fail = "FAIL";
    private static final String timeOut = "access-token timeout";

    private NoticeService noticeService;
    private JwtService jwtService;
    private DMService dmService;

    private HttpStatus status = HttpStatus.NOT_FOUND;

    @Autowired
    NoticeController(NoticeService noticeService, DMService dmService,JwtService jwtService){
        this.noticeService = noticeService;
        this.jwtService = jwtService;
        this.dmService = dmService;
    }

    @GetMapping("/user/notice") //알림 조회
    public ResponseEntity<?> viewNotice(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        String decodeId = checkToken(request, resultMap);

        if(decodeId != null){
            try{
                List<NoticeViewDto> list = noticeService.viewNotice(decodeId); // 알림 목록 조회 서비스 호출
                resultMap.put("data", list);
                resultMap.put("message", okay);
                status = HttpStatus.OK;
            }catch(Exception e){
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @GetMapping("/user/notice/count") //알림 개수 조회
    public ResponseEntity<?> countNotice(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        String decodeId = checkToken(request, resultMap);

        if (decodeId != null) {
            try {
                long countNotice = noticeService.countNotice(decodeId);
                long countDM = dmService.countDM(decodeId);
                resultMap.put("countNotice", countNotice);
                resultMap.put("countDM", countDM);
                resultMap.put("message", okay);
                status = HttpStatus.OK;
            } catch (Exception e) {
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @PutMapping("/user/notice/{idx}") //알림 읽음 처리
    public ResponseEntity<?> readNotice(HttpServletRequest request, @PathVariable int idx) {
        Map<String, Object> resultMap = new HashMap<>();
        String decodeId = checkToken(request, resultMap);

        if(decodeId != null){
            try{
                boolean result = noticeService.readNotice(decodeId, idx);
                if(result){
                    resultMap.put("message", okay);
                    status = HttpStatus.OK;
                }else{
                    resultMap.put("message", fail);
                    status = HttpStatus.NO_CONTENT;
                }
            } catch (Exception e){
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @DeleteMapping("/user/notice/{idx}") //알림 삭제
    public ResponseEntity<?> deleteNotice(HttpServletRequest request, @PathVariable int idx) {
        Map<String, Object> resultMap = new HashMap<>();
        String decodeId = checkToken(request, resultMap);

        if(decodeId != null) {
            try {
                noticeService.deleteNotice(decodeId, idx);
                resultMap.put("message", okay);
                status = HttpStatus.OK;
            } catch (Exception e) {
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
