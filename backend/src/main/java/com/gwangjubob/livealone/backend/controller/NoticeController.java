package com.gwangjubob.livealone.backend.controller;

import com.gwangjubob.livealone.backend.dto.notice.NoticeViewDto;
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

    private HttpStatus status = HttpStatus.NOT_FOUND;
    private Map<String, Object> resultMap;

    @Autowired
    NoticeController(NoticeService noticeService, JwtService jwtService){
        this.noticeService = noticeService;
        this.jwtService = jwtService;
    }

    @GetMapping("/user/notice")
    public ResponseEntity<?> viewNotice(HttpServletRequest request) {
        resultMap = new HashMap<>();
        String decodeId = checkToken(request);

        if(!decodeId.equals("timeout")){
            try{
                List<NoticeViewDto> list = noticeService.viewNotice(decodeId); // 알림 목록 조회 서비스 호출
                resultMap.put("noticeList", list);
                resultMap.put("message", okay);
                status = HttpStatus.OK;
            }catch(Exception e){
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @GetMapping("/user/notice/count")
    public ResponseEntity<?> countNotice(HttpServletRequest request) {
        resultMap = new HashMap<>();
        String decodeId = checkToken(request);

        if (decodeId != null) {
            try {
                long count = noticeService.countNotice(decodeId); // 읽지 않은 알림 개수 조회 서비스 호출
                resultMap.put("count", count);
                resultMap.put("message", okay);
                status = HttpStatus.OK;
            } catch (Exception e) {
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @PutMapping("/user/notice/{idx}")
    public ResponseEntity<?> readNotice(HttpServletRequest request, @PathVariable int idx) {
        resultMap = new HashMap<>();
        String decodeId = checkToken(request);

        if(decodeId != null){
            try{
                boolean result = noticeService.readNotice(decodeId, idx); // 알림 읽음 처리 서비스 호출
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

    @DeleteMapping("/user/notice/{idx}")
    public ResponseEntity<?> deleteNotice(HttpServletRequest request, @PathVariable int idx) {
        resultMap = new HashMap<>();
        String decodeId = checkToken(request);

        if(decodeId != null) {
            try {
                noticeService.deleteNotice(decodeId, idx); // 알림 삭제 서비스 호출
                resultMap.put("message", okay);
                status = HttpStatus.OK;
            } catch (Exception e) {
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return new ResponseEntity<>(resultMap, status);
    }

        public String checkToken(HttpServletRequest request){
            String accessToken = request.getHeader("access-token");
            String decodeId = jwtService.decodeToken(accessToken);
            if(!decodeId.equals("timeout")){
                return decodeId;
            }else{
                resultMap.put("message", timeOut);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                return null;
            }
        }
}
