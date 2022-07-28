package com.gwangjubob.livealone.backend.service;

import com.gwangjubob.livealone.backend.dto.tipcomment.TipCommentCreateDto;
import com.gwangjubob.livealone.backend.dto.tipcomment.TipCommentUpdateDto;
import com.gwangjubob.livealone.backend.dto.tipcomment.TipCommentViewDto;

import java.util.List;

public interface TipCommentService {
    void createTipComment(String decodeId, TipCommentCreateDto tipCommentCreateDto);

    void updateTipComment(String decodeId, Integer idx, TipCommentUpdateDto requestDto);

    List<TipCommentViewDto> viewTipComment(Integer idx);

    void deleteTipComment(String decodeId, Integer idx);
}
