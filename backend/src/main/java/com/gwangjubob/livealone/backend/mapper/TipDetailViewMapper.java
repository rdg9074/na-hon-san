package com.gwangjubob.livealone.backend.mapper;

import com.gwangjubob.livealone.backend.domain.entity.TipEntity;
import com.gwangjubob.livealone.backend.dto.tip.TipDetailViewDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TipDetailViewMapper extends GenericMapper<TipDetailViewDto, TipEntity> {
    @Override
    TipDetailViewDto toDto(TipEntity tipEntity);

    @Override
    TipEntity toEntity(TipDetailViewDto tipDetailViewDto);
}
