package com.gwangjubob.livealone.backend.service;

import com.gwangjubob.livealone.backend.dto.tip.*;

import java.util.List;
import java.util.Map;

public interface TipService {
    int createTip(String decodeId, TipCreateDto tipCreateDto);
    Map viewTip(TipListDto tipListDto);
    void updateTip(String decodeId, TipUpdateDto tipUpdateDto, Integer idx);
    void deleteTip(String decodeId, Integer idx);
    TipDetailViewDto detailViewTip(Integer idx);
    void likeTip(String decodeId, Integer idx);

    boolean countUpView(Integer idx);

    long getTotalCount();

    boolean clickLikeButton(String decodeId, Integer idx);
}
