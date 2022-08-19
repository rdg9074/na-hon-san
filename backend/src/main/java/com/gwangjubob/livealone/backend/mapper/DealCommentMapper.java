package com.gwangjubob.livealone.backend.mapper;

import com.gwangjubob.livealone.backend.domain.entity.DealCommentEntity;
import com.gwangjubob.livealone.backend.domain.entity.DealEntity;
import com.gwangjubob.livealone.backend.dto.Deal.DealCommentDto;
import com.gwangjubob.livealone.backend.dto.Deal.DealDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DealCommentMapper extends GenericMapper<DealCommentDto, DealCommentEntity> {

    @Override
    DealCommentDto toDto(DealCommentEntity dealCommentEntity);

    @Override
    DealCommentEntity toEntity(DealCommentDto dealCommentDto);

    List<DealCommentEntity> toEntityList(List<DealCommentDto> dealCommentDtoList);

    List<DealCommentDto> toDtoList(List<DealCommentEntity> dealCommentEntityList);

}
