package com.gwangjubob.livealone.backend.mapper;

import com.gwangjubob.livealone.backend.domain.entity.DealEntity;

import com.gwangjubob.livealone.backend.dto.Deal.DealViewDto;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring")
public interface DealViewMapper extends GenericMapper<DealViewDto, DealEntity> {

    @Override
    DealViewDto toDto(DealEntity dealEntity);

    @Override
    DealEntity toEntity(DealViewDto dealViewDto);

    List<DealEntity> toEntityList(List<DealViewDto> dealDtoList);

    List<DealViewDto> toDtoList(List<DealEntity> dealEntityList);
}
