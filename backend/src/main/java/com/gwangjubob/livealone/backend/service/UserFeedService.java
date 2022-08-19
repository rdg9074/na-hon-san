package com.gwangjubob.livealone.backend.service;

import com.gwangjubob.livealone.backend.dto.Deal.DealDto;
import com.gwangjubob.livealone.backend.dto.feed.FollowViewDto;
import com.gwangjubob.livealone.backend.dto.feed.PopularFollowDto;
import com.gwangjubob.livealone.backend.dto.feed.PostViewDto;
import com.gwangjubob.livealone.backend.dto.feed.ProfileViewDto;
import com.gwangjubob.livealone.backend.dto.tip.TipViewDto;

import java.util.List;
import java.util.Map;

public interface UserFeedService {
    boolean registFollow(String toId, String fromId);
    List<FollowViewDto> listFollow(String id);
    List<FollowViewDto> listFollower(String id);
    List<FollowViewDto> searchFollow(String id,String keyword);
    List<FollowViewDto> searchFollower(String id,String keyword);
    ProfileViewDto feedProfile(String id,String decodeId);
    List<PostViewDto> feedPosts(String id, int category);
    boolean deleteFollow(String toId, String fromId);

    List<PopularFollowDto> popularFollower(String decodeId);

    List<DealDto> popularHoneyDeal(String decodeId);

    Map userFollowHoneyTip(String decodeId, Integer lastIdx, int pageSize);

    boolean checkFollowDeal(String decodeId, Integer idx);

    boolean checkFollowTip(String decodeId, Integer idx);
}
