package com.gwangjubob.livealone.backend.service;

import com.gwangjubob.livealone.backend.domain.entity.*;
import com.gwangjubob.livealone.backend.domain.repository.*;
import com.gwangjubob.livealone.backend.dto.Deal.DealCommentDto;
import com.gwangjubob.livealone.backend.dto.Deal.DealDto;
import com.gwangjubob.livealone.backend.mapper.DealCommentMapper;
import com.gwangjubob.livealone.backend.mapper.DealMapper;
import com.gwangjubob.livealone.backend.mapper.DealViewMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import javax.net.ssl.HttpsURLConnection;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

@SpringBootTest
@Transactional
public class DealServiceTest {
    private DealRepository dealRepository;
    private DealMapper dealMapper;
    private DealCommentMapper dealCommentMapper;
    private UserRepository userRepository;

    private DealCommentRepository dealCommentRepository;
    private UserLikeDealsRepository userLikeDealsRepository;
    private NoticeRepository noticeRepository;
    private DealViewMapper dealViewMapper;

    private static final String okay = "SUCCESS";
    private static final String fail = "FAIL";

    @Autowired
    DealServiceTest(DealRepository dealRepository, DealMapper dealMapper, UserRepository userRepository, DealCommentMapper dealCommentMapper,
                    DealCommentRepository dealCommentRepository, UserLikeDealsRepository userLikeDealsRepository, NoticeRepository noticeRepository, DealViewMapper dealViewMapper){
        this.dealRepository = dealRepository;
        this.dealMapper = dealMapper;
        this.userRepository = userRepository;
        this.dealCommentMapper = dealCommentMapper;
        this.dealCommentRepository = dealCommentRepository;
        this.userLikeDealsRepository = userLikeDealsRepository;
        this.noticeRepository = noticeRepository;
        this.dealViewMapper = dealViewMapper;
    }

    @Test
    public void 꿀딜_게시글_작성(){
        Map<String, Object> resultMap = new HashMap<>();
        String userNickname = "비밀번호는 test 입니다.";
        String title = "제목이다.";
        String content = "내용입니다.";
        String category = "주방용품";
        byte[] bannerImg = null;
        String state = "거래중";
        String area = "광주";
        Integer view = 3;
        Optional<UserEntity> optionalUser = userRepository.findByNickname(userNickname);
        if (optionalUser.isPresent()){
            UserEntity user = optionalUser.get();
            DealDto input = DealDto.builder()
                    .userNickname(userNickname)
                    .title(title)
                    .content(content)
                    .category(category)
                    .bannerImg(bannerImg)
                    .state(state)
                    .area(area)
                    .view(view).build();
            DealEntity deal = dealMapper.toEntity(input);
            deal.setUser(user);
            DealEntity dealEntity = dealRepository.save(deal);
            DealDto dealDto = dealMapper.toDto(dealEntity);
            dealDto.setUserNickname(deal.getUser().getNickname());
            resultMap.put("data", dealDto);
            resultMap.put("message", okay);
        } else{
            resultMap.put("message", fail);
        }
        System.out.println(resultMap);
    }

    @Test
    public void 꿀딜_게시글_상세조회(){
        Map<String, Object> resultMap = new HashMap<>();
        Integer idx = 94;
        Optional<DealEntity> optionalDeal = dealRepository.findById(idx);
        if(optionalDeal.isPresent()){
            DealEntity dealEntity = optionalDeal.get();
            List<DealCommentEntity> comments = dealCommentRepository.findByDealOrderByComment(dealEntity);
            List<DealCommentDto> commentDto = dealCommentMapper.toDtoList(comments);
            DealDto data = dealMapper.toDto(dealEntity);
            data.setUserNickname(dealEntity.getUser().getNickname());
            data.setUserId(dealEntity.getUser().getId());
            resultMap.put("data", data);

            resultMap.put("message", okay);
        } else{
            resultMap.put("message", fail);
        }
        System.out.println(resultMap);
    }

