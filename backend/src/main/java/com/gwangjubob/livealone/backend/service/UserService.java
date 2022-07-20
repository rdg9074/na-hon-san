package com.gwangjubob.livealone.backend.service;

import com.gwangjubob.livealone.backend.dto.user.UserLoginDto;
import com.gwangjubob.livealone.backend.dto.user.UserRegistDto;
import com.gwangjubob.livealone.backend.dto.user.UserUpdateDto;

public interface UserService {
    boolean loginUser(UserLoginDto userLoginDto);

    void userDelete(String id);
    boolean passwordCheckUser(String id, String password);

    boolean registUser(UserRegistDto userRegistDto);

    boolean checkNickName(String nickname);

    UserUpdateDto updateUser(UserUpdateDto userUpdateDto);

    boolean updatePassword(UserLoginDto userLoginDto);
}
