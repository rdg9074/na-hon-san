package com.gwangjubob.livealone.backend.service;

import com.gwangjubob.livealone.backend.dto.user.UserLoginDto;

public interface UserService {
    boolean loginUser(UserLoginDto userLoginDto);
}
