package com.gwangjubob.livealone.backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistDto {
    private String id;
    private String password;
    private String nickname;
}
