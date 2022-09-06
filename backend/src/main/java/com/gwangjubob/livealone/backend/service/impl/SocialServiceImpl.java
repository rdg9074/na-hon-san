package com.gwangjubob.livealone.backend.service.impl;

import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import com.gwangjubob.livealone.backend.domain.repository.UserRepository;
import com.gwangjubob.livealone.backend.service.SocialService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class SocialServiceImpl implements SocialService {
    private UserRepository userRepository;
    @Autowired
    SocialServiceImpl(UserRepository userRepository ){
        this.userRepository = userRepository;
    }
    @Override
    @Transactional
    public String[] kakaoLogin(String authToken) {
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        String[] jsonId = new String[2];
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + authToken);
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(result);
            jsonId[0] = json.get("id").toString();
            String nameId = "k" + jsonId[0].substring(1, 10);
            jsonId[0] = "k" + jsonId[0].substring(0,10);
            //DB에 회원인지 찾기
            Optional<UserEntity> user = userRepository.findById(jsonId[0]);
            if (!user.isPresent()) { //존재하지 않는다면
                UserEntity userRegist = UserEntity.builder()
                        .id(jsonId[0])
                        .password("social")
                        .nickname(nameId)
                        .social("kakao")
                        .build();
                userRepository.save(userRegist);
                jsonId[1] = "true";
            }else{
                jsonId[1] = "false";
            }


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return jsonId;
    }
    public String naverOAuthRedirect(String code, String state) throws ParseException {
        // RestTemplate 인스턴스 생성
        RestTemplate rt = new RestTemplate();

        HttpHeaders accessTokenHeaders = new HttpHeaders();
        accessTokenHeaders.add("Content-type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> accessTokenParams = new LinkedMultiValueMap<>();
        accessTokenParams.add("grant_type", "authorization_code");
        accessTokenParams.add("client_id", "KZdIc1vBH76pe642scIQ");
        accessTokenParams.add("client_secret", "94o80Dc82x");
        accessTokenParams.add("code" , code);	// 응답으로 받은 코드
        accessTokenParams.add("state" , state); // 응답으로 받은 상태

        HttpEntity<MultiValueMap<String, String>> accessTokenRequest = new HttpEntity<>(accessTokenParams, accessTokenHeaders);

        ResponseEntity<String> accessTokenResponse = rt.exchange(
                "https://nid.naver.com/oauth2.0/token",
                HttpMethod.POST,
                accessTokenRequest,
                String.class
        );
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(accessTokenResponse.getBody());
        return (String) json.get("access_token");
    }

    @Override
    public String[] naverLogin(String authToken) throws ParseException {
        String accessToken = naverOAuthRedirect(authToken,"nahonjan");
        String reqURL = "https://openapi.naver.com/v1/nid/me";
        String[] jsonId = new String[2];
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(result);
            JSONObject jsonObjectB = (JSONObject) json.get("response");
            jsonId[0] = jsonObjectB.get("id").toString();
            String nameId = "n" + jsonId[0].substring(1, 10);
            jsonId[0] = "n" + jsonId[0].substring(0,20);
            //DB에 회원인지 찾기
            Optional<UserEntity> user = userRepository.findById(jsonId[0]);
            if (!user.isPresent()) { //존재하지 않는다면
                UserEntity userRegist = UserEntity.builder()
                        .id(jsonId[0])
                        .password("social")
                        .nickname(nameId)
                        .social("naver")
                        .build();
                userRepository.save(userRegist);
                jsonId[1] = "true";
            }else{
                jsonId[1] = "false";
            }


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        return jsonId;
    }

    @Override
    public String[] googleLogin(String authToken) {


        String reqURL = "https://www.googleapis.com/oauth2/v1/userinfo";
        String[] jsonId = new String[2];
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            //    요청에 필요한 Header에 포함될 내용
            conn.setRequestProperty("Authorization", "Bearer " + authToken);
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(result);
            jsonId[0] = json.get("id").toString();
            String nameId = "g" + jsonId[0].substring(1, 10);
            jsonId[0] = "g" + jsonId[0].substring(0,20);
            //DB에 회원인지 찾기
            Optional<UserEntity> user = userRepository.findById(jsonId[0]);
            if (!user.isPresent()) { //존재하지 않는다면
                UserEntity userRegist = UserEntity.builder()
                        .id(jsonId[0])
                        .password("social")
                        .nickname(nameId)
                        .social("google")
                        .build();
                userRepository.save(userRegist);
                jsonId[1] = "true";
            }else{
                jsonId[1] = "false";
            }


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return jsonId;
    }
}
