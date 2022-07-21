package com.gwangjubob.livealone.backend.dto.dm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DMViewDto  {
    Integer idx;
    String fromId;
    String toId;
    String content;
    String image;
    Boolean read;
    Integer count;
    LocalDateTime time;
}
