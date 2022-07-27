package com.gwangjubob.livealone.backend.dto.tipcomment;

import com.gwangjubob.livealone.backend.domain.entity.TipCommentEntity;
import com.gwangjubob.livealone.backend.domain.entity.TipEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class TipCommentCreateDto {
    private String userNickname;
    private Integer postIdx;
    private Integer upIdx;
    private String content;
    private byte[] bannerImg;
}
