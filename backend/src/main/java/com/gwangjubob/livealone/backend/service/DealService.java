package com.gwangjubob.livealone.backend.service;


import com.gwangjubob.livealone.backend.domain.entity.DealCommentEntity;
import com.gwangjubob.livealone.backend.dto.Deal.DealCommentDto;
import com.gwangjubob.livealone.backend.dto.Deal.DealDto;

public interface DealService {
    DealDto registDeal(DealDto dealDto);
    DealDto viewDetailDeal(Integer idx);

    DealDto updateDeal(Integer idx, DealDto DealDto);

    boolean deleteDeal(Integer idx);

    DealCommentDto registDealComment(DealCommentDto dealCommentDto);

    DealCommentDto updateDealComment(Integer idx, DealCommentDto dealCommentDto);

    boolean deleteDealComment(Integer idx);
}
