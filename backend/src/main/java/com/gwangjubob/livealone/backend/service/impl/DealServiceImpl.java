package com.gwangjubob.livealone.backend.service.impl;


import com.gwangjubob.livealone.backend.domain.entity.*;
import com.gwangjubob.livealone.backend.domain.repository.*;
import com.gwangjubob.livealone.backend.dto.Deal.DealCommentDto;
import com.gwangjubob.livealone.backend.dto.Deal.DealDto;
import com.gwangjubob.livealone.backend.dto.Deal.DealRequestDto;
import com.gwangjubob.livealone.backend.dto.Deal.DealViewDto;
import com.gwangjubob.livealone.backend.mapper.DealCommentMapper;
import com.gwangjubob.livealone.backend.mapper.DealMapper;
import com.gwangjubob.livealone.backend.mapper.DealViewMapper;
import com.gwangjubob.livealone.backend.service.DealService;
import com.gwangjubob.livealone.backend.service.UserService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class DealServiceImpl implements DealService {
    private DealRepository dealRepository;
    private DealMapper dealMapper;
    private UserRepository userRepository;

    private DealCommentRepository dealCommentRepository;
    private UserLikeDealsRepository userLikeDealsRepository;
    private NoticeRepository noticeRepository;
    private DealCommentMapper dealCommentMapper;
    private DealViewMapper dealViewMapper;
    private UserService userService;
    @Autowired
    DealServiceImpl(DealRepository dealRepository, DealMapper dealMapper, UserRepository userRepository, DealCommentRepository dealCommentRepository, UserService userService,
                    NoticeRepository noticeRepository, DealCommentMapper dealCommentMapper, UserLikeDealsRepository userLikeDealsRepository, DealViewMapper dealViewMapper){
        this.dealRepository = dealRepository;
        this.dealMapper = dealMapper;
        this.userRepository = userRepository;
        this.dealCommentRepository = dealCommentRepository;
        this.dealCommentMapper = dealCommentMapper;
        this.userLikeDealsRepository = userLikeDealsRepository;
        this.noticeRepository = noticeRepository;
        this.dealViewMapper = dealViewMapper;
        this.userService = userService;
    }


    @Override
    public DealDto registDeal(DealDto dealDto) {
        Optional<UserEntity> optionalUser = userRepository.findById(dealDto.getUserId());
        DealDto data = new DealDto();
        if(optionalUser.isPresent()){
            UserEntity user = optionalUser.get();
            DealEntity deal = dealMapper.toEntity(dealDto);
            deal.setUser(user);
            String area = user.getArea().split(" ")[0];
            deal.setArea(area);
            deal.setTime(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
            dealRepository.save(deal);
            data = dealMapper.toDto(deal);
            data.setUserNickname(deal.getUser().getNickname());
            data.setUserId(deal.getUser().getId());
            data.setUserProfileImg(user.getProfileImg());
        } else{
            data = null;
        }
        return data;
    }

    @Override
    public DealDto viewDetailDeal(Integer idx) {
        Optional<DealEntity> optionalDeal = dealRepository.findById(idx);
        DealDto data = new DealDto();
        if(optionalDeal.isPresent()){
            DealEntity deal = optionalDeal.get();
            data = dealMapper.toDto(deal);
            data.setLikes(deal.getLikes());
            data.setUserNickname(deal.getUser().getNickname());
            data.setUserId(deal.getUser().getId());
            data.setUserProfileImg(deal.getUser().getProfileImg());
        } else{
            data = null;
        }
        return data;
    }

    @Override
    public List<DealCommentDto> viewDealComment(Integer idx) {
        Optional<DealEntity> optionalDeal = dealRepository.findById(idx);
        List<DealCommentDto> result = null;
        if(optionalDeal.isPresent()){
            DealEntity deal = optionalDeal.get();
            List<DealCommentEntity> dealCommentEntity = dealCommentRepository.findByDealOrderByComment(deal);
            if(!dealCommentEntity.isEmpty()){
                result = new ArrayList<>();
                for (DealCommentEntity d : dealCommentEntity){
                    DealCommentDto dto = dealCommentMapper.toDto(d);
                    dto.setUserId(d.getUser().getId());
                    dto.setPostIdx(d.getDeal().getIdx());
                    dto.setUserNickname(d.getUser().getNickname());
                    dto.setUserProfileImg(d.getUser().getProfileImg());
                    result.add(dto);
                }
            }
        }
        return result;
    }


    @Override
    public DealDto updateDeal(Integer idx, DealDto dealDto, String decodeId) {
        Optional<DealEntity> optionalDeal = dealRepository.findById(idx);
        Optional<UserEntity> optionalUser = userRepository.findById(decodeId);
        DealDto data = new DealDto();
        if(optionalDeal.isPresent() && optionalUser.isPresent()){
            DealEntity deal = optionalDeal.get();
            UserEntity user = optionalUser.get();
            if(deal.getUser().getId().equals(user.getId())){
                dealMapper.updateFromDto(dealDto, deal);
                deal.setUpdateTime(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
                DealEntity res =dealRepository.save(deal);
                data = dealMapper.toDto(res);
            } else{
                data = null;
            }
        } else {
            data = null;
        }
        return data;
    }

    @Override
    public boolean deleteDeal(Integer idx, String decodeId) {
        Optional<DealEntity> optionalDeal = dealRepository.findById(idx);
        Optional<UserEntity> optionalUser = userRepository.findById(decodeId);
        if(optionalDeal.isPresent() && optionalUser.isPresent()){
            DealEntity deal = optionalDeal.get();
            UserEntity user = optionalUser.get();
            if(deal.getUser().getId().equals(user.getId())){
                dealRepository.delete(deal);
                return true;
            }
            return false;
        } else{
            return false;
        }
    }

    @Override
    public DealCommentDto registDealComment(DealCommentDto dealCommentDto) {
        Optional<UserEntity> optionalUser = userRepository.findById(dealCommentDto.getUserId());
        Optional<DealEntity> optionalDeal = dealRepository.findById(dealCommentDto.getPostIdx());
        DealCommentDto data = new DealCommentDto();
        if(optionalUser.isPresent() && optionalDeal.isPresent()){
            UserEntity user = optionalUser.get();
            DealEntity deal = optionalDeal.get();
            DealCommentEntity dealComment = dealCommentMapper.toEntity(dealCommentDto);
            dealComment.setUser(user);
            dealComment.setDeal(deal);
            dealComment.setTime(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
            dealCommentRepository.save(dealComment);
            data = dealCommentMapper.toDto(dealComment);
            data.setUserNickname(dealComment.getUser().getNickname());
            data.setUserId(dealComment.getUser().getId());
            data.setPostIdx(dealComment.getDeal().getIdx());

            deal.setComment(deal.getComment() + 1);
            dealRepository.save(deal);

            if(dealComment.getUpIdx() == 0 && !deal.getUser().getId().equals(user.getId())){ // 댓글 알림 등록
                NoticeEntity notice = NoticeEntity.builder()
                        .noticeType("comment")
                        .user(deal.getUser()) // 글작성자
                        .fromUserId(user.getId())
                        .postType("deal")
                        .commentIdx(dealComment.getIdx())
                        .time(dealComment.getTime())
                        .postIdx(deal.getIdx())
                        .build();

                noticeRepository.save(notice);
            }

            if(dealComment.getUpIdx() != 0){
                DealCommentEntity upDealComment = dealCommentRepository.findByIdx(dealComment.getUpIdx()).get();
                if(!upDealComment.getUser().getId().equals(user.getId())){
                    NoticeEntity notice = NoticeEntity.builder()
                            .noticeType("reply")
                            .user(upDealComment.getUser()) // 댓글작성자
                            .fromUserId(user.getId())
                            .postType("deal")
                            .commentIdx(dealComment.getIdx())
                            .commentUpIdx(dealComment.getUpIdx())
                            .time(dealComment.getTime())
                            .postIdx(deal.getIdx())
                            .build();

                    noticeRepository.save(notice);
                }
            }
        } else{
            data = null;
        }
        return data;
    }

    @Override
    public DealCommentDto updateDealComment(Integer idx, DealCommentDto dealCommentDto) {
        Optional<DealCommentEntity> optionalDealComment = dealCommentRepository.findById(idx);
        Optional<UserEntity> optionalUser = userRepository.findById(dealCommentDto.getUserId());
        DealCommentDto data = new DealCommentDto();
        if(optionalDealComment.isPresent() && optionalUser.isPresent()){
            DealCommentEntity dealComment = optionalDealComment.get();
            UserEntity user = optionalUser.get();
            if(dealComment.getUser().getId().equals(user.getId())){
                dealComment.setUpdateTime(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
                dealCommentMapper.updateFromDto(dealCommentDto, dealComment);
                dealCommentRepository.save(dealComment);
                data = dealCommentMapper.toDto(dealComment);
                data.setUserNickname(dealComment.getUser().getNickname());
            } else{
                data = null;
            }
        } else{
            data = null;
        }
        return data;
    }

    @Override
    public void deleteDealComment(Integer idx, String userId) {
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        Optional<DealCommentEntity> optionalDealComment = dealCommentRepository.findByIdx(idx);
        if(optionalDealComment.isPresent() && optionalUser.isPresent()){
            DealCommentEntity dealComment = optionalDealComment.get();
            DealEntity deal = dealRepository.findById(dealComment.getDeal().getIdx()).get();
            UserEntity user = userRepository.findById(userId).get();

            if(user.getNickname().equals(dealComment.getUser().getNickname())){
                if(dealComment.getUpIdx() != 0){
                    if(deal.getComment() > 0){
                        deal.setComment(deal.getComment() - 1);
                    } else {
                        deal.setComment(0);
                    }

                    dealRepository.save(deal);

                    dealCommentRepository.delete(dealComment);

                    Optional<NoticeEntity> notice = noticeRepository.findByNoticeTypeAndFromUserIdAndPostTypeAndPostIdxAndCommentIdx("reply", user.getId(), "deal", deal.getIdx(), dealComment.getIdx());
                    if(notice.isPresent()){
                        noticeRepository.delete(notice.get());
                    }
                } else {
                    List<DealCommentEntity> replyCommentList = dealCommentRepository.findByUpIdx(idx);
                    int size = replyCommentList.size();

                    if(!replyCommentList.isEmpty()) {
                        if(deal.getComment() - size > 0){
                            deal.setComment(deal.getComment() - size);
                        } else{
                            deal.setComment(0);
                        }
                        dealRepository.save(deal);

                        dealCommentRepository.deleteAllInBatch(replyCommentList);

                        List<NoticeEntity> noticeList = noticeRepository.findAllByNoticeTypeAndPostTypeAndPostIdxAndCommentUpIdx("reply", "deal", deal.getIdx(), dealComment.getIdx());

                        if (!noticeList.isEmpty()) {
                            noticeRepository.deleteAllInBatch(noticeList);
                        }
                    }
                    if(deal.getComment() > 0){
                        deal.setComment(deal.getComment() - 1);
                    } else {
                        deal.setComment(0);
                    }
                    dealRepository.save(deal);
                    dealCommentRepository.delete(dealComment);
                    Optional<NoticeEntity> notice = noticeRepository.findByNoticeTypeAndPostTypeAndPostIdxAndCommentIdx("comment", "deal", deal.getIdx(), dealComment.getIdx());
                    if(notice.isPresent()){
                        noticeRepository.delete(notice.get());
                    }
                }
            }
        }
    }

    @Override
    public boolean likeDeal(Integer idx, String userId) {
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        Optional<DealEntity> optionalDeal = dealRepository.findById(idx);
        if(optionalUser.isPresent() && optionalDeal.isPresent()){
            UserEntity user = optionalUser.get();
            DealEntity deal = optionalDeal.get();
            Optional<UserLikeDealsEntity> optionalUserLikeDeals = userLikeDealsRepository.findByDealAndUser(deal, user);
            if (optionalUserLikeDeals.isPresent()){
                UserLikeDealsEntity userLikeDeals = optionalUserLikeDeals.get();
                userLikeDealsRepository.delete(userLikeDeals);
                deal.setLikes(deal.getLikes() - 1);
                dealRepository.save(deal);

                Optional<NoticeEntity> notice = noticeRepository.findByNoticeTypeAndFromUserIdAndPostTypeAndPostIdx("like",user.getId(),"deal",deal.getIdx());
                if(notice.isPresent()){
                    noticeRepository.delete(notice.get());
                }
            } else{
                UserLikeDealsEntity userLikeDeals = UserLikeDealsEntity
                        .builder()
                        .deal(deal)
                        .user(user)
                        .time(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
                        .build();
                userLikeDealsRepository.save(userLikeDeals);
                deal.setLikes(deal.getLikes() + 1);
                dealRepository.save(deal);

                NoticeEntity notice = NoticeEntity.builder()
                        .noticeType("like")
                        .user(deal.getUser())
                        .fromUserId(user.getId())
                        .postType("deal")
                        .postIdx(deal.getIdx())
                        .time(userLikeDeals.getTime())
                        .build();

                noticeRepository.save(notice);
            }
            return true;
        } else{
            return false;
        }
    }

    @Override
    public boolean countUpView(Integer idx) {
        Optional<DealEntity> optionalDeal = dealRepository.findById(idx);
        if(optionalDeal.isPresent()){
            DealEntity deal = optionalDeal.get();
            deal.setView(deal.getView() + 1);
            dealRepository.save(deal);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Map<String, Object> viewDeal(DealRequestDto dealRequestDto) {
        String keyword = dealRequestDto.getKeyword();
        String state = dealRequestDto.getState();
        String type = dealRequestDto.getType();
        String area = dealRequestDto.getArea();
        Integer pageSize = dealRequestDto.getPageSize();
        List<String> categorys = dealRequestDto.getCategorys();
        Integer lastIdx = dealRequestDto.getLastIdx();
        Integer lastView = dealRequestDto.getLastView();
        Integer lastLikes = dealRequestDto.getLastLikes();
        List<DealViewDto> list = new ArrayList<>();
        Map<String, Object> data = new HashMap<>();
        if(lastLikes == null) {
            lastLikes = dealRepository.findTop1ByOrderByLikesDesc().get().getLikes() + 1;
        }
        if(lastView == null){
            lastView = dealRepository.findTop1ByOrderByViewDesc().get().getView() + 1;
        }
        if(lastIdx == null){
            lastIdx = dealRepository.findTop1ByOrderByIdxDesc().get().getIdx() + 1;
        }
        Slice<DealEntity> deals = null;
        Pageable pageable = null;
        Sort sortIdx = Sort.by(
                Sort.Order.desc("idx")
        );
        Sort sortLikes = Sort.by(
                Sort.Order.desc("likes"),
                Sort.Order.desc("idx")
        );
        Sort sortView = Sort.by(
                Sort.Order.desc("view"),
                Sort.Order.desc("idx")
        );
        if (area != null){
            if(categorys.contains("전체")){
                if(keyword == null){
                    if (type.equals("조회순")){
                        pageable = PageRequest.of(0, pageSize, sortView);
                        deals = dealRepository.findViewArea(state, lastIdx, lastView, area, pageable);
                    } else if (type.equals("좋아요순")){
                        pageable = PageRequest.of(0, pageSize, sortLikes);
                        deals = dealRepository.findlikesArea(state,lastIdx, lastLikes, area, pageable);
                    } else{
                        pageable = PageRequest.of(0, pageSize, sortIdx);
                        deals = dealRepository.findIdxArea(state, lastIdx, area, pageable);
                    }
                } else{
                    if (type.equals("조회순")){
                        pageable = PageRequest.of(0, pageSize, sortView);
                        deals = dealRepository.findTitleViewArea(state, keyword, lastIdx, lastView, area, pageable);
                    } else if (type.equals("좋아요순")){
                        pageable = PageRequest.of(0, pageSize, sortLikes);
                        deals = dealRepository.findTitleLikesArea(state, keyword, lastIdx, lastLikes, area, pageable);
                    } else{
                        pageable = PageRequest.of(0, pageSize, sortIdx);
                        deals = dealRepository.findTitleIdxArea(state, keyword, lastIdx, area, pageable);
                    }
                }
            } else{
                if(keyword == null){
                    if(type.equals("조회순")){
                        pageable = PageRequest.of(0, pageSize, sortView);
                        deals = dealRepository.findCategoryViewArea(state, categorys, lastIdx, lastView, area, pageable);
                    } else if (type.equals("좋아요순")){
                        pageable = PageRequest.of(0, pageSize, sortLikes);
                        deals = dealRepository.findCategoryLikesArea(state, categorys, lastIdx, lastLikes, area,pageable);
                    } else{
                        pageable = PageRequest.of(0, pageSize, sortIdx);
                        deals = dealRepository.findCategoryIdxArea(state, categorys,lastIdx, area, pageable);
                    }
                } else{
                    if(type.equals("조회순")){
                        pageable = PageRequest.of(0, pageSize, sortView);
                        deals = dealRepository.findCategoryTitleViewArea(state, categorys, keyword, lastIdx, lastView, area, pageable);
                    } else if (type.equals("좋아요순")){
                        pageable = PageRequest.of(0, pageSize, sortLikes);
                        deals = dealRepository.findCategoryTitleLikesArea(state, categorys, keyword, lastIdx, lastLikes, area, pageable);
                    } else{
                        pageable = PageRequest.of(0, pageSize, sortIdx);
                        deals = dealRepository.findCategoryTitleIdxArea(state, categorys,keyword, lastIdx, area, pageable);
                    }
                }
            }
        } else{
            if(categorys.contains("전체")){
                if(keyword == null){
                    if (type.equals("조회순")){
                        pageable = PageRequest.of(0, pageSize, sortView);
                        deals = dealRepository.findView(state, lastIdx, lastView, pageable);
                    } else if (type.equals("좋아요순")){
                        pageable = PageRequest.of(0, pageSize, sortLikes);
                        deals = dealRepository.findlikes(state,lastIdx, lastLikes,  pageable);
                    } else{
                        pageable = PageRequest.of(0, pageSize, sortIdx);
                        deals = dealRepository.findIdx(state, lastIdx, pageable);
                    }
                } else{
                    if (type.equals("조회순")){
                        pageable = PageRequest.of(0, pageSize, sortView);
                        deals = dealRepository.findTitleView(state, keyword, lastIdx, lastView, pageable);
                    } else if (type.equals("좋아요순")){
                        pageable = PageRequest.of(0, pageSize, sortLikes);
                        deals = dealRepository.findTitleLikes(state, keyword, lastIdx, lastLikes, pageable);
                    } else{
                        pageable = PageRequest.of(0, pageSize, sortIdx);
                        deals = dealRepository.findTitleIdx(state, keyword, lastIdx, pageable);
                    }
                }
            } else{
                if(keyword == null){
                    if(type.equals("조회순")){
                        pageable = PageRequest.of(0, pageSize, sortView);
                        deals = dealRepository.findCategoryView(state, categorys, lastIdx, lastView, pageable);
                    } else if (type.equals("좋아요순")){
                        pageable = PageRequest.of(0, pageSize, sortLikes);
                        deals = dealRepository.findCategoryLikes(state, categorys, lastIdx, lastLikes, pageable);
                    } else{
                        pageable = PageRequest.of(0, pageSize, sortIdx);
                        deals = dealRepository.findCategoryIdx(state, categorys,lastIdx, pageable);
                    }
                } else{
                    if(type.equals("조회순")){
                        pageable = PageRequest.of(0, pageSize, sortView);
                        deals = dealRepository.findCategoryTitleView(state, categorys, keyword, lastIdx, lastView, pageable);
                    } else if (type.equals("좋아요순")){
                        pageable = PageRequest.of(0, pageSize, sortLikes);
                        deals = dealRepository.findCategoryTitleLikes(state, categorys, keyword, lastIdx, lastLikes, pageable);
                    } else{
                        pageable = PageRequest.of(0, pageSize, sortIdx);
                        deals = dealRepository.findCategoryTitleIdx(state, categorys,keyword, lastIdx, pageable);
                    }
                }
            }
        }
        if(deals != null){
            List<DealEntity> dealsList = deals.getContent();
            for (DealEntity res : dealsList){
                DealViewDto dto = dealViewMapper.toDto(res);
                dto.setUserNickname(res.getUser().getNickname());
                dto.setUserProfileImg(res.getUser().getProfileImg());
                list.add(dto);
            }
            data.put("list", list);
            data.put("hasNext", deals.hasNext());
        } else{
            data = null;
        }
        return data;
    }

    @Override
    public Boolean clickLikeButton(String decodeId, Integer idx) {
        UserEntity user = userRepository.findById(decodeId).get();
        DealEntity deal = dealRepository.findByIdx(idx).get();

        if(userLikeDealsRepository.findByDealAndUser(deal,user).isPresent()){
            return true;
        }
        return false;
    }
    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        if (unit == "kilometer") {
            dist = dist * 1.609344;
        } else if(unit == "meter"){
            dist = dist * 1609.344;
        }

        return (dist);
    }
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    @Override
    public Map<String, Object> searchMidPosition(String loginUserId, String targetUserId) {
        Map<String, Object> info = new HashMap<>();
        ArrayList<Long> loginUserTime;
        ArrayList<Long> targetUserTime;
        ArrayList<List> busStation = new ArrayList<>();
        ArrayList<List> busStationList = new ArrayList<>();

        Double loginUserX = userService.getPosition(loginUserId).get("positionX");
        Double loginUserY = userService.getPosition(loginUserId).get("positionY");

        Double targetUserX = userService.getPosition(targetUserId).get("positionX");
        Double targetUserY = userService.getPosition(targetUserId).get("positionY");

        Double distanceMeter = distance(loginUserY, loginUserX, targetUserY, targetUserX, "meter");

        if (distanceMeter <= 2000) {
            System.out.println("두 사용자 간의 거리가 2km 이내입니다. ");
            info.put("distance", distanceMeter);

            double midXd = (loginUserX + targetUserX) / 2;
            double midYd = (loginUserY + targetUserY) / 2;

            info.put("midXPosition", midXd);
            info.put("midYPosition", midYd);
        } else {
            double midXd = (loginUserX + targetUserX) / 2;
            double midYd = (loginUserY + targetUserY) / 2;

            info.put("midXPosition", midXd);
            info.put("midYPosition", midYd);

            String surl;
            URL url;
            HttpsURLConnection conn;
            BufferedReader br;
            String inputStr;
            StringBuilder sb;

            try {
                String midX = String.valueOf(midXd);
                String midY = String.valueOf(midYd);
//                String apiKey = "sVVsoLKtRaVMwkTbiQfAPb3Dzbu/GeKVmpaAxqvSH0c";
//                String apiKey = "4TDg6wbzkEuH7hP6AnSu2xCEA+3lLmjCH/hAEGbAnUw";
//                String apiKey = "jq+RtpFslnVWy1+I0ZAKnxM2eYmrCAwqtgtu/q9LFBw";
                String apiKey = "tfCyZK7P7irQVoasYK5ZXqZKLWs6NrllNfNG3RSSnRg";
                String radius = "500";

                surl = "https://api.odsay.com/v1/api/pointSearch?apiKey=" + apiKey
                        + "&x=" + midX + "&y=" + midY + "&radius=" + radius;

                url = new URL(surl);

                conn = (HttpsURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                sb = new StringBuilder();
                while ((inputStr = br.readLine()) != null) {
                    sb.append(inputStr + "\n");
                }
                br.close();
                conn.disconnect();

                JSONParser parser = new JSONParser();
                JSONObject json = (JSONObject) parser.parse(sb.toString());

                JSONObject resultObj = (JSONObject) json.get("result");
                long count = (long) resultObj.get("count");
                if (count > 0) {
                    info.put("radius", radius);
                    JSONArray data = (JSONArray) resultObj.get("station");

                    for (int i = 0; i < data.size(); i++) {
                        JSONObject obj = (JSONObject) data.get(i);
                        List<Double> list = new ArrayList<>();
                        list.add(Double.parseDouble(obj.get("x").toString()));
                        list.add(Double.parseDouble(obj.get("y").toString()));

                        busStation.add(list);
                    }
                } else {
                    radius = "1000";
                    info.put("radius", radius);

                    surl = "https://api.odsay.com/v1/api/pointSearch?apiKey=" + apiKey
                            + "&x=" + midX + "&y=" + midY + "&radius=" + radius; // ODsay 버정 조회 api

                    url = new URL(surl);

                    conn = (HttpsURLConnection) url.openConnection();

                    conn.setRequestMethod("GET");

                    br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    sb = new StringBuilder();
                    while ((inputStr = br.readLine()) != null) {
                        sb.append(inputStr + "\n");
                    }
                    br.close();
                    conn.disconnect();

                    JSONParser parser2 = new JSONParser();
                    JSONObject json2 = (JSONObject) parser2.parse(sb.toString());
                    JSONObject result2 = (JSONObject) json2.get("result");
                    long count2 = (long) result2.get("count");
                    if (count2 > 0) {
                        JSONArray data = (JSONArray) result2.get("station");

                        for (int i = 0; i < data.size(); i++) {
                            JSONObject obj = (JSONObject) data.get(i);

                            List<Double> list = new ArrayList<>();
                            list.add(Double.parseDouble(obj.get("x").toString()));
                            list.add(Double.parseDouble(obj.get("y").toString()));

                            busStation.add(list);
                        }
                    } else {
                        radius = "반경 1km 내에 버스 정류장 없음";
                        info.put("radius", radius);
                    }
                }

                int size;
                if(busStation.size() > 5){
                    size = 5;
                }else{
                    size = busStation.size();
                }

                loginUserTime = getMidBusStation(size, busStation, loginUserX, loginUserY);
                for(int i=0; i<loginUserTime.size();i++){
                    System.out.println("사용자가 " + i +"번째 버스정류장까지 가는데 걸린 총 시간 : " + loginUserTime.get(i));
                }
                Thread.sleep(2000);
                targetUserTime = getMidBusStation(size, busStation, targetUserX, targetUserY);
                for(int i=0; i<targetUserTime.size();i++){
                    System.out.println("상대방이 " + i +"번째 버스정류장까지 가는데 걸린 총 시간 : " + targetUserTime.get(i));
                }

                Long minTime = Long.MAX_VALUE;
                int index = 0;
                for (int i = 0; i < loginUserTime.size(); i++) {
                    //minTime = (loginUserTime.get(0)+targetUserTime.get(0)) + (loginUserTime.get(0)-targetUserTime.get(0))*2;

                    Long sum = loginUserTime.get(i) + targetUserTime.get(i);
                    Long minus = Math.abs(loginUserTime.get(i) - targetUserTime.get(i));

                    Long tmp = sum + minus * 2;
                    if (minTime > tmp) {
                        index = i;
                        minTime = tmp;
                    }
                }
//                Map<String, Object> top5station = new HashMap<>();
//                for(int i=0; i<loginUserTime.size(); i++){
//                    top5station.put("BusStation"+(i+1), busStation.get(i));
//                    top5station.put("loginUserTime"+(i+1), loginUserTime.get(i));
//                    top5station.put("targetUserTime"+(i+1), targetUserTime.get(i));
//                }
//                info.put("busStationList", top5station);


                for(int i=0; i<size; i++){
                    busStationList.add(busStation.get(i));
                }
                info.put("busStationList", busStationList);
                info.put("loginUserTimeList", loginUserTime);
                info.put("targetUserTimeList", targetUserTime);

                Map<String, Object> resultStation = new HashMap<>();
                resultStation.put("finalBusPositionX", busStation.get(index).get(0));
                resultStation.put("finalBusPositionY", busStation.get(index).get(1));

                resultStation.put("loginUserTotalTime", loginUserTime.get(index));
                resultStation.put("targetUserTotalTime", targetUserTime.get(index));

                info.put("result", resultStation);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return info;
    }

    @Override
    public Map<String, Object> searchMidPositionTest(String loginUserId, String targetUserId) {
        Map<String, Object> info = new HashMap<>();
        ArrayList<Long> targetUserTime = new ArrayList<>();
        ArrayList<Long> loginUserTime = new ArrayList<>();
        ArrayList<List> busStationList = new ArrayList<>();
        Double loginUserX = userService.getPosition(loginUserId).get("positionX");
        Double loginUserY = userService.getPosition(loginUserId).get("positionY");

        Double targetUserX = userService.getPosition(targetUserId).get("positionX");
        Double targetUserY = userService.getPosition(targetUserId).get("positionY");

        Double distanceMeter = distance(loginUserY, loginUserX, targetUserY, targetUserX, "meter");

        if (distanceMeter <= 2000) {
            System.out.println("두 사용자 간의 거리가 2km 이내입니다. ");
            info.put("distance", distanceMeter);

            double midXd = (loginUserX + targetUserX) / 2;
            double midYd = (loginUserY + targetUserY) / 2;

            info.put("midXPosition", midXd);
            info.put("midYPosition", midYd);

        } else {
            targetUserTime.add(17L);
            targetUserTime.add(17L);
            targetUserTime.add(15L);
            targetUserTime.add(15L);
            targetUserTime.add(14L);

            loginUserTime.add(19L);
            loginUserTime.add(20L);
            loginUserTime.add(22L);
            loginUserTime.add(22L);
            loginUserTime.add(22L);

            ArrayList<Double> list1 = new ArrayList<>();
            list1.add(0, 126.83852);
            list1.add(1, 35.19075);
            ArrayList<Double> list2 = new ArrayList<>();
            list2.add(0, 126.83853);
            list2.add(1, 35.19048);
            ArrayList<Double> list3 = new ArrayList<>();
            list3.add(0, 126.83691);
            list3.add(1, 35.18916);
            ArrayList<Double> list4 = new ArrayList<>();
            list4.add(0, 126.837135);
            list4.add(1, 35.189053);
            ArrayList<Double> list5 = new ArrayList<>();
            list5.add(0, 126.83512);
            list5.add(1, 35.190735);
            busStationList.add(list1);
            busStationList.add(list2);
            busStationList.add(list3);
            busStationList.add(list4);
            busStationList.add(list5);

            Map<String, Object> result = new HashMap<>();
            result.put("targetUserTotalTime", 17);
            result.put("finalBusPositionX", 126.83852);
            result.put("loginUserTotalTime", 19);
            result.put("finalBusPositionY", 35.19075);

            info.put("result", result);
            info.put("loginUserTime", loginUserTime);
            info.put("targetUserTime", targetUserTime);
            info.put("busStationList", busStationList);
            info.put("radius", "500");
            info.put("midXPosition", 126.837844095768);
            info.put("midYPosition", 35.19097170925615);
        }
        return info;
    }

    @Override
    public long countArea(String area) {
        long cnt = 0;
        if(area.equals("전체")){
            cnt = dealRepository.count();
        } else{
            cnt = dealRepository.countAllByArea(area);
        }
        return cnt;
    }


    private ArrayList<Long> getMidBusStation(int size, ArrayList<List> station, Double userX, Double userY) {
        ArrayList<Long> userTime = new ArrayList<>();

        for(int i=0; i<size; i++){

            Double midX = (Double) station.get(i).get(0);
            Double midY = (Double) station.get(i).get(1);

            String userSX = String.valueOf(userX);
            String userSY = String.valueOf(userY);

            String EX = String.valueOf(midX);
            String EY = String.valueOf(midY);

//            String apiKey = "sVVsoLKtRaVMwkTbiQfAPb3Dzbu/GeKVmpaAxqvSH0c";
//            String apiKey = "jq+RtpFslnVWy1+I0ZAKnxM2eYmrCAwqtgtu/q9LFBw";
            String apiKey = "tfCyZK7P7irQVoasYK5ZXqZKLWs6NrllNfNG3RSSnRg";
            String surl = "https://api.odsay.com/v1/api/searchPubTransPathT?apiKey="+apiKey+"&SX="+userSX+"&SY="+userSY+"&EX="+EX+"&EY="+EY;

            try {
                URL url = new URL(surl);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String inputStr;
                StringBuilder sb = new StringBuilder();
                while ((inputStr = br.readLine()) != null) {
                    sb.append(inputStr);
                }
                br.close();
                conn.disconnect();

                JSONParser parser = new JSONParser();
                JSONObject json = (JSONObject) parser.parse(sb.toString());

                Object resultObj = json.get("result");
                if(resultObj!=null){
                    JSONObject result = (JSONObject) resultObj;
                    JSONArray paths = (JSONArray) result.get("path");
                    JSONObject path = (JSONObject) paths.get(0);
                    JSONObject info = (JSONObject) path.get("info");

                    Long minTime = Long.parseLong(info.get("totalTime").toString());

                    if(paths.size() > 1){
                        for(int j=1; j<paths.size(); j++){
                            JSONObject path2 = (JSONObject) paths.get(j);
                            JSONObject info2 = (JSONObject) path2.get("info");

                            Long totalTime = Long.parseLong(info2.get("totalTime").toString());
                            if(minTime > totalTime) minTime = totalTime;
                        }
                    }
                    userTime.add(minTime);
                }
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("검색실패..T.T");
            }
        }
        return userTime;
    }

}
