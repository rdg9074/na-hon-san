package com.gwangjubob.livealone.backend.service.impl;

import com.gwangjubob.livealone.backend.domain.entity.DMEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import com.gwangjubob.livealone.backend.domain.repository.DMRepository;
import com.gwangjubob.livealone.backend.dto.dm.DMSendDto;
import com.gwangjubob.livealone.backend.service.DMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DMServiceImpl implements DMService {
	private final DMRepository dmRepository;
	@Autowired
	DMServiceImpl(DMRepository dmRepository){
		this.dmRepository = dmRepository;
	}

	@Override
	public boolean sendDM(DMSendDto dmSendDto) {
		DMEntity dmEntity = DMEntity.builder()
				.fromUserId(dmSendDto.getFromId())
				.toUserId(dmSendDto.getToId())
				.content(dmSendDto.getContent())
				.image(dmSendDto.getImage())
				.build();
		dmRepository.save(dmEntity);
		return false;
	}
}
