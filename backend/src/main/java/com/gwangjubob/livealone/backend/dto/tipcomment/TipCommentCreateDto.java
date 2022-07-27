package com.gwangjubob.livealone.backend.dto.tipcomment;

import com.gwangjubob.livealone.backend.domain.entity.TipCommentEntity;
import com.gwangjubob.livealone.backend.domain.entity.TipEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@Getter
@Setter
public class TipCommentCreateDto {

    private Integer postIdx;
    private Integer upIdx;
    private String content; // 댓글 내용
    private byte[] bannerImg; // 댓글 배너 이미지
    private LocalDate time;

}
