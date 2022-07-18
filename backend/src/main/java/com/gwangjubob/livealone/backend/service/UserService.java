package com.gwangjubob.livealone.backend.service;

import com.gwangjubob.livealone.backend.dto.user.UserLoginDto;
import com.gwangjubob.livealone.backend.dto.user.UserRegistDto;

public interface UserService {
    boolean loginUser(UserLoginDto userLoginDto);

    boolean registUser(UserRegistDto userRegistDto);
}
