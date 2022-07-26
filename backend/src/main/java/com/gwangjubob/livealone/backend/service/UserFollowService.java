package com.gwangjubob.livealone.backend.service;

import com.gwangjubob.livealone.backend.dto.feed.FollowViewDto;
import com.gwangjubob.livealone.backend.dto.user.UserInfoDto;
import com.gwangjubob.livealone.backend.dto.user.UserLoginDto;
import com.gwangjubob.livealone.backend.dto.user.UserMoreDTO;
import com.gwangjubob.livealone.backend.dto.user.UserRegistDto;

import java.util.List;

public interface UserFollowService {
    boolean registFollow(String toId, String fromId);
    List<FollowViewDto> listFollow(String id);
    List<FollowViewDto> listFollower(String id);
    List<FollowViewDto> searchFollow(String id,String keyword);
    List<FollowViewDto> searchFollower(String id,String keyword);
    boolean deleteFollow(String toId, String fromId);
}
