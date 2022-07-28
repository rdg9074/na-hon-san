package com.gwangjubob.livealone.backend.service;

import com.gwangjubob.livealone.backend.dto.Deal.DealDto;
import com.gwangjubob.livealone.backend.dto.feed.FollowViewDto;
import com.gwangjubob.livealone.backend.dto.feed.PopularFollowDto;
import com.gwangjubob.livealone.backend.dto.feed.PostViewDto;
import com.gwangjubob.livealone.backend.dto.feed.ProfileViewDto;

import java.util.List;

public interface UserFeedService {
    boolean registFollow(String toId, String fromId);
    List<FollowViewDto> listFollow(String id);
    List<FollowViewDto> listFollower(String id);
    List<FollowViewDto> searchFollow(String id,String keyword);
    List<FollowViewDto> searchFollower(String id,String keyword);
    ProfileViewDto feedProfile(String id);
    List<PostViewDto> feedPosts(String id, int category);
    boolean deleteFollow(String toId, String fromId);

    List<PopularFollowDto> popularFollower();

    List<DealDto> popularHoneyDeal(String decodeId);
}
