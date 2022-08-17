package com.gwangjubob.livealone.backend.service;

import com.gwangjubob.livealone.backend.domain.entity.MailEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserCategoryEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import com.gwangjubob.livealone.backend.domain.repository.MailRepository;
import com.gwangjubob.livealone.backend.domain.repository.UserCategoryRepository;
import com.gwangjubob.livealone.backend.domain.repository.UserRepository;
import com.gwangjubob.livealone.backend.dto.user.UserInfoDto;
import com.gwangjubob.livealone.backend.dto.user.UserMoreDTO;
import com.gwangjubob.livealone.backend.mapper.UserInfoMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@SpringBootTest
@Transactional
public class UserServiceTest {
    private UserRepository userRepository;
    private UserCategoryRepository userCategoryRepository;
    private UserInfoMapper userInfoMapper;
    private JavaMailSender javaMailSender;
    private MailRepository mailRepository;

    private static final String okay = "SUCCESS";
    private static final String fail = "FAIL";
    private final PasswordEncoder passwordEncoder;

    @Autowired
    UserServiceTest(UserRepository userRepository,JavaMailSender javaMailSender, MailRepository mailRepository,PasswordEncoder passwordEncoder, UserCategoryRepository userCategoryRepository, UserInfoMapper userInfoMapper){
        this.userRepository = userRepository;
        this.userCategoryRepository = userCategoryRepository;
        this.userInfoMapper = userInfoMapper;
        this.passwordEncoder = passwordEncoder;
        this.javaMailSender = javaMailSender;
        this.mailRepository = mailRepository;
    }

    @Test
    public void 내_정보_조회_테스트(){
        String id = "test";
        //when
        UserEntity user = userRepository.findById(id).get();
        if(user != null) {
            UserInfoDto userInfo = userInfoMapper.toDto(user);
            System.out.println(userInfo);
            System.out.println(okay);
        }
    }


    @Test
    public void 회원_가입(){
        String id = "usertest";
        String nickname = "비밀번호는usertest입니다.";
        String password = "usertest";
        UserEntity inputUser = UserEntity.builder()
                .id(id)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .build();
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            System.out.println(fail);
        } else{
            userRepository.save(inputUser);
            System.out.println(okay);
        }
    }

    @Test
    public void 회원_가입_추가_정보(){
        String id = "test";
        String area = "gwangju";
        List<String> categorys = new ArrayList<>();
        categorys.add("A");
        categorys.add("B");
        categorys.add("C");
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            UserEntity user = optionalUser.get();
            user.setArea(area);
            userRepository.save(user);
            List<UserCategoryEntity> delCategorys = userCategoryRepository.findByUser(user);
            for (UserCategoryEntity uc : delCategorys) {
                userCategoryRepository.delete(uc);
            }
            for (String c : categorys) {
                UserCategoryEntity usercategory = UserCategoryEntity.builder()
                        .category(c)
                        .user(user)
                        .build();
                userCategoryRepository.save(usercategory);
            }
            System.out.println(okay);
        } else{
            System.out.println(fail);
        }
    }

    @Test
    public void 닉네임_중복_체크(){
        String nickname = "nickname";
        Optional<UserEntity> optionalUser = userRepository.findByNickname(nickname);
        if(optionalUser.isPresent()){
            System.out.println(fail);
        } else{
            System.out.println(okay);
        }
    }

    @Test
    public void 회원_정보_수정(){
        String id = "test";
        String nickname = "test";
        String area = "test";
        Boolean followOpen = true;
        Boolean followerOpen = true;
        Boolean likeNotice = true;
        Boolean followNotice = true;
        Boolean commentNotice = true;
        Boolean replyNotice = true;
        String profileMsg = "test";
        byte[] profileImg = null;
        UserInfoDto userInfoDto = UserInfoDto.builder()
                .id(id)
                .nickname(nickname)
                .area(area)
                .followOpen(followOpen)
                .followerOpen(followerOpen)
                .likeNotice(likeNotice)
                .followNotice(followNotice)
                .commentNotice(commentNotice)
                .replyNotice(replyNotice)
                .profileMsg(profileMsg)
                .profileImg(profileImg)
                .build();
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()){
            UserEntity user = optionalUser.get();
            userInfoMapper.updateFromDto(userInfoDto, user);
            userRepository.save(user);
            userInfoDto = userInfoMapper.toDto(user);
            System.out.println(userInfoDto);
            System.out.println(okay);
        } else{
            System.out.println(fail);
        }
    }
    @Test
    public void 회원_비밀번호_수정(){
        String id = "test";
        String password = "test";
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            UserEntity user = optionalUser.get();
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            System.out.println(okay);
        } else{
            System.out.println(fail);
        }
    }
    @Test
    public void 회원_탈퇴(){
        String id = "test";
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            UserEntity user = optionalUser.get();
            userRepository.delete(user);
            System.out.println(okay);
        } else{
            System.out.println(fail);
        }
    }
    @Test
    public void 비밀번호_인증_테스트(){
        //given
        final String id = "test";
        final String password = "test";

        //when
        Optional<UserEntity> user = userRepository.findById(id);
        boolean res = passwordEncoder.matches(password,user.get().getPassword());

        //thens
        if(res){
            System.out.println("OK");
        }else {
            System.out.println("FAIL");
        }
    }
    @Test
    public void 로그인_테스트(){
        //given
        final String id = "test";
        final String password = "test";

        //when
        Optional<UserEntity> user = userRepository.findById(id);
        boolean res = passwordEncoder.matches(password,user.get().getPassword());

        //thens
        if(res){
            System.out.println("OK");
        }else {
            System.out.println("FAIL");
        }
    }

    @Disabled
    public void 이메일_전송_테스트(){
        //given
        SimpleMailMessage message = new SimpleMailMessage();
        String authKey = "231456";
        String toId = "1552419@gmail.com";
        String fromId = "gwangjubob@gmail.com";
        String subject = ("[인증테스트] 나 혼자 잘 산다.");
        //when
        message.setTo(toId);
        message.setFrom(fromId);
        message.setSubject(subject);
        message.setText("인증번호 : "+ authKey);
        MailEntity dummyMail = MailEntity.builder()
                .id(toId)
                .type(fromId)
                .number(authKey)
                .time(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
                .build();
        mailRepository.saveAndFlush(dummyMail);
        javaMailSender.send(message); //메일 전송
        MailEntity mail = MailEntity.builder()
                .id(toId)
                .type("0")
                .number(authKey)
                .time(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
                .build();
        mailRepository.saveAndFlush(mail);
        //thens
        System.out.println("OK");
    }
    @Test
    public void 이메일_인증_테스트(){
        //given
        String authKey = "917699";
        String id = "1552419@gmail.com";
        String type = "0";
        //when
        int res = mailRepository.findById(id,authKey, type);
        //thens
        if(res == 1){
        System.out.println("OK");

        }else{
        System.out.println("FAIL");

        }
    }

    @Test
    public void 회원_추가_정보_조회(){
        Map<String , Object> resultMap = new HashMap<>();
        String id = "ssafy";
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            UserEntity user = optionalUser.get();
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
            resultMap.put("data", data);
            resultMap.put("message", okay);
        } else{
            resultMap.put("message", fail);
        }
        System.out.println(resultMap);
    }
    @Test
    public void 주소를_좌표로_변환() {
        Map<String, Double> location = new HashMap<>();
        String testId = "test";
        UserEntity user = userRepository.findById(testId).get();

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
        //System.out.println("size 확인 :: " + size);

    }

    @Test
    public void 중간위치_주변_버스정류장_조회() {
        // given - 중간위치
        double doubleMidX = 126.8533768895765;
        double doubleMidY = 35.18480088833835; // DB에서 double로 받아온 값을 String으로 변환해줘야함

        ArrayList<List> station = new ArrayList<>(); // 버스정류장 정보 담을 리스트 생성
        String surl;
        URL url;
        HttpsURLConnection conn;
        BufferedReader br;
        String inputStr;
        StringBuilder sb;

        // when - api 조회
        try{
            // request params
            String midX = String.valueOf(doubleMidX);
            String midY = String.valueOf(doubleMidY);
            String apiKey = "sVVsoLKtRaVMwkTbiQfAPb3Dzbu/GeKVmpaAxqvSH0c"; // 인증키
            String radius = "500"; // 디폴트는 반경 500, 없으면 1000으로 바꿔서 조회

            surl = "https://api.odsay.com/v1/api/pointSearch?apiKey=" + apiKey
                    +"&x="+midX+"&y="+midY+"&radius="+radius; // ODsay 버정 조회 api

            url = new URL(surl);

            conn = (HttpsURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            sb = new StringBuilder();
            while((inputStr = br.readLine()) != null){
                sb.append(inputStr + "\n");
            }
            br.close();
            conn.disconnect();

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(sb.toString());

            JSONObject result = (JSONObject) json.get("result");
            long count = (long) result.get("count"); // 검색 결과 주변 버스정류장 개수 - 잘 받아옴
//            System.out.println("검색 결과 총 개수: " + count);
            if(count > 0) {
                JSONArray data = (JSONArray) result.get("station");

                for(int i=0; i<data.size(); i++){
                    JSONObject obj = (JSONObject) data.get(i);

                    List<Double> list = new ArrayList<>();
                    list.add(Double.parseDouble(obj.get("x").toString()));
                    list.add(Double.parseDouble(obj.get("y").toString()));

                    station.add(list);
                }


                for(List l : station){
                    System.out.println(l.get(0));
                    System.out.println(l.get(1));
                }
            }else{ // 조회 결과 없으면 반경 1000으로 변경
                radius = "1000";
                surl = "https://api.odsay.com/v1/api/pointSearch?apiKey=" + apiKey
                        +"&x="+midX+"&y="+midY+"&radius="+radius; // ODsay 버정 조회 api

                url = new URL(surl);

                conn = (HttpsURLConnection) url.openConnection();

                conn.setRequestMethod("GET");

                br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                sb = new StringBuilder();
                while((inputStr = br.readLine()) != null){
                    sb.append(inputStr + "\n");
                }
                br.close();
                conn.disconnect();

                JSONParser parser2 = new JSONParser();
                JSONObject json2 = (JSONObject) parser2.parse(sb.toString());
                JSONObject result2 = (JSONObject) json2.get("result");
                long count2 = (long) result2.get("count");
                if(count2 > 0) {
                    JSONArray data = (JSONArray) result2.get("station");

                    for(int i=0; i<data.size(); i++){
                        JSONObject obj = (JSONObject) data.get(i);

                        List<Double> list = new ArrayList<>();
                        list.add(Double.parseDouble(obj.get("x").toString()));
                        list.add(Double.parseDouble(obj.get("y").toString()));

                        station.add(list);
                    }


                    for(List l : station){
                        System.out.println(l.get(0));
                        System.out.println(l.get(1));
                    }
                }
//                System.out.println(result2.toString());
            }


        }catch (Exception e){
            e.printStackTrace();
            System.out.println("검색 실패.... T.T");
        }

        // 내 위치, 상대방 위치랑 중간위치 주변 버스 정류장 중 최단 시간이 걸리는 정류장 고르기.
        for(int i=0; i<station.size(); i++){
            Double x = (Double) station.get(i).get(0); // 버정 x좌표
            Double y = (Double) station.get(i).get(1); // 버정 y좌표

            System.out.println((i+1)+ " : " +x + "," +y);
        }
    }

    @Test
    public void 사용자와_버스정류장들중_최단거리_구하기() {
        // given
        Double loginUserX = 126.877498406332;
        Double loginUserY = 35.1841322155411;

        Double targetUserX = 126.829255372821;
        Double targetUserY = 35.1854695611356;

        ArrayList<List> station = new ArrayList<>();
        List<Double> list1 = new ArrayList<>();
        list1.add(126.85769);
        list1.add(35.189346);
        List<Double> list2 = new ArrayList<>();
        list2.add(126.85758);
        list2.add(35.189686);
        List<Double> list3 = new ArrayList<>();
        list3.add(126.85134);
        list3.add(35.1917);
        List<Double> list4 = new ArrayList<>();
        list4.add(126.851555);
        list4.add(35.19183);
        List<Double> list5 = new ArrayList<>();
        list5.add(126.8608);
        list5.add(35.17906);
        List<Double> list6 = new ArrayList<>();
        list6.add(126.860794);
        list6.add(35.179);
        station.add(list1);
        station.add(list2);
        station.add(list3);
        station.add(list4);
        station.add(list5);
        station.add(list6);

        for(int i=0; i<station.size(); i++){
            List<Long> loginUserTime = new ArrayList<>();
            List<Long> targetUserTime = new ArrayList<>();
            Double midX = (Double) station.get(i).get(0); // 하나씩 조회하며 시간 구하기
            Double midY = (Double) station.get(i).get(1);

            String loginUserSX = String.valueOf(loginUserX);
            String loginUserSY = String.valueOf(loginUserY);

            String targetUserSX = String.valueOf(targetUserX);
            String targetUserSY = String.valueOf(targetUserY);

            String EX = String.valueOf(midX);
            String EY = String.valueOf(midY);

            String apiKey = "sVVsoLKtRaVMwkTbiQfAPb3Dzbu/GeKVmpaAxqvSH0c";
            String surl = "https://api.odsay.com/v1/api/searchPubTransPathT?apiKey="+apiKey+"&SX="+loginUserSX+"&SY="+loginUserSY+"&EX="+EX+"&EY="+EY;
            String surl2 = "https://api.odsay.com/v1/api/searchPubTransPathT?apiKey="+apiKey+"&SX="+targetUserSX+"&SY="+targetUserSY+"&EX="+EX+"&EY="+EY;

            try{
                URL url = new URL(surl);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

                conn.setRequestMethod("GET");

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),  "UTF-8"));
                String inputStr;
                StringBuilder sb = new StringBuilder();
                while((inputStr = br.readLine()) != null){
                    sb.append(inputStr);
                }
                br.close();
                conn.disconnect();

                JSONParser parser = new JSONParser();
                JSONObject json = (JSONObject) parser.parse(sb.toString());

                JSONObject result = (JSONObject) json.get("result");
                System.out.println(result.toString());
                JSONArray paths = (JSONArray) result.get("path"); // 여기서 최단 시간 찾아야함

                JSONObject path = (JSONObject) paths.get(0);
                JSONObject info = (JSONObject) path.get("info");

                Long minTime = Long.parseLong(info.get("totalTime").toString());

                for(int j=1; j<paths.size(); j++){
                    path = (JSONObject) paths.get(j);
                    info = (JSONObject) path.get("info");

                    Long totalTime = Long.parseLong(info.get("totalTime").toString());
                    if(minTime > totalTime) minTime = totalTime;
                }
                System.out.println("최단시간 : " + minTime);

                loginUserTime.add(minTime);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}

