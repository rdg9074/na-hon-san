package com.gwangjubob.livealone.backend.dto.tip;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipListDto {
    private String category;
    private String keyword;
    private String type;
    private Integer lastIdx;
    private Integer lastView;
    private Integer lastLike;
    private Integer pageSize;
}
