package com.gwangjubob.livealone.backend.service;

import com.gwangjubob.livealone.backend.dto.tip.TipCreateDto;
import com.gwangjubob.livealone.backend.dto.tip.TipDetailViewDto;
import com.gwangjubob.livealone.backend.dto.tip.TipUpdateDto;
import com.gwangjubob.livealone.backend.dto.tip.TipViewDto;

import java.util.List;

public interface TipService {
    void createTip(String decodeId, TipCreateDto tipCreateDto);
    List<TipViewDto> viewTip(String category);
    void updateTip(String decodeId, TipUpdateDto tipUpdateDto, Integer idx);
    void deleteTip(String decodeId, Integer idx);
    TipDetailViewDto detailViewTip(Integer idx);
    void likeTip(String decodeId, Integer idx);
}