    @Test
    public void 꿀딜_게시글_수정(){
        Map<String, Object> resultMap = new HashMap<>();
        Integer idx = 10;
        String title = "update";
        String content = "update";
        String category = "update";
        byte[] bannerImg = null;
        String state = "거래중";
        DealDto dealDto = new DealDto()
                .builder()
                .title(title)
                .content(content)
                .category(category)
                .bannerImg(bannerImg)
                .state(state)
                .build();
        Optional<DealEntity> optionalDeal = dealRepository.findById(idx);
        if(optionalDeal.isPresent()){
            DealEntity dealEntity = optionalDeal.get();
            dealMapper.updateFromDto(dealDto, dealEntity);
            DealEntity deal = dealRepository.save(dealEntity);
            DealDto data = dealMapper.toDto(deal);
            resultMap.put("data", data);
            resultMap.put("message", okay);
        }else{
            resultMap.put("message", fail);
        }
        System.out.println(resultMap);
    }

    @Test
    public void 꿀딜_게시글_삭제(){
        Map<String, Object> resultMap = new HashMap<>();
        Integer idx = 13;
        Optional<DealEntity> optionalDeal = dealRepository.findById(idx);
        if(optionalDeal.isPresent()) {
            DealEntity dealEntity = optionalDeal.get();
            dealRepository.delete(dealEntity);
            resultMap.put("message", okay);

            // 해당 게시글 관련 모든 알림 삭제
            List<NoticeEntity> noticeEntityList = noticeRepository.findAllByPostIdxAndPostType(idx, "deal");
            if(!noticeEntityList.isEmpty()){
                noticeRepository.deleteAllInBatch(noticeEntityList);
            }
        } else{
            resultMap.put("message", fail);
        }
        System.out.println(resultMap);
    }

    @Test
    public void 꿀딜_댓글_등록(){
        Map<String, Object> resultMap = new HashMap<>();
        Integer postIdx = 69;
        Integer upidx = 30;
        String userNickname = "ssafy";
        String content = "e대대ㅐㅐ대ㅐ댇댓글 내용입니다.";
        byte[] bannerImg = null;
        Optional<UserEntity> optionalUser = userRepository.findByNickname(userNickname);
        Optional<DealEntity> optionalDeal = dealRepository.findById(postIdx);
        if (optionalUser.isPresent() && optionalDeal.isPresent()){
            UserEntity user = optionalUser.get();
            DealEntity deal = optionalDeal.get();
            DealCommentDto input = DealCommentDto.builder()
                    .upIdx(upidx)
                    .content(content)
                    .bannerImg(bannerImg)
                    .build();
            DealCommentEntity inputEntity = dealCommentMapper.toEntity(input);
            inputEntity.setDeal(deal);
            inputEntity.setUser(user);
            DealCommentEntity dealCommentEntity  = dealCommentRepository.save(inputEntity);
            // deal 게시물 댓글 수 증가
            deal.setComment(deal.getComment() + 1);
            dealRepository.save(deal);

            // 댓글 등록시 글작성자가 아니면 알림 가게
            if(upidx == 0 & !deal.getUser().getNickname().equals(userNickname)){
                NoticeEntity notice = NoticeEntity.builder()
                        .noticeType("comment")
                        .user(deal.getUser())
                        .fromUserId(user.getId())
                        .postType("deal")
                        .commentIdx(inputEntity.getIdx())
                        .postIdx(deal.getIdx())
                        .build();

                noticeRepository.save(notice);
            }
            // 대댓글 등록 알림
            if(upidx != 0){
                DealCommentEntity dealComment = dealCommentRepository.findByIdx(upidx).get();
                if(!dealComment.getUser().getNickname().equals(userNickname)){
                    NoticeEntity notice = NoticeEntity.builder()
                            .noticeType("reply")
                            .user(dealComment.getUser())
                            .fromUserId(user.getId())
                            .postType("deal")
                            .commentIdx(inputEntity.getIdx())
                            .commentUpIdx(inputEntity.getUpIdx())
                            .postIdx(deal.getIdx())
                            .build();

                    noticeRepository.save(notice);
                }
            }

            DealCommentDto data = dealCommentMapper.toDto(dealCommentEntity);
            data.setPostIdx(deal.getIdx());
            data.setUserNickname(user.getNickname());
            resultMap.put("data", data);
            resultMap.put("message", okay);
        } else{
            resultMap.put("message", fail);
        }
        System.out.println(resultMap);
    }

