package com.gwangjubob.livealone.backend.service;

import com.gwangjubob.livealone.backend.dto.dm.DMSendDto;
import com.gwangjubob.livealone.backend.dto.user.UserLoginDto;
import com.gwangjubob.livealone.backend.dto.user.UserRegistDto;

public interface DMService {
    boolean sendDM(DMSendDto dmSendDto);
}
