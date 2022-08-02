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
    private int pageNum;
    private int pageSize;
}
