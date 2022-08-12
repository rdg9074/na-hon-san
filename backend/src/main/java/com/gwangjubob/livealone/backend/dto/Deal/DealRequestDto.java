package com.gwangjubob.livealone.backend.dto.Deal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DealRequestDto {
    private String area;
    private List<String> categorys;
    private String keyword;
    private String type;
    private String state;
    private Integer pageSize;
    private Integer lastLikes;
    private Integer lastView;
    private Integer lastIdx;
}