    @Test
    public void 꿀딜_댓글_수정(){
        Map<String, Object> resultMap = new HashMap<>();
        Integer idx = 11;
        String content = "update";
        byte[] bannerImg = null;
        DealCommentDto dealCommentDto = DealCommentDto
                .builder()
                .content(content)
                .bannerImg(bannerImg)
                .build();
        Optional<DealCommentEntity> optionalDealComment = dealCommentRepository.findById(idx);
        if(optionalDealComment.isPresent()){
            DealCommentEntity dealCommentEntity = optionalDealComment.get();
            dealCommentMapper.updateFromDto(dealCommentDto, dealCommentEntity);
            DealCommentEntity updateDeal = dealCommentRepository.save(dealCommentEntity);
            DealCommentDto data = dealCommentMapper.toDto(updateDeal);
            resultMap.put("data", data);
            resultMap.put("message", okay);
        }else{
            resultMap.put("message", fail);
        }
        System.out.println(resultMap);
    }

    @Test
    public void 꿀딜_댓글_삭제(){
        Map<String, Object> resultMap = new HashMap<>();
        Integer postIdx = 121;
        Integer idx = 108;

        String userId = "aa981204@naver.com";
        Optional<DealCommentEntity> optionalDealComment = dealCommentRepository.findById(idx);
        DealEntity deal = dealRepository.findByIdx(postIdx).get();

        if(optionalDealComment.isPresent()){
            DealCommentEntity dealCommentEntity = optionalDealComment.get();

            // 대댓글이라면
            if(dealCommentEntity.getUpIdx() != 0 ){
                deal.setComment(deal.getComment() - 1);
                dealRepository.save(deal);

                dealCommentRepository.delete(dealCommentEntity);

                // 알림이 있다면 알림도 삭제
                Optional<NoticeEntity> noticeEntity = noticeRepository.findByNoticeTypeAndFromUserIdAndPostTypeAndPostIdxAndCommentIdx("reply", userId , "deal", postIdx, dealCommentEntity.getIdx());
                if(noticeEntity.isPresent()){
                    noticeRepository.delete(noticeEntity.get());
                }
            }else{ // 댓글이라면 관련된 대댓글들 모두 삭제
                List<DealCommentEntity> replyCommentList = dealCommentRepository.findByUpIdx(idx);
                int size = replyCommentList.size();

                if(!replyCommentList.isEmpty()){
                    deal.setComment(deal.getComment() - size);
                    dealRepository.save(deal);

                    dealCommentRepository.deleteAllInBatch(replyCommentList);

                    // 대댓글관련 알림까지 삭제
                    List<NoticeEntity> noticeEntityList = noticeRepository.findAllByNoticeTypeAndPostTypeAndPostIdxAndCommentUpIdx("reply", "deal", postIdx, dealCommentEntity.getIdx());
                    if(!noticeEntityList.isEmpty()){
                        noticeRepository.deleteAllInBatch(noticeEntityList);
                    }
                }
                deal.setComment(deal.getComment() - 1);
                dealRepository.save(deal);

                dealCommentRepository.delete(dealCommentEntity);
                // 댓글 알림 삭제
                Optional<NoticeEntity> noticeEntity = noticeRepository.findByNoticeTypeAndPostTypeAndPostIdxAndCommentIdx("comment","deal",postIdx, dealCommentEntity.getIdx());
                if(noticeEntity.isPresent()){
                    noticeRepository.delete(noticeEntity.get());
                }
            }

            dealCommentRepository.delete(dealCommentEntity);
            resultMap.put("message", okay);
        } else{
            resultMap.put("message", fail);
        }
        System.out.println(resultMap);
    }

