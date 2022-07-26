package com.gwangjubob.livealone.backend.controller;

import com.gwangjubob.livealone.backend.dto.tip.TipCreateDto;
import com.gwangjubob.livealone.backend.dto.tip.TipViewDto;
import com.gwangjubob.livealone.backend.dto.tipcomment.TipCommentCreateDto;
import com.gwangjubob.livealone.backend.service.JwtService;
import com.gwangjubob.livealone.backend.service.TipCommentService;
import com.gwangjubob.livealone.backend.service.TipService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TipController {
    private TipService tipService;
    private JwtService jwtService;
    private TipCommentService tipCommentService;

    private static final String okay = "SUCCESS";
    private static final String fail = "FAIL";
    private static final String timeOut = "access-token timeout";
    private static HttpStatus status = HttpStatus.NOT_FOUND;
    private static Map<String, Object> resultMap;

    public TipController(TipService tipService, JwtService jwtService, TipCommentService tipCommentService){
        this.tipService = tipService;
        this.jwtService = jwtService;
        this.tipCommentService = tipCommentService;
    }

    @PostMapping("/honeyTip")
    public ResponseEntity<?> createTip(HttpServletRequest request, @RequestBody TipCreateDto tipCreateDto){
        resultMap = new HashMap<>();
        String decodeId = checkToken(request);

        if(!decodeId.equals("timeout")){
            try{
                tipService.createTip(decodeId, tipCreateDto); // 꿀팁 게시글 작성 서비스 호출
                resultMap.put("message", okay);
                status = HttpStatus.OK;
            }catch(Exception e){
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @PostMapping("/honeyTip/comment")
    public ResponseEntity<?> createTipComment(HttpServletRequest request, @RequestBody TipCommentCreateDto tipCommentCreateDto){
        resultMap = new HashMap<>();
        String decodeId = checkToken(request);

        if(!decodeId.equals("timeout")){
            try{
                tipCommentService.createTipComment(decodeId, tipCommentCreateDto); // 꿀팁 댓글 작성 서비스 호출
                resultMap.put("message", okay);
                status = HttpStatus.OK;
            }catch (Exception e){
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @GetMapping("/honeyTip/{category}")
    public ResponseEntity<?> viewTip(@PathVariable String category){
        resultMap = new HashMap<>();

        try{
            List<TipViewDto> list = tipService.viewTip(category); // 카테고리별 게시글 목록 조회
            resultMap.put("data", list);
            resultMap.put("message", okay);
            status = HttpStatus.OK;
        }catch (Exception e){
            resultMap.put("message", fail);
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
