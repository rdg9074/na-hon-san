package com.gwangjubob.livealone.backend.service.impl;

import com.gwangjubob.livealone.backend.domain.entity.DMEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import com.gwangjubob.livealone.backend.domain.repository.DMRepository;
import com.gwangjubob.livealone.backend.dto.dm.DMSendDto;
import com.gwangjubob.livealone.backend.dto.dm.DMViewDto;
import com.gwangjubob.livealone.backend.service.DMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DMServiceImpl implements DMService {
	private final DMRepository dmRepository;
	@Autowired
	DMServiceImpl(DMRepository dmRepository){
		this.dmRepository = dmRepository;
	}

	@Override
	public boolean sendDM(DMSendDto dmSendDto) {
//		System.out.println(dmSendDto.getContent());
		DMEntity dmEntity = DMEntity.builder()
				.fromUserId(dmSendDto.getFromId())
				.toUserId(dmSendDto.getToId())
				.content(dmSendDto.getContent())
				.image(dmSendDto.getImage())
				.build();

		dmRepository.save(dmEntity);
		return true;
	}
	@Override
	public List<DMViewDto> listDM(String id){
		List<DMViewDto> dmViewDtoList = new ArrayList<>();
		List<DMEntity> dmEntityList = dmRepository.findListViews(id);

		for(DMEntity dmEntity : dmEntityList){
			DMViewDto dmViewDto = new DMViewDto();
			dmViewDto.setIdx(dmEntity.getIdx());
			dmViewDto.setFromId(dmEntity.getFromUserId());
			dmViewDto.setToId(dmEntity.getToUserId());
			dmViewDto.setTime(dmEntity.getTime());
			dmViewDto.setRead(dmEntity.getRead());
			dmViewDto.setContent(dmEntity.getContent());
			dmViewDto.setImage((dmEntity.getImage()));
			int count = dmRepository.findCount(id,dmEntity.getFromUserId());
			dmViewDto.setCount(count);
			dmViewDtoList.add(dmViewDto);
		}
		return dmViewDtoList;
	}
}