    @Test
    public void 꿀딜_게시글_좋아요(){
        Map<String, Object> resultMap = new HashMap<>();
        Integer postIdx = 49;
        String userId = "ssafy";
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        Optional<DealEntity> optionalDeal = dealRepository.findById(postIdx);
        if(optionalUser.isPresent() && optionalDeal.isPresent()){
            UserEntity user = optionalUser.get();
            DealEntity deal = optionalDeal.get();
            Optional<UserLikeDealsEntity> optionalUserLikeDeals = userLikeDealsRepository.findByDealAndUser(deal, user);
            if(optionalUserLikeDeals.isPresent()){
                UserLikeDealsEntity userLikeDeals = optionalUserLikeDeals.get();
                userLikeDealsRepository.delete(userLikeDeals);
                deal.setLikes(deal.getLikes() - 1);
                dealRepository.save(deal);
                resultMap.put("message", okay);
                resultMap.put("data", "좋아요 취소");

                // 좋아요 취소 누르면 알림까지 삭제
                Optional<NoticeEntity> noticeEntity = noticeRepository.findByNoticeTypeAndFromUserIdAndPostTypeAndPostIdx("like",userId,"deal",postIdx);
                if(noticeEntity.isPresent()){
                    noticeRepository.delete(noticeEntity.get());
                }
            } else{
                UserLikeDealsEntity userLikeDeals = UserLikeDealsEntity
                        .builder()
                        .deal(deal)
                        .user(user)
                        .build();
               userLikeDealsRepository.save(userLikeDeals);
               deal.setLikes(deal.getLikes() + 1);
               dealRepository.save(deal);
               resultMap.put("message", "좋아요");

               // 알림 등록
                NoticeEntity noticeEntity = NoticeEntity.builder()
                        .noticeType("like")
                        .user(deal.getUser())
                        .fromUserId(userId)
                        .postType("deal")
                        .postIdx(deal.getIdx())
                        .time(userLikeDeals.getTime())
                        .build();

                noticeRepository.save(noticeEntity);
            }
        } else{
            resultMap.put("message", fail);
        }
        System.out.println(resultMap);
    }

