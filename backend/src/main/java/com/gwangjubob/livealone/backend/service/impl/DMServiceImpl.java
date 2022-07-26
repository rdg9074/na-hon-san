package com.gwangjubob.livealone.backend.service.impl;

import com.gwangjubob.livealone.backend.domain.entity.DMEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import com.gwangjubob.livealone.backend.domain.repository.DMRepository;
import com.gwangjubob.livealone.backend.domain.repository.UserRepository;
import com.gwangjubob.livealone.backend.dto.dm.DMSendDto;
import com.gwangjubob.livealone.backend.dto.dm.DMViewDto;
import com.gwangjubob.livealone.backend.dto.user.UserInfoDto;
import com.gwangjubob.livealone.backend.service.DMService;
import com.gwangjubob.livealone.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DMServiceImpl implements DMService {
	private final DMRepository dmRepository;
	private final UserRepository userRepository;
	private final UserServiceImpl userService;
	@Autowired
	DMServiceImpl(DMRepository dmRepository,UserServiceImpl userService,UserRepository userRepository){
		this.dmRepository = dmRepository;
		this.userService = userService;
		this.userRepository = userRepository;
	}

	@Override
	public boolean sendDM(DMSendDto dmSendDto) {
		//2개의 데이터 toId, fromId
		UserEntity toId = userRepository.findById(dmSendDto.getToId()).get();
		UserEntity fromId = userRepository.findById(dmSendDto.getFromId()).get();
		DMEntity dmEntity = DMEntity.builder()
				.toUserId(toId)
				.fromUserId(fromId)
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
			dmViewDto.setFromId(dmEntity.getFromUserId().getId());
			dmViewDto.setNickname(dmEntity.getFromUserId().getNickname());
			dmViewDto.setToId(dmEntity.getToUserId().getId());
			dmViewDto.setTime(dmEntity.getTime());
			dmViewDto.setRead(dmEntity.getRead());
			dmViewDto.setContent(dmEntity.getContent());
			dmViewDto.setImage((dmEntity.getImage()));
			int count = dmRepository.findCount(id,dmEntity.getFromUserId().getId());
			dmViewDto.setCount(count);
			dmViewDtoList.add(dmViewDto);
		}
		return dmViewDtoList;
	}
	@Override
	public List<DMViewDto> listDetailDM(String id, String fromId){
		List<DMViewDto> dmViewDtoList = new ArrayList<>();
		UserEntity toUserEntity = userRepository.findById(id).get();
		UserEntity fromUserEntity = userRepository.findById(fromId).get();
		List<DMEntity> dmEntityList = dmRepository.findByToUserIdAndFromUserId(toUserEntity,fromUserEntity);

		for(DMEntity dmEntity : dmEntityList){
			dmEntity.setRead(true);
			dmRepository.save(dmEntity);
			DMViewDto dmViewDto = new DMViewDto();
			dmViewDto.setType("from");
			dmViewDto.setIdx(dmEntity.getIdx());
			dmViewDto.setFromId(dmEntity.getFromUserId().getId());
			dmViewDto.setToId(dmEntity.getToUserId().getId());
			dmViewDto.setTime(dmEntity.getTime());
			dmViewDto.setRead(dmEntity.getRead());
			dmViewDto.setNickname(dmEntity.getFromUserId().getNickname());
			dmViewDto.setContent(dmEntity.getContent());
			dmViewDto.setImage((dmEntity.getImage()));
			dmViewDtoList.add(dmViewDto);
		}
		dmEntityList = dmRepository.findByToUserIdAndFromUserId(fromUserEntity,toUserEntity);
		for(DMEntity dmEntity : dmEntityList){
			DMViewDto dmViewDto = new DMViewDto();
			dmViewDto.setType("to");
			dmViewDto.setIdx(dmEntity.getIdx());
			dmViewDto.setFromId(dmEntity.getFromUserId().getId());
			dmViewDto.setToId(dmEntity.getToUserId().getId());
			dmViewDto.setNickname(dmEntity.getFromUserId().getNickname());
			dmViewDto.setTime(dmEntity.getTime());
			dmViewDto.setRead(dmEntity.getRead());
			dmViewDto.setContent(dmEntity.getContent());
			dmViewDto.setImage((dmEntity.getImage()));
			dmViewDtoList.add(dmViewDto);
		}

		return dmViewDtoList;
	}

	@Override
	public long countDM(String id) {
		return dmRepository.findByCountDM(id);
	}

}
