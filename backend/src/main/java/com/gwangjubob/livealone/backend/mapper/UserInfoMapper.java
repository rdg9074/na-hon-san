package com.gwangjubob.livealone.backend.mapper;

import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import com.gwangjubob.livealone.backend.dto.user.UserInfoDto;
import org.mapstruct.Mapper;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Mapper(componentModel = "spring")
public interface UserInfoMapper extends GenericMapper<UserInfoDto, UserEntity>{
    @Override
    UserInfoDto toDto(UserEntity userEntity);

    @Override
    UserEntity toEntity(UserInfoDto userInfoDto);
}
