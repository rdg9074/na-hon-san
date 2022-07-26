package com.gwangjubob.livealone.backend.service;

import com.gwangjubob.livealone.backend.dto.tipcomment.TipCommentCreateDto;

public interface TipCommentService {
    void createTipComment(String decodeId, TipCommentCreateDto tipCommentCreateDto);
}
