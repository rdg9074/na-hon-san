package com.gwangjubob.livealone.backend.dto.dm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DMSendDto  {
    String fromId;
    String toNickname;
    @NotBlank(message = "이름을 입력해주세요.")
    String content;
    byte[] image;
}
