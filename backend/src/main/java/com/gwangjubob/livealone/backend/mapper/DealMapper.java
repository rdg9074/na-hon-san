package com.gwangjubob.livealone.backend.mapper;

import com.gwangjubob.livealone.backend.domain.entity.DealEntity;
import com.gwangjubob.livealone.backend.dto.Deal.DealDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DealMapper extends GenericMapper<DealDto, DealEntity> {
    @Override
    DealDto toDto(DealEntity dealEntity);

    @Override
    DealEntity toEntity(DealDto dealDto);
}
