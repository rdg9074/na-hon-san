package com.gwangjubob.livealone.backend.service;


import com.gwangjubob.livealone.backend.domain.entity.DealCommentEntity;
import com.gwangjubob.livealone.backend.dto.Deal.DealCommentDto;
import com.gwangjubob.livealone.backend.dto.Deal.DealDto;
import com.gwangjubob.livealone.backend.dto.Deal.DealRequestDto;


import java.util.List;
import java.util.Map;

public interface DealService {
    DealDto registDeal(DealDto dealDto);
    DealDto viewDetailDeal(Integer idx);

    List<DealCommentDto> viewDealComment(Integer idx);

    DealDto updateDeal(Integer idx, DealDto DealDto, String decodeId);

    boolean deleteDeal(Integer idx, String decodeId);

    DealCommentDto registDealComment(DealCommentDto dealCommentDto);

    DealCommentDto updateDealComment(Integer idx, DealCommentDto dealCommentDto);

    void deleteDealComment(Integer idx, String userId);

    boolean likeDeal(Integer idx, String userId);

    boolean countUpView(Integer idx);

    Boolean clickLikeButton(String decodeId, Integer idx);

    Map<String, Object> viewDeal(DealRequestDto dealRequestDto);

    Map<String,Object> searchMidPosition(String loginUserId, String targetUserId);

    long countArea(String area);

    int getApiCount();
}
