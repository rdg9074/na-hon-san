package com.gwangjubob.livealone.backend.service;

import com.gwangjubob.livealone.backend.dto.user.UserLoginDto;
import com.gwangjubob.livealone.backend.dto.user.UserMoreDTO;
import com.gwangjubob.livealone.backend.dto.user.UserRegistDto;
import com.gwangjubob.livealone.backend.dto.user.UserInfoDto;

import java.util.Map;

public interface UserService {
    boolean loginUser(UserLoginDto userLoginDto);

    void userDelete(String id);
    boolean passwordCheckUser(String id, String password);

    void registUser(UserRegistDto userRegistDto);

    boolean checkNickName(String nickname);

    UserInfoDto updateUser(UserInfoDto userUpdateDto);

    boolean updatePassword(UserLoginDto userLoginDto);

    void moreUpdate(UserMoreDTO userMoreDTO);

    UserInfoDto infoUser(String id);
    String NicknameToId(String nickname);
    UserMoreDTO infoMore(String id);

    Map<String, Double> getXYLocation(String id);

    String getTargetId(String nickname);

    Map<String, Double> getPosition(String id);

}
