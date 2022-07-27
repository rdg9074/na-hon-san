package com.gwangjubob.livealone.backend.service;

import com.gwangjubob.livealone.backend.dto.tipcomment.TipCommentCreateDto;
import com.gwangjubob.livealone.backend.dto.tipcomment.TipCommentUpdateDto;

public interface TipCommentService {
    void createTipComment(String decodeId, TipCommentCreateDto tipCommentCreateDto);

    void updateTipComment(String decodeId, Integer idx, TipCommentUpdateDto requestDto);
}
