package com.gwangjubob.livealone.backend.service;

import com.gwangjubob.livealone.backend.dto.feed.FollowViewDto;
import com.gwangjubob.livealone.backend.dto.feed.ProfileViewDto;

import java.util.List;

public interface UserFeedService {
    boolean registFollow(String toId, String fromId);
    List<FollowViewDto> listFollow(String id);
    List<FollowViewDto> listFollower(String id);
    List<FollowViewDto> searchFollow(String id,String keyword);
    List<FollowViewDto> searchFollower(String id,String keyword);
    ProfileViewDto feedProfile(String id);
    boolean deleteFollow(String toId, String fromId);
}
