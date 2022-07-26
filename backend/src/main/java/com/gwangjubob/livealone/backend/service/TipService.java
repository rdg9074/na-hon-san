package com.gwangjubob.livealone.backend.service;

import com.gwangjubob.livealone.backend.dto.tip.TipCreateDto;
import com.gwangjubob.livealone.backend.dto.tip.TipViewDto;

import java.util.List;

public interface TipService {
    void createTip(String decodeId, TipCreateDto tipCreateDto);
    List<TipViewDto> viewTip(String category);
}
