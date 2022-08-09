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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.*;

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
	public Map listDetailDM(String id, String fromId,int lastIdx, int pageSize){
		List<DMViewDto> dmViewDtoList = new ArrayList<>();
		UserEntity toUserEntity = userRepository.findById(id).get();
		UserEntity fromUserEntity = userRepository.findById(fromId).get();
		if(lastIdx == 0){ // null 이면 가장 최신 게시글 찾아줘야함
			lastIdx = dmRepository.findTop1ByOrderByIdxDesc().get().getIdx() + 1;
		}
		Pageable pageable = PageRequest.ofSize(pageSize);
		Slice<DMEntity> dmEntityList = dmRepository.findByToUserIdAndFromUserId(toUserEntity,fromUserEntity,lastIdx,pageable);
		Map<String, Object> result = new HashMap<>();
		boolean hasNext = dmEntityList.hasNext();

		for(DMEntity dmEntity : dmEntityList){
			dmEntity.setRead(true);
			dmRepository.save(dmEntity);
			DMViewDto dmViewDto = new DMViewDto();
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
		result.put("list",dmViewDtoList);
		result.put("fromProfileImg",fromUserEntity.getProfileImg());
		result.put("hasNext",hasNext);
		return result;
	}

	@Override
	public long countDM(String id) {
		return dmRepository.findByCountDM(id);
	}

}
