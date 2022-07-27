package com.gwangjubob.livealone.backend.controller;


import com.gwangjubob.livealone.backend.dto.feed.FollowViewDto;
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

    @PostMapping("/userFeed/follow/{id}")
    public ResponseEntity<?> registFollow(@PathVariable("id")String fromId, HttpServletRequest request){
        resultMap = new HashMap<>();
        String decodeId = checkToken(request);
        if(decodeId != null){
            userFeedService.registFollow(decodeId,fromId);
            resultMap.put("result",okay);
            status = HttpStatus.OK;
        }else {
            resultMap.put("result",fail);
            status = HttpStatus.UNAUTHORIZED;
        }


        return new ResponseEntity<>(resultMap,status);
    }
    @DeleteMapping("/userFeed/follow/{id}")
    public ResponseEntity<?> deleteFollow(@PathVariable("id")String fromId, HttpServletRequest request){
        resultMap = new HashMap<>();
        String decodeId = checkToken(request);
        if(decodeId != null){
            userFeedService.deleteFollow(decodeId,fromId);
            resultMap.put("result",okay);
            status = HttpStatus.OK;
        }else {
            resultMap.put("result",fail);
            status = HttpStatus.UNAUTHORIZED;
        }


        return new ResponseEntity<>(resultMap,status);
    }
    @GetMapping("/userFeed/follow/{id}")
    public ResponseEntity<?> listFollow(@PathVariable("id")String fromId){
        resultMap = new HashMap<>();
        try{
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
    @GetMapping("/userFeed/follower/{id}")
    public ResponseEntity<?> listFollower(@PathVariable("id")String fromId){
        resultMap = new HashMap<>();
        try{
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
    @GetMapping("/userFeed/follow/search/{id}")
    public ResponseEntity<?> searchFollow(@PathVariable("id")String fromId, @RequestParam("keyword") String keyword){
        System.out.println(keyword);
        resultMap = new HashMap<>();
        try{
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
    @GetMapping("/userFeed/follower/search/{id}")
    public ResponseEntity<?> searchFollower(@PathVariable("id")String fromId, @RequestParam("keyword") String keyword){
        resultMap = new HashMap<>();
        try{
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
    @GetMapping("/userFeed/profile/{id}")
    public ResponseEntity<?> feedProfile(@PathVariable("id")String fromId){
        resultMap = new HashMap<>();
        try{
                ProfileViewDto result = userFeedService.feedProfile(fromId);
                resultMap.put("data",result);
                resultMap.put("result",okay);
                status = HttpStatus.OK;
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
