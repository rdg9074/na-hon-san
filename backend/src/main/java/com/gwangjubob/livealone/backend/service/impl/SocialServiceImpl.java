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
//    public static JSONParser jsonParser;
    @Override
    @Transactional
    public String kakaoLogin(String authToken) {
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        String jsonId;
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            //    요청에 필요한 Header에 포함될 내용
            conn.setRequestProperty("Authorization", "Bearer " + authToken);
            int responseCode = conn.getResponseCode();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(result);
            JSONObject jsonObjectB = (JSONObject) json.get("properties");
            jsonId = json.get("id").toString();
            String jsonName = jsonObjectB.get("nickname").toString();
//            String IMAGE_URL = (String) jsonObjectB.get("thumbnail_image");
            String nameId = jsonName + jsonId.substring(0, 4);
            jsonId = "kakao" + jsonId;
            //DB에 회원인지 찾기
            Optional<UserEntity> user = userRepository.findByNickname(nameId);
            if (!user.isPresent()) { //존재하지 않는다면
                UserEntity userRegist = UserEntity.builder()
                        .id(jsonId)
                        .password("social")
                        .nickname(nameId)
                        .social("kakao")
                        .build();
                userRepository.save(userRegist);

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
    public String naverLogin(String authToken) throws ParseException {
        String accessToken = naverOAuthRedirect(authToken,"nahonjan");
        String reqURL = "https://openapi.naver.com/v1/nid/me";
        String jsonId;
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            //    요청에 필요한 Header에 포함될 내용
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            int responseCode = conn.getResponseCode();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(result);
            JSONObject jsonObjectB = (JSONObject) json.get("response");
            jsonId = jsonObjectB.get("id").toString();
            String jsonName = jsonObjectB.get("nickname").toString();
//            String IMAGE_URL = (String) jsonObjectB.get("thumbnail_image");
            String nameId = jsonName + jsonId.substring(0, 4);
            jsonId = "naver" + jsonId;
            //DB에 회원인지 찾기
            Optional<UserEntity> user = userRepository.findByNickname(nameId);
            if (!user.isPresent()) { //존재하지 않는다면
                UserEntity userRegist = UserEntity.builder()
                        .id(jsonId)
                        .password("social")
                        .nickname(nameId)
                        .social("naver")
                        .build();
                userRepository.save(userRegist);

            }


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        return jsonId;
    }

    @Override
    public String googleLogin(String authToken) {


        String reqURL = "https://www.googleapis.com/oauth2/v1/userinfo";
        String jsonId = null;
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            //    요청에 필요한 Header에 포함될 내용
            conn.setRequestProperty("Authorization", "Bearer " + authToken);
            int responseCode = conn.getResponseCode();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(result);
            jsonId = json.get("id").toString();
            String jsonName = json.get("name").toString();
//            String IMAGE_URL = (String) jsonObjectB.get("thumbnail_image");
            String nameId = jsonName + jsonId.substring(0, 4);
            jsonId = "google" + jsonId;
            //DB에 회원인지 찾기
            Optional<UserEntity> user = userRepository.findByNickname(nameId);
            if (!user.isPresent()) { //존재하지 않는다면
                UserEntity userRegist = UserEntity.builder()
                        .id(jsonId)
                        .password("social")
                        .nickname(nameId)
                        .social("google")
                        .build();
                userRepository.save(userRegist);

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
