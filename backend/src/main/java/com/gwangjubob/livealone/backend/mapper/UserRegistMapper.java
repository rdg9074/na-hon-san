package com.gwangjubob.livealone.backend.mapper;

import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import com.gwangjubob.livealone.backend.dto.user.UserRegistDto;

public interface UserRegistMapper extends GenericMapper<UserRegistDto, UserEntity>{
    @Override
    UserRegistDto toDto(UserEntity userEntity);

    @Override
    UserEntity toEntity(UserRegistDto userRegistDto);
}
