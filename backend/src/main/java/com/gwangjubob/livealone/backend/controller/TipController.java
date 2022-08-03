package com.gwangjubob.livealone.backend.controller;

import com.gwangjubob.livealone.backend.dto.tip.*;
import com.gwangjubob.livealone.backend.dto.tipcomment.TipCommentCreateDto;
import com.gwangjubob.livealone.backend.dto.tipcomment.TipCommentUpdateDto;
import com.gwangjubob.livealone.backend.dto.tipcomment.TipCommentViewDto;
import com.gwangjubob.livealone.backend.service.JwtService;
import com.gwangjubob.livealone.backend.service.TipCommentService;
import com.gwangjubob.livealone.backend.service.TipService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
                int postIdx = tipService.createTip(decodeId, tipCreateDto); // 꿀팁 게시글 작성 서비스 호출
                resultMap.put("postIdx", postIdx);
                resultMap.put("message", okay);
                status = HttpStatus.OK;
            }catch(Exception e){
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @PostMapping("/honeyTip/list")
    public ResponseEntity<?> viewTip(@RequestBody TipListDto tipListDto){
        resultMap = new HashMap<>();

        try{
            Map<String, Object> result = tipService.viewTip(tipListDto); // 카테고리별 게시글 목록 조회
            resultMap.put("data", result.get("list"));
            resultMap.put("hasNext", result.get("hasNext"));
            resultMap.put("message", okay);
            status = HttpStatus.OK;
        }catch (Exception e){
            resultMap.put("message", fail);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @GetMapping("honeyTip/totalCount")
    public ResponseEntity<?> totalCount(){
        resultMap = new HashMap<>();
        try{
            long totalCount = tipService.getTotalCount();
            resultMap.put("total",totalCount);
            resultMap.put("message", okay);
            status = HttpStatus.OK;
        }catch (Exception e){
            resultMap.put("message", fail);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(resultMap, status);
    }
    @GetMapping("/honeyTip/detail/{idx}")
    public ResponseEntity<?> detailViewTip(@PathVariable Integer idx, HttpServletRequest request, HttpServletResponse response){
        resultMap = new HashMap<>();

        try{
            TipDetailViewDto dto = tipService.detailViewTip(idx); // 게시글 세부 조회 서비스 호출
            Cookie oldCookie = null;
            Cookie[] cookies = request.getCookies();
            if(cookies != null){
                for (Cookie cookie : cookies){
                    if(cookie.getName().equals("postTip")){
                        oldCookie = cookie;
                    }
                }
            }
            if (oldCookie != null){
                if (!oldCookie.getValue().contains("[" + idx + "]")){
                    boolean upCheck = tipService.countUpView(idx);
                    if(upCheck){
                        oldCookie.setValue(oldCookie.getValue() + "[" + idx + "]");
                        oldCookie.setPath("/");
                        oldCookie.setMaxAge(60 * 60 * 24);
                        response.addCookie(oldCookie);
                    }
                }
            } else{
                tipService.countUpView(idx);
                Cookie newCookie = new Cookie("postTip", "["+ idx + "]");
                newCookie.setPath("/");
                newCookie.setMaxAge(60 * 60 * 24);
                response.addCookie(newCookie);
            }
            resultMap.put("tip",dto);
            List<TipCommentViewDto> list = tipCommentService.viewTipComment(idx); // 게시글 관련 댓글 조회 서비스 호출
            resultMap.put("tipComments", list);
            resultMap.put("message", okay);
            status = HttpStatus.OK;
        }catch (Exception e){
            resultMap.put("message", fail);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @PutMapping("/honeyTip/{idx}")
    public ResponseEntity<?> updateTip(HttpServletRequest request,@PathVariable Integer idx, @RequestBody TipUpdateDto tipUpdateDto){
        resultMap = new HashMap<>();
        String decodeId = checkToken(request);

        if(!decodeId.equals("timeout")){
            try{
                tipService.updateTip(decodeId, tipUpdateDto, idx); // 게시글 정보 수정 서비스 호출
                resultMap.put("message",okay);
                status = HttpStatus.OK;
            }catch(Exception e){
                resultMap.put("messaeg",fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }

         return new ResponseEntity<>(resultMap, status);
    }

    @DeleteMapping("/honeyTip/{idx}")
    public ResponseEntity<?> daleteTip(HttpServletRequest request, @PathVariable Integer idx){
        resultMap = new HashMap<>();
        String decodeId = checkToken(request);

        if(!decodeId.equals("timeout")){
            try{
                tipService.deleteTip(decodeId, idx); // 게시글 삭제 서비스 호출
                resultMap.put("message", okay);
                status = HttpStatus.OK;
            }catch (Exception e){
                resultMap.put("message",fail);
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

    @PutMapping("/honeyTip/comment/{idx}")
    public ResponseEntity<?> updateTipComment(HttpServletRequest request,@PathVariable Integer idx, @RequestBody TipCommentUpdateDto tipCommentUpdateDto){
        resultMap = new HashMap<>();
        String decodeId = checkToken(request);

        if(!decodeId.equals("timeout")){
            try{
                tipCommentService.updateTipComment(decodeId, idx, tipCommentUpdateDto); // 댓글 수정 서비스 호출
                resultMap.put("message", okay);
                status = HttpStatus.OK;
            }catch (Exception e){
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @DeleteMapping("honeyTip/comment/{idx}")
    public ResponseEntity<?> deleteTipComment(HttpServletRequest request, @PathVariable Integer idx){
        resultMap = new HashMap<>();
        String decodeId = checkToken(request);

        if(!decodeId.equals("timeout")){
            try{
                tipCommentService.deleteTipComment(decodeId, idx);
                resultMap.put("message", okay);
                status = HttpStatus.OK;
            }catch (Exception e){
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @GetMapping("honeyTip/like/{idx}")
    public ResponseEntity<?> likeTip(HttpServletRequest request, @PathVariable Integer idx){
        resultMap = new HashMap<>();
        String decodeId = checkToken(request);

        if(!decodeId.equals("timeout")){
            try{
                tipService.likeTip(decodeId, idx);
                resultMap.put("message", okay);
                status = HttpStatus.OK;
            }catch (Exception e){
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
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
