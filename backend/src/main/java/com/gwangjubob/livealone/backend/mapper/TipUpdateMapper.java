package com.gwangjubob.livealone.backend.mapper;

import com.gwangjubob.livealone.backend.domain.entity.TipEntity;
import com.gwangjubob.livealone.backend.dto.tip.TipUpdateDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TipUpdateMapper extends GenericMapper<TipUpdateDto, TipEntity> {
    @Override
    TipUpdateDto toDto(TipEntity tipEntity);

    @Override
    TipEntity toEntity(TipUpdateDto tipUpdateDto);
}
