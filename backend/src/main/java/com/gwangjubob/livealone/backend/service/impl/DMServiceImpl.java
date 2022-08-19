package com.gwangjubob.livealone.backend.service.impl;

import com.gwangjubob.livealone.backend.domain.entity.DMEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import com.gwangjubob.livealone.backend.domain.repository.DMRepository;
import com.gwangjubob.livealone.backend.domain.repository.UserRepository;
import com.gwangjubob.livealone.backend.dto.dm.DMSendDto;
import com.gwangjubob.livealone.backend.dto.dm.DMViewDto;
import com.gwangjubob.livealone.backend.service.DMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
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

		UserEntity toId = userRepository.findByNickname(dmSendDto.getToNickname()).get();
		UserEntity fromId = userRepository.findById(dmSendDto.getFromId()).get();
		DMEntity dmEntity = DMEntity.builder()
				.toUserId(toId)
				.fromUserId(fromId)
				.content(dmSendDto.getContent())
				.image(dmSendDto.getImage())
				.time(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
				.build();

		dmRepository.save(dmEntity);
		return true;
	}
	@Override
	public List<DMViewDto> listDM(String id){
		List<DMViewDto> dmViewDtoList = new ArrayList<>();
		Optional<UserEntity> optionalUser = userRepository.findById(id);
		if(optionalUser.isPresent()){
			UserEntity user = optionalUser.get();
			List<UserEntity> toUser = dmRepository.findByfromUserIdGrouptoUserId(user);
			List<UserEntity> fromUser = dmRepository.findBytoUserIdGroupFromUserId(user);
			List<UserEntity> DMUsers = new ArrayList<>();
			if(!toUser.isEmpty()){
				for (UserEntity d: toUser) {
					if(!DMUsers.contains(d)){
						DMUsers.add(d);
					}
				}
			}
			if(!fromUser.isEmpty()){
				for (UserEntity d: fromUser){
					if(!DMUsers.contains(d)){
						DMUsers.add(d);
					}
				}
			}
			List<DMEntity> dmList = new ArrayList<>();
			if(!DMUsers.isEmpty()){
				for(UserEntity u : DMUsers){
					Optional<DMEntity> optionalDM = dmRepository.findByUserIdAndOtherId(user,u);
					if(optionalDM.isPresent()){
						DMEntity dm = optionalDM.get();
						dmList.add(dm);
					}
				}
				Collections.sort(dmList, (a,b) -> b.getIdx() - a.getIdx());
				for(DMEntity d : dmList){
					String otherId = null;
					String nickname = null;
					if(d.getFromUserId().getId().equals(id)){
						nickname = d.getToUserId().getNickname();
						otherId = d.getToUserId().getId();
					} else if(d.getToUserId().getId().equals(id)){
						nickname = d.getFromUserId().getNickname();
						otherId = d.getFromUserId().getId();
					}
					byte[] profileImg = null;
					if(user.getId().equals(d.getFromUserId().getId())){
						profileImg = d.getToUserId().getProfileImg();
					} else if(user.getId().equals(d.getToUserId().getId())){
						profileImg = d.getFromUserId().getProfileImg();
					}

					int cnt = dmRepository.findCount(id, otherId);

					DMViewDto dmViewDto = DMViewDto.builder()
							.idx(d.getIdx())
							.fromId(d.getFromUserId().getId())
							.toProfileImg(profileImg)
							.toId(d.getToUserId().getId())
							.content(d.getContent())
							.image(d.getImage())
							.read(d.getRead())
							.Nickname(nickname)
							.time(d.getTime())
							.count(cnt)
							.build();
					dmViewDtoList.add(dmViewDto);
				}
			}
		}
		return dmViewDtoList;
	}
	@Override
	public Map listDetailDM(String id, String fromId,int lastIdx, int pageSize){
		List<DMViewDto> dmViewDtoList = new ArrayList<>();
		UserEntity toUserEntity = userRepository.findById(id).get();
		UserEntity fromUserEntity = userRepository.findById(fromId).get();
		Optional<DMEntity> tmpIdx = dmRepository.findTop1ByOrderByIdxDesc();

		if(lastIdx == 0 && tmpIdx.isPresent()){ // null 이면 가장 최신 게시글 찾아줘야함
			lastIdx = tmpIdx.get().getIdx() + 1;
		}
		Pageable pageable = PageRequest.ofSize(pageSize);
		Slice<DMEntity> dmEntityList = dmRepository.findByToUserIdAndFromUserId(toUserEntity,fromUserEntity,lastIdx,pageable);
		Map<String, Object> result = new HashMap<>();
		boolean hasNext = false;

		if(!dmEntityList.isEmpty()){
			hasNext = dmEntityList.hasNext();

			for(DMEntity dmEntity : dmEntityList){

				DMViewDto dmViewDto = new DMViewDto();
				if(dmEntity.getFromUserId().getId().equals(id)){
					dmViewDto.setType("send");
				}else{
					dmViewDto.setType("recv");
					dmEntity.setRead(true);
					dmRepository.save(dmEntity);
				}
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
		}
		result.put("list",dmViewDtoList);
		result.put("fromProfileImg",fromUserEntity.getProfileImg());
		result.put("fromNickname", fromUserEntity.getNickname());
		result.put("hasNext",hasNext);
		return result;
	}

	@Override
	public long countDM(String id) {
		return dmRepository.findByCountDM(id);
	}

}
