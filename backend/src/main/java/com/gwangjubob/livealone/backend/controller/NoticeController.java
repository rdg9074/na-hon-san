package com.gwangjubob.livealone.backend.controller;

import com.gwangjubob.livealone.backend.domain.entity.NoticeEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import com.gwangjubob.livealone.backend.dto.notice.NoticeReadDto;
import com.gwangjubob.livealone.backend.dto.notice.NoticeViewDto;
import com.gwangjubob.livealone.backend.service.JwtService;
import com.gwangjubob.livealone.backend.service.NoticeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class NoticeController {

    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";

    private UserEntity userEntity;
    private NoticeService noticeService;
    private JwtService jwtService;

    @Autowired
    NoticeController(NoticeService noticeService, JwtService jwtService){
        this.noticeService = noticeService;
        this.jwtService = jwtService;
    }

    // 알림 전체 조회

    @GetMapping("/user/notice") // accessToken 에서 가져올 것인지
    public ResponseEntity<?> viewNotice(HttpServletRequest request) throws Exception {
        String accessToken = request.getHeader("access-token");
        String decodeId = jwtService.decodeToken(accessToken);

        HttpStatus status;
        Map<String,Object> resultMap = new HashMap<>();

        try{
            List<NoticeViewDto> list = noticeService.viewNotice(decodeId);
            resultMap.put("noticeList", list);
            resultMap.put("message", SUCCESS);
            status = HttpStatus.OK;

        }catch(Exception e){
            resultMap.put("message", FAIL);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(resultMap, status);
    }

    // 알림 읽음
    @PutMapping("/user/notice/{idx}")
    public ResponseEntity<?> readNotice(HttpServletRequest request, @PathVariable int idx) throws Exception{
        String accessToken = request.getHeader("access-token");
        String decodeId = jwtService.decodeToken(accessToken);

        HttpStatus status;
        Map<String, Object> resultMap = new HashMap<>();

        try{
            NoticeReadDto read = noticeService.readNotice(decodeId, idx);
            resultMap.put("message", SUCCESS);
            status = HttpStatus.OK;
        } catch (Exception e){
            resultMap.put("message", FAIL);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }

    // 알림 삭제
    @DeleteMapping("/user/notice/{idx}")
    public ResponseEntity<?> deleteNotice(HttpServletRequest request, @PathVariable int idx) throws Exception{
        String accessToken = request.getHeader("access-token");
        String decodeId = jwtService.decodeToken(accessToken);

        HttpStatus status;
        Map<String, Object> resultMap = new HashMap<>();

        try{
            noticeService.deleteNotice(decodeId, idx);
            resultMap.put("message", SUCCESS);
            status = HttpStatus.OK;
        }catch(Exception e){
            resultMap.put("message", FAIL);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }
}
