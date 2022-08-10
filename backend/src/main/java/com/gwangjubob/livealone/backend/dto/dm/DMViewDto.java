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
    String type;
    String fromId;
    String Nickname;
    String toId;
    String content;
    byte[] image;
    Boolean read;
    Integer count;
    LocalDateTime time;
}