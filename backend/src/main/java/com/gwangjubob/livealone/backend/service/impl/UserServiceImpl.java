package com.gwangjubob.livealone.backend.service.impl;

import com.gwangjubob.livealone.backend.domain.entity.DealEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserCategoryEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import com.gwangjubob.livealone.backend.domain.repository.DealRepository;
import com.gwangjubob.livealone.backend.domain.repository.UserCategoryRepository;
import com.gwangjubob.livealone.backend.domain.repository.UserRepository;
import com.gwangjubob.livealone.backend.dto.user.UserLoginDto;
import com.gwangjubob.livealone.backend.dto.user.UserMoreDTO;
import com.gwangjubob.livealone.backend.dto.user.UserRegistDto;
import com.gwangjubob.livealone.backend.mapper.UserInfoMapper;
import com.gwangjubob.livealone.backend.dto.user.UserInfoDto;
import com.gwangjubob.livealone.backend.service.UserService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private UserCategoryRepository userCategoryRepository;
    private final PasswordEncoder passwordEncoder;
    private UserInfoMapper userInfoMapper;

    private DealRepository dealRepository;
    @Autowired
    UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserCategoryRepository userCategoryRepository, UserInfoMapper userInfoMapper, DealRepository dealRepository){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userCategoryRepository = userCategoryRepository;
        this.userInfoMapper = userInfoMapper;
        this.dealRepository = dealRepository;
    }
    @Override
    public boolean loginUser(UserLoginDto userLoginDto){
        UserEntity user = userRepository.findByIdAndSocial(userLoginDto.getId(),"normal").get();
        if (user !=null){
            return passwordEncoder.matches(userLoginDto.getPassword(),user.getPassword());
        }
        return false;
    }

    @Override
    public void userDelete(String id){
        userRepository.deleteById(id);
    }

    @Override
    public boolean passwordCheckUser(String id, String password) {
        UserEntity user = userRepository.findById(id).get();
        if (user !=null){
            Boolean passwordCheck = passwordEncoder.matches(password,user.getPassword());
            return passwordCheck;
        }
        return false;
    }


    public void registUser(UserRegistDto userRegistDto) {
        String password = passwordEncoder.encode(userRegistDto.getPassword());
        UserEntity user = UserEntity.builder()
                .id(userRegistDto.getId())
                .password(password)
                .nickname(userRegistDto.getNickname())
                .social("normal")
                .build();
        userRepository.save(user);
    }

    @Override
    public boolean checkNickName(String nickname) {
        return userRepository.findByNickname(nickname).isPresent();
    }

    @Override
    public UserInfoDto updateUser(UserInfoDto userInfoDto) {
        UserEntity user =  userRepository.findById(userInfoDto.getId()).get();
        if(user != null){
            userInfoMapper.updateFromDto(userInfoDto, user);
            userRepository.save(user);
            UserInfoDto userInfo =  userInfoMapper.toDto(user);
            return userInfo;
        }
        return null;
    }

    @Override
    public boolean updatePassword(UserLoginDto userLoginDto) {
        UserEntity user =  userRepository.findById(userLoginDto.getId()).get();
        String password = passwordEncoder.encode(userLoginDto.getPassword());
        if(user != null){
            user.setPassword(password);
            userRepository.save(user);
            return true;
        } else{
            return false;
        }
    }

    @Override
    public void moreUpdate(UserMoreDTO userMoreDTO) {
        UserEntity user = userRepository.findById(userMoreDTO.getUserId()).get();
        if(user != null){
            if(!userMoreDTO.getArea().isBlank()){
                String area = userMoreDTO.getArea().split(" ")[0].substring(0,2);
                user.setArea(userMoreDTO.getArea());
                userRepository.save(user);
                List<DealEntity> deals = dealRepository.findByUser(user);
                if(!deals.isEmpty()){
                    for(DealEntity deal : deals){
                        deal.setArea(area);
                        dealRepository.save(deal);
                    }
                }
            }
            Map<String, Double> location = getXYLocation(user.getId());
            user.setAreaX(location.get("areaX"));
            user.setAreaY(location.get("areaY"));
            userRepository.save(user);

            List<UserCategoryEntity> delCategorys = userCategoryRepository.findByUser(user);
            for (UserCategoryEntity uc : delCategorys) {
                userCategoryRepository.delete(uc);
            }
            List<String> categorys = userMoreDTO.getCategorys();
            for (String c : categorys) {
                UserCategoryEntity usercategory = UserCategoryEntity.builder()
                        .category(c)
                        .user(user)
                        .build();
                userCategoryRepository.save(usercategory);
            }
        }
    }



    public UserInfoDto infoUser(String id) {
        UserEntity user = userRepository.findById(id).get();
        UserInfoDto userInfo = userInfoMapper.toDto(user);
        return userInfo;
    }

    @Override
    public String NicknameToId(String nickname) {
        return userRepository.findByNickname(nickname).get().getId();
    }

    @Override
    public UserMoreDTO infoMore(String id) {
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            UserEntity user = userRepository.findById(id).get();
            List<UserCategoryEntity> userCategoryEntities = userCategoryRepository.findByUser(user);
            List<String> categorys = new ArrayList<>();
            for (UserCategoryEntity c : userCategoryEntities){
                categorys.add(c.getCategory());
            }
            UserMoreDTO data = UserMoreDTO
                    .builder()
                    .userId(user.getId())
                    .area(user.getArea())
                    .categorys(categorys)
                    .build();
            return data;
        } else{
            return null;
        }
    }

    @Override
    public Map<String, Double> getXYLocation(String id) {
        Map<String, Double> location = new HashMap<>();

        UserEntity user = userRepository.findById(id).get();

        String userArea = user.getArea();

        try{
            String address = URLEncoder.encode(userArea, "UTF-8");
            String surl = "https://dapi.kakao.com/v2/local/search/address.json?query=" + address;

            URL url = new URL(surl);

            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            String auth = "KakaoAK " + "f5c2474d4cb8a685be34f0c926aa7e8a";
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-Requested-With", "curl");
            conn.setRequestProperty("Authorization", auth);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String inputStr;
            StringBuilder sb = new StringBuilder();
            while((inputStr = br.readLine()) != null){
                sb.append(inputStr);
            }

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(sb.toString());

            JSONObject jsonObject = (JSONObject) json.get("meta");

            JSONArray data = (JSONArray) json.get("documents");
            long size = (long) jsonObject.get("total_count");
            if(size > 0 ){
                JSONObject jsonX = (JSONObject) data.get(0);

                Double areaX = Double.parseDouble(jsonX.get("x").toString());
                Double areaY = Double.parseDouble(jsonX.get("y").toString());

                location.put("areaX", areaX);
                location.put("areaY", areaY);
            }

            System.out.println(location.get("areaX"));
            System.out.println(location.get("areaY"));
        }catch (Exception e){
            e.printStackTrace();
        }


        return location;
    }

    @Override
    public String getTargetId(String nickname) {
        if(userRepository.findByNickname(nickname).isPresent()){
            return userRepository.findByNickname(nickname).get().getId();
        }
        return null;
    }

    @Override
    public Map<String, Double> getPosition(String id) {
        Map<String, Double> position = new HashMap<>();

        UserEntity user = userRepository.findById(id).get();

        if(user.getArea()!=null){
            Double positionX = userRepository.findById(id).get().getAreaX();
            Double positionY = userRepository.findById(id).get().getAreaY();

            position.put("positionX", positionX);
            position.put("positionY", positionY);
        }else{
            position.put("positionX", null);
            position.put("positionY", null);
        }
        return position;
    }




}
