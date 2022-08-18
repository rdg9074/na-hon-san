package com.gwangjubob.livealone.backend.controller;

import com.gwangjubob.livealone.backend.dto.Deal.DealCommentDto;
import com.gwangjubob.livealone.backend.dto.Deal.DealDto;
import com.gwangjubob.livealone.backend.dto.Deal.DealRequestDto;
import com.gwangjubob.livealone.backend.service.DealService;
import com.gwangjubob.livealone.backend.service.JwtService;
import com.gwangjubob.livealone.backend.service.UserFeedService;
import com.gwangjubob.livealone.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class DealController {

    private static final String okay = "SUCCESS";
    private static final String fail = "FAIL";
    private static final String timeOut = "access-token timeout";

    private final JwtService jwtService;
    private final DealService dealService;
    private final UserFeedService userFeedService;
    private final UserService userService;
    private static HttpStatus status = HttpStatus.NOT_FOUND;
    private Map<String, Object> resultMap;

    @Autowired
    DealController(JwtService jwtService, DealService dealService, UserFeedService userFeedService, UserService userService)
    {
        this.jwtService = jwtService;
        this.dealService = dealService;
        this.userFeedService = userFeedService;
        this.userService = userService;
    }

    @PostMapping("/honeyDeal") //꿀딜 게시글 등록
    public ResponseEntity<?> registDeal(@RequestBody DealDto dealDto, HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        String decodeId = checkToken(request, resultMap);
        if (decodeId != null){
            try {
                dealDto.setUserId(decodeId);
                DealDto data = dealService.registDeal(dealDto);
                if(data != null){
                    resultMap.put("data", data);
                    resultMap.put("message", okay);
                } else{
                    resultMap.put("message", fail);
                }
                status = HttpStatus.OK;
            } catch (Exception e){
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return new ResponseEntity<>(resultMap, status);
    }
    @GetMapping("/honeyDeal/detail/{idx}") //꿀딜 게시글 상세 조회
    public ResponseEntity<?> viewDetailDeal(@PathVariable Integer idx, HttpServletRequest request, HttpServletResponse response){
        Map<String, Object> resultMap = new HashMap<>();
        String decodeId = "isLogin";
        if(request != null && request.getHeader("Authorization") != null){
            decodeId = checkToken(request, resultMap);
        }
        if(decodeId != null){
            try {
                if(!decodeId.equals("isLogin")){
                    resultMap.put("isLike", dealService.clickLikeButton(decodeId, idx));
                    resultMap.put("isFollow", userFeedService.checkFollowDeal(decodeId, idx));
                }
                DealDto dto = dealService.viewDetailDeal(idx);
                Cookie oldCookie = null;
                Cookie[] cookies = request.getCookies();
                if(cookies != null){
                    for (Cookie cookie : cookies){
                        if(cookie.getName().equals("postDeal")){
                            oldCookie = cookie;
                        }
                    }
                }
                if(oldCookie != null){
                    if(!oldCookie.getValue().contains("[" + idx + "]")){
                        boolean upCheck = dealService.countUpView(idx);
                        if (upCheck){
                            oldCookie.setValue(oldCookie.getValue() + "[" + idx + "]");
                            oldCookie.setPath("/");
                            oldCookie.setMaxAge(60 * 60 * 24);
                            response.addCookie(oldCookie);
                        }
                    }
                } else{
                    dealService.countUpView(idx);
                    Cookie newCookie = new Cookie("postDeal", "["+ idx + "]");
                    newCookie.setPath("/");
                    newCookie.setMaxAge(60 * 60 * 24);
                    response.addCookie(newCookie);
                }
                if(dto != null){
                    resultMap.put("deal", dto);
                    List<DealCommentDto> list = dealService.viewDealComment(idx);
                    resultMap.put("dealComments", list);
                    resultMap.put("message", okay);
                } else{
                    resultMap.put("message", fail);
                }
                status = HttpStatus.OK;
            } catch (Exception e) {
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @PutMapping("/honeyDeal/{idx}") //꿀딜 게시글 수정
    public ResponseEntity<?> updateDeal(@PathVariable Integer idx, @RequestBody DealDto dealDto, HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        String decodeId = checkToken(request, resultMap);
        if (decodeId != null){
            try {
                DealDto data = dealService.updateDeal(idx, dealDto, decodeId);
                if(data != null){
                    resultMap.put("data", data);
                    resultMap.put("message", okay);
                } else{
                    resultMap.put("message", fail);
                }
                status = HttpStatus.OK;
            } catch (Exception e){
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @DeleteMapping("/honeyDeal/{idx}") //꿀딜 게시글 삭제
    public ResponseEntity<?> deleteDeal(@PathVariable Integer idx, HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        String decodeId = checkToken(request, resultMap);
        if (decodeId != null){
            try {
                if(dealService.deleteDeal(idx, decodeId)){
                    resultMap.put("message", okay);
                } else{
                    resultMap.put("message", fail);
                }
                status = HttpStatus.OK;
            } catch (Exception e){
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @PostMapping("/honeyDeal/comment") //꿀딜 댓글 등록
    public ResponseEntity<?> registDealComment(@RequestBody DealCommentDto dealCommentDto, HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        String decodeId = checkToken(request, resultMap);
        if(decodeId != null){
            try {
                dealCommentDto.setUserId(decodeId);
                DealCommentDto data = dealService.registDealComment(dealCommentDto);
                if(data != null){
                    resultMap.put("data", data);
                    resultMap.put("message", okay);
                } else{
                    resultMap.put("message", fail);
                }
                status = HttpStatus.OK;
            } catch (Exception e){
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @PutMapping("/honeyDeal/comment/{idx}") //꿀딜 댓글 수정
    public ResponseEntity<?> updateDealComment(@PathVariable Integer idx, @RequestBody DealCommentDto dealCommentDto, HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        String decodeId = checkToken(request, resultMap);
        if (decodeId != null){
            try {
                dealCommentDto.setUserId(decodeId);
                DealCommentDto data = dealService.updateDealComment(idx, dealCommentDto);
                if(data != null){
                    resultMap.put("data", data);
                    resultMap.put("message", okay);
                } else{
                    resultMap.put("message", fail);
                }
                status = HttpStatus.OK;
            } catch (Exception e){
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return new ResponseEntity<>(resultMap, status);
    }
    @DeleteMapping("/honeyDeal/comment/{idx}") //꿀딜 댓글 삭제
    public ResponseEntity<?> deleteDealComment(HttpServletRequest request, @PathVariable Integer idx){
        Map<String, Object> resultMap = new HashMap<>();
        String decodeId = checkToken(request, resultMap);
        if(decodeId != null){
            try {
                dealService.deleteDealComment(idx, decodeId);
                resultMap.put("message", okay);
                status = HttpStatus.OK;
            } catch (Exception e){
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @GetMapping("/honeyDeal/like/{idx}") //꿀딜 게시글 좋아요, 좋아요 취소
    public ResponseEntity<?> likeDeal(@PathVariable Integer idx, HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        String decodeId = checkToken(request, resultMap);
        if(decodeId != null){
            try {
                if(dealService.likeDeal(idx, decodeId)){
                    resultMap.put("message", okay);
                } else{
                    resultMap.put("message", fail);
                }
                status = HttpStatus.OK;
            } catch (Exception e){
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return new ResponseEntity<>(resultMap, status);
    }
    @PostMapping("/honeyDeal/view") //꿀딜 게시글 조회
    public ResponseEntity<?> viewDeal(@RequestBody DealRequestDto dealRequestDto){
        Map<String, Object> resultMap = new HashMap<>();
        try{
            Map<String, Object> data = dealService.viewDeal(dealRequestDto);
            if(data != null){
                resultMap.put("data", data.get("list"));
                resultMap.put("hasNext", data.get("hasNext"));
                resultMap.put("message", okay);
            } else{
                resultMap.put("message", fail);
            }
            status = HttpStatus.OK;
        } catch (Exception e){
            resultMap.put("message", fail);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @GetMapping("/honeyDeal/count/{area}")//지역 거래 건수 조회
    public ResponseEntity<?> countArea(@PathVariable String area){
        Map<String, Object> resultMap = new HashMap<>();
        try {
            long cnt = dealService.countArea(area);
            resultMap.put("countArea", cnt);
            resultMap.put("message", okay);
            status = HttpStatus.OK;
        } catch (Exception e){
            resultMap.put("message", fail);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @GetMapping("/honeyDeal/position/{nickname}") // 꿀딜 좌표 조회
    public ResponseEntity<?> getPosition(HttpServletRequest request, @PathVariable String nickname){
        Map<String, Object> resultMap = new HashMap<>();
        String loginUserId = checkToken(request, resultMap);
        String targetUserId = userService.getTargetId(nickname);
        int apiCount = dealService.getApiCount();
        System.out.println(apiCount);
        if(loginUserId != null) {
            try {

                if(apiCount > 985){
                    resultMap.put("message", "too many API Usage");
                    status = HttpStatus.OK;
                }else{
                    // 사용자 위치 구하는 서비스 호출
                    resultMap.put("loginUserPosition", userService.getPosition(loginUserId));
                    resultMap.put("targetUserPosition", userService.getPosition(targetUserId));

                    if(userService.getPosition(loginUserId).get("positionX")==null){
                        resultMap.put("message","loginUserPositionNotFound");
                    }else if(userService.getPosition(targetUserId).get("positionX")==null){
                        resultMap.put("message","targetUserPositionNotFound");
                    }else{
                        resultMap.put("midPositionInfo",dealService.searchMidPosition(loginUserId,targetUserId));
                        resultMap.put("message",okay);
                    }
                    status = HttpStatus.OK;
                }
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
