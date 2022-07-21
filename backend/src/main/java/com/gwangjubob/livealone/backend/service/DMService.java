package com.gwangjubob.livealone.backend.service;

import com.gwangjubob.livealone.backend.dto.dm.DMSendDto;
import com.gwangjubob.livealone.backend.dto.dm.DMViewDto;
import com.gwangjubob.livealone.backend.dto.user.UserLoginDto;
import com.gwangjubob.livealone.backend.dto.user.UserRegistDto;


import java.util.List;



public interface DMService {
    boolean sendDM(DMSendDto dmSendDto);
    List<DMViewDto> listDM(String id);
}
