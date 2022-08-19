package com.gwangjubob.livealone.backend.mapper;

import com.gwangjubob.livealone.backend.domain.entity.TipEntity;
import com.gwangjubob.livealone.backend.dto.tip.TipCreateDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TipCreateMapper extends GenericMapper<TipCreateDto, TipEntity> {

    @Override
    TipCreateDto toDto(TipEntity tipEntity);

    @Override
    TipEntity toEntity(TipCreateDto tipCreateDto);
}
