package com.gwangjubob.livealone.backend.controller;


import com.gwangjubob.livealone.backend.dto.Deal.DealDto;
import com.gwangjubob.livealone.backend.dto.feed.FollowViewDto;
import com.gwangjubob.livealone.backend.dto.feed.PopularFollowDto;
import com.gwangjubob.livealone.backend.dto.feed.PostViewDto;
import com.gwangjubob.livealone.backend.dto.feed.ProfileViewDto;
import com.gwangjubob.livealone.backend.dto.user.UserInfoDto;
import com.gwangjubob.livealone.backend.service.JwtService;
import com.gwangjubob.livealone.backend.service.UserFeedService;
import com.gwangjubob.livealone.backend.service.UserService;
import com.gwangjubob.livealone.backend.service.impl.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class FeedController {
    private static final String okay = "SUCCESS";
    private static final String fail = "FAIL";
    private static final String timeOut = "access-token timeout";
    private static final String notAllowed = "notAllowed";
    private final UserService userService;
    private final JwtService jwtService;
    private final MailService mailService;
    private static HttpStatus status = HttpStatus.NOT_FOUND;
    private static Map<String, Object> resultMap;
    private final UserFeedService userFeedService;
    @Autowired
    FeedController(UserService userService , UserFeedService userFeedService, JwtService jwtService, MailService mailService){
        this.userService = userService;
        this.jwtService = jwtService;
        this.mailService = mailService;
        this.userFeedService = userFeedService;
    }

    @PostMapping("/userFeed/follow/{nickname}") // 팔로우 등록
    public ResponseEntity<?> registFollow(@PathVariable("nickname")String fromNickname, HttpServletRequest request){
        resultMap = new HashMap<>();
        String decodeId = checkToken(request);
        if(decodeId != null){
            String fromId = userService.NicknameToId(fromNickname);
            userFeedService.registFollow(decodeId,fromId);
            resultMap.put("result",okay);
            status = HttpStatus.OK;
        }

        return new ResponseEntity<>(resultMap,status);
    }
    @DeleteMapping("/userFeed/follow/{nickname}") // 팔로우 취소
    public ResponseEntity<?> deleteFollow(@PathVariable("nickname")String fromNickname, HttpServletRequest request){
        resultMap = new HashMap<>();
        String decodeId = checkToken(request);
        if(decodeId != null){
            String fromId = userService.NicknameToId(fromNickname);
            userFeedService.deleteFollow(decodeId,fromId);
            resultMap.put("result",okay);
            status = HttpStatus.OK;
        }

        return new ResponseEntity<>(resultMap,status);
    }
    @GetMapping("/userFeed/follow/{nickname}")  // 팔로우 목록 조회
    public ResponseEntity<?> listFollow(@PathVariable("nickname")String fromNickname){
        resultMap = new HashMap<>();
        try{
            String fromId = userService.NicknameToId(fromNickname);
            UserInfoDto userInfoDto =  userService.infoUser(fromId);
            if(userInfoDto.getFollowOpen()) {// 대상 id가 팔로우 설정이 되어있다면 조회하기
                List<FollowViewDto> result = userFeedService.listFollow(fromId);
                resultMap.put("data",result);
            }else{
                resultMap.put("data",notAllowed);
            }
            resultMap.put("result",okay);
            status = HttpStatus.OK;
        }catch (Exception e){
            resultMap.put("result",fail);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(resultMap,status);
    }
    @GetMapping("/userFeed/follower/{nickname}") // 팔로워 목록 조회
    public ResponseEntity<?> listFollower(@PathVariable("nickname")String fromNickname){
        resultMap = new HashMap<>();
        try{
            String fromId = userService.NicknameToId(fromNickname);
            UserInfoDto userInfoDto =  userService.infoUser(fromId);
            if(userInfoDto.getFollowerOpen()){// 대상 id가 팔로워 설정이 되어있다면 조회하기
                List<FollowViewDto> result = userFeedService.listFollower(fromId);
                resultMap.put("data",result);
            }else{
                resultMap.put("data",notAllowed);
            }
            resultMap.put("result",okay);
            status = HttpStatus.OK;
        }catch (Exception e){
            resultMap.put("result",fail);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(resultMap,status);
    }
    @GetMapping("/userFeed/follow/search/{nickname}") // 팔로우 검색
    public ResponseEntity<?> searchFollow(@PathVariable("nickname")String fromNickname, @RequestParam("keyword") String keyword){
        resultMap = new HashMap<>();
        try{
            String fromId = userService.NicknameToId(fromNickname);
            UserInfoDto userInfoDto =  userService.infoUser(fromId);
            if(userInfoDto.getFollowerOpen()){// 대상 id가 팔로워 설정이 되어있다면 조회하기
                List<FollowViewDto> result = userFeedService.searchFollow(fromId,keyword);
                resultMap.put("data",result);
            }else{
                resultMap.put("data",notAllowed);
            }
            resultMap.put("result",okay);
            status = HttpStatus.OK;
        }catch (Exception e){
            resultMap.put("result",fail);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(resultMap,status);
    }
    @GetMapping("/userFeed/follower/search/{nickname}") // 팔로워 검색
    public ResponseEntity<?> searchFollower(@PathVariable("nickname")String fromNickname, @RequestParam("keyword") String keyword){
        resultMap = new HashMap<>();
        try{
            String fromId = userService.NicknameToId(fromNickname);
            UserInfoDto userInfoDto =  userService.infoUser(fromId);
            if(userInfoDto.getFollowerOpen()){// 대상 id가 팔로워 설정이 되어있다면 조회하기
                List<FollowViewDto> result = userFeedService.searchFollower(fromId,keyword);
                resultMap.put("data",result);
            }else{
                resultMap.put("data",notAllowed);
            }
            resultMap.put("result",okay);
            status = HttpStatus.OK;
        }catch (Exception e){
            resultMap.put("result",fail);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(resultMap,status);
    }
    @GetMapping("/userFeed/profile/{nickname}") // 회원 피드 - 프로필 조회
    public ResponseEntity<?> feedProfile(@PathVariable("nickname")String fromNickname,HttpServletRequest request){
        resultMap = new HashMap<>();
        String accessToken = request.getHeader("Authorization"); // 로그인 했는지 체크?
        String decodeId = checkToken(request);
        String fromId = userService.NicknameToId(fromNickname);
        ProfileViewDto result = null;
        try{
            if(accessToken != null && decodeId == null){ // 로그인 했는데 인증 만료라면
                    resultMap.put("result",fail);
                    status = HttpStatus.UNAUTHORIZED;
            }else{ //서비스 호출
                result = userFeedService.feedProfile(fromId, decodeId);
                status = HttpStatus.OK;

            }

            if(result != null) {
                resultMap.put("data", result);
                resultMap.put("result", okay);
            }
        }catch (Exception e){
            resultMap.put("result",fail);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(resultMap,status);
    }

    @GetMapping("/userFeed/post/{nickname}") // 회원 피드 - 게시글 조회
    public ResponseEntity<?> feedPosts(@PathVariable("nickname")String fromNickname, @RequestParam("category") int category){
        resultMap = new HashMap<>();
        try{
            String fromId = userService.NicknameToId(fromNickname);
            List<PostViewDto> result = userFeedService.feedPosts(fromId,category);
            if(result != null){
                resultMap.put("data",result);
                resultMap.put("result",okay);
                status = HttpStatus.OK;
            }
        }catch (Exception e){
            resultMap.put("result",fail);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(resultMap,status);
    }
    @GetMapping("/mainFeed/user") // 인기있는 팔로워 추천
    public ResponseEntity<?> popularFollower(HttpServletRequest request){
        resultMap = new HashMap<>();
        try{
            String decodeId = checkToken(request);
            if(decodeId != null){
                List<PopularFollowDto> result = userFeedService.popularFollower(decodeId);
                if(result != null){
                    resultMap.put("data",result);
                    resultMap.put("result",okay);
                }
                status = HttpStatus.OK;
            }

        }catch (Exception e){
            resultMap.put("result",fail);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(resultMap,status);
    }
    @GetMapping("/mainFeed/honeyDeal") // 인기있는 꿀딜 추천
    public ResponseEntity<?> popularHoneyDeal(HttpServletRequest request){
        resultMap = new HashMap<>();
        String decodeId = checkToken(request);
        try{
            if(decodeId != null){
                List<DealDto> result = userFeedService.popularHoneyDeal(decodeId);
                resultMap.put("data",result);
                resultMap.put("result",okay);
                status = HttpStatus.OK;
            }
        }catch (Exception e){
            resultMap.put("result",fail);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(resultMap,status);
    }
    @GetMapping("/mainFeed/honeyTip")
    public ResponseEntity<?> userFollowHoneyTip(@RequestParam("lastIdx")Integer lastIdx,@RequestParam("pageSize") int pageSize, HttpServletRequest request){
        resultMap = new HashMap<>();
        String decodeId = checkToken(request);
        try{
            if(decodeId != null){
                Map result = userFeedService.userFollowHoneyTip(decodeId,lastIdx, pageSize);
                resultMap.put("data",result);

                resultMap.put("result",okay);
                status = HttpStatus.OK;
            }
        }catch (Exception e){
            resultMap.put("result",fail);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(resultMap,status);
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
