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

    List<DealDto> viewDeal(String category);

    DealDto updateDeal(Integer idx, DealDto DealDto);

    boolean deleteDeal(Integer idx);

    DealCommentDto registDealComment(DealCommentDto dealCommentDto);

    DealCommentDto updateDealComment(Integer idx, DealCommentDto dealCommentDto);

    boolean deleteDealComment(Integer idx, String userId);

    boolean likeDeal(Integer idx, String userId);

    boolean countUpView(Integer idx);
    Map<String, Object> viewDealView(DealRequestDto dealRequestDto,String decodeId);
}
