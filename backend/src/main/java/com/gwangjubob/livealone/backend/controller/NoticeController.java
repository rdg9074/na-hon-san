package com.gwangjubob.livealone.backend.controller;

import com.gwangjubob.livealone.backend.dto.notice.NoticeViewDto;
import com.gwangjubob.livealone.backend.service.NoticeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class NoticeController {

    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";

    private NoticeService noticeService;

    @Autowired
    NoticeController(NoticeService noticeService){
        this.noticeService = noticeService;
    }

    // 알림 전체 조회
    @GetMapping("/user/notice/{id}") // accessToken 에서 가져올 것인지
    public ResponseEntity<?> viewNotice(@PathVariable String id) throws Exception {
        HttpStatus status;
        Map<String,Object> resultMap = new HashMap<>();
        List<NoticeViewDto> list;

        try{
            list = noticeService.viewNotice(id);
            resultMap.put("noticeList", list);
            resultMap.put("message", SUCCESS);
            status = HttpStatus.OK;

        }catch(Exception e){
            resultMap.put("message", FAIL);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(resultMap, status);
    }

    // 알림 삭제
    @DeleteMapping("/user/notice/{idx}")
    public ResponseEntity<?> deleteNotice(@PathVariable int idx) throws Exception{
        HttpStatus status;
        Map<String, Object> resultMap = new HashMap<>();
        try{
            noticeService.deleteNotice(idx);
            resultMap.put("message", SUCCESS);
            status = HttpStatus.OK;
        }catch (Exception e){
            resultMap.put("message", FAIL);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(resultMap, status);
    }
}
