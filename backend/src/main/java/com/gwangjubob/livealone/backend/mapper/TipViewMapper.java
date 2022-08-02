package com.gwangjubob.livealone.backend.mapper;

import com.gwangjubob.livealone.backend.domain.entity.TipEntity;
import com.gwangjubob.livealone.backend.dto.tip.TipViewDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TipViewMapper extends GenericMapper<TipViewDto, TipEntity> {

    List<TipViewDto> toDtoList(List<TipEntity> tipEntityList);
}
