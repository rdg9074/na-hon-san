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
    private Integer postIdx;
    private Integer upIdx;
    private String content;
    private String bannerImg;
    private LocalDate time;
}