    @Test
    public void 꿀딜_게시글_조회(){
        Map<String, Object> resultMap = new HashMap<>();
        String keyword = null;
        String state = "거래 대기"; //거래중, 거래 대기, 거래 완료
        Integer pageSize = 6;
        List<String> categorys = new ArrayList<>(); //"전체", "의류","식품","주방용품","생활용품","홈인테리어","가전디지털","취미용품","기타"
        categorys.add("전체");
        String type = "좋아요순"; //조회순, 좋아요순, 최신순
        Slice<DealEntity> deals = null;
        Pageable pageable = null;
        String area = "광주";
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
        Integer lastLikes = null;
        Integer lastView = null;
        Integer lastIdx = null;
        if(lastLikes == null) {
            lastLikes = dealRepository.findTop1ByOrderByLikesDesc().get().getLikes() + 1;
        }
        if(lastView == null){
            lastView = dealRepository.findTop1ByOrderByViewDesc().get().getView() + 1;
        }
        if(lastIdx == null){
            lastIdx = dealRepository.findTop1ByOrderByIdxDesc().get().getIdx() + 1;
        }
        if(area != null){
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
                        deals = dealRepository.findCategoryLikesArea(state, categorys, lastIdx, lastLikes, area, pageable);
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
                        deals = dealRepository.findlikes(state,lastIdx, lastLikes, pageable);
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
        long cnt = 0;
        if(area == null){
            cnt = dealRepository.count();
        } else{
            cnt = dealRepository.countAllByArea(area);
        }

        if(deals != null){
            List<DealEntity> dealsList = deals.getContent();
            List<DealDto> result = dealMapper.toDtoList(dealsList);
            resultMap.put("message", okay);
            resultMap.put("data", result);
            resultMap.put("count", cnt);
        } else{
            resultMap.put("message", fail);
        }
        System.out.println(resultMap);
    }

    @Test
    public void 조회수_증가(){
        Map<String, Object> resultMap = new HashMap<>();
        Integer idx = 51;
        Optional<DealEntity> optionalDeal = dealRepository.findById(idx);
        if(optionalDeal.isPresent()){
            DealEntity deal = optionalDeal.get();
            deal.setView(deal.getView() + 1);
            dealRepository.save(deal);
            resultMap.put("message", okay);
        } else{
            resultMap.put("message", fail);
        }
        System.out.println(resultMap);
    }

    @Test
    public void 두_사용자_거리_조회() {
        Double loginUserX = 126.701172788021;
        Double loginUserY = 35.1712946488375;

        Double targetUserX = 126.876826236529;
        Double targetUserY = 35.1849575358041;

        Double distanceKilometer = distance(loginUserY,loginUserX,targetUserY,targetUserX, "kilometer");
        Double distanceMeter = distance(loginUserY,loginUserX,targetUserY,targetUserX, "meter");

        if(distanceMeter <= 2000){
            System.out.println("두 사용자 간의 거리가 가깝습니다. " +distanceMeter);
        }
        System.out.println(distanceKilometer);
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

        ArrayList<List> station = new ArrayList<>();// i=0,1,2,3,4,5
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
//        List<Double> list6 = new ArrayList<>();
//        list6.add(126.860794);
//        list6.add(35.179);
        station.add(list1);
        station.add(list2);
        station.add(list3);
        station.add(list4);
        station.add(list5);
//        station.add(list6);

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
                URL url = new URL(surl2);

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
//                System.out.println(json);
                JSONObject result = (JSONObject) json.get("result");
//                System.out.println(result);
                JSONArray paths = (JSONArray) result.get("path"); // 여기서 최단 시간 찾아야함
//                System.out.println(paths.get(0));
//                System.out.println(paths.size());
//                JSONObject path = (JSONObject) paths.get(0);
//                JSONObject info = (JSONObject) path.get("info");
//                JSONObject totalTime = (JSONObject) info.get("totalTime");
//                Long minTime = Long.parseLong(info.get("totalTime").toString());
//                System.out.println(i+"번째 정류장 총 시간 : " + minTime);
//                System.out.println(paths.size());

                Long minTime = Long.MAX_VALUE;

                for(int j=0; j<paths.size(); j++){
                    JSONObject path = (JSONObject) paths.get(j);
                    JSONObject info = (JSONObject) path.get("info");

                    Long totalTime = Long.parseLong(info.get("totalTime").toString());
                    if(minTime > totalTime) minTime = totalTime;
                }
                loginUserTime.add(minTime); // 6개. 정렬 -> 3개 뽑으면 되는거 아닌가여?
                System.out.println("최단시간 : " + minTime);

                loginUserTime.add(minTime);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Test
    public void 최적의_거리_구하기() {
        ArrayList<List> station = new ArrayList<>();// i=0,1,2,3,4,5
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

        List<Long> loginUserTime = new ArrayList<>();
        List<Long> targetUserTime = new ArrayList<>();

        loginUserTime.add(18L);
        loginUserTime.add(17L);
        loginUserTime.add(20L);
        loginUserTime.add(19L);
        loginUserTime.add(29L);
        loginUserTime.add(29L);

        targetUserTime.add(24L);
        targetUserTime.add(24L);
        targetUserTime.add(21L);
        targetUserTime.add(22L);
        targetUserTime.add(29L);
        targetUserTime.add(29L);

        Long MIN = Long.MAX_VALUE;
        // 두 시간의 합, 차 --> list? 반복돌리면서 최소값을 ?
        int index = 0;
        for(int i=0; i< loginUserTime.size(); i++){
            Long sum = loginUserTime.get(i) + targetUserTime.get(i);
            Long minus = Math.abs(loginUserTime.get(i) - targetUserTime.get(i));

            Long tmp = sum + minus * 2;
            if(MIN > tmp) {
                index = i; // 정류장 위치 인덱스
                MIN = tmp;
            }
        }

        // 결과 - 최적의 정류장 위치좌표
        Double x = (Double) station.get(index).get(0);
        Double y = (Double) station.get(index).get(1);
        // 각 사용자가 걸린 시간
        Long userTime = loginUserTime.get(index);
        Long targetTime = targetUserTime.get(index);

        System.out.println("x좌표 : " + x);
        System.out.println("y좌표 : " + y);
        System.out.println("loginUser 걸린 시간 : " + userTime);
        System.out.println("targetUser 걸린 시간 : " + targetTime);
    }
}
