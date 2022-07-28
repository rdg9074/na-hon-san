package com.gwangjubob.livealone.backend.dto.tipcomment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TipCommentUpdateDto {
    private String content;
    private byte[] bannerImg;
}
