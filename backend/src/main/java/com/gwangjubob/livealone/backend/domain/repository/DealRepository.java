package com.gwangjubob.livealone.backend.domain.repository;

import com.gwangjubob.livealone.backend.domain.entity.DealEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DealRepository extends JpaRepository<DealEntity, Integer> {

    Optional<DealEntity> findByIdx(Integer postIdx);
    List<DealEntity> findByUser(UserEntity userEntity);
    List<DealEntity> findTop6ByCategoryAndStateAndAreaOrderByViewDesc(String category,String state,String area);

    Optional<DealEntity> findTop1ByOrderByIdxDesc();

    Optional<DealEntity> findTop1ByOrderByLikesDesc();

    Optional<DealEntity> findTop1ByOrderByViewDesc();

    List<DealEntity> findByCategory(String category);
    @Query("select d from DealEntity d where d.state = :state and ((d.idx < :idx and d.view = :view  ) or d.view < :view)")
    Slice<DealEntity> findView(String state, Integer idx, Integer view, Pageable pageable); //조회순
    @Query("select d from DealEntity d where d.state = :state and ((d.idx < :idx and d.likes = :likes) or d.likes < :likes)")
    Slice<DealEntity> findlikes(String state, Integer idx, Integer likes, Pageable pageable); //좋아요순
    @Query("select d from DealEntity d where d.state = :state and d.idx < :idx")
    Slice<DealEntity> findIdx(String state , Integer idx, Pageable pageable); //최신순

    @Query("select d from DealEntity d where d.state = :state and d.title like %:keyword% and ((d.idx < :idx and d.view = :view) or d.view < :view)")
    Slice<DealEntity> findTitleView(String state, String keyword, Integer idx, Integer view, Pageable pageable); //검색어, 조회순
    @Query("select d from DealEntity d where d.state = :state and d.title like %:keyword% and ((d.idx < :idx and d.likes = :likes) or d.likes < :likes)")
    Slice<DealEntity> findTitleLikes(String state, String keyword, Integer idx, Integer likes, Pageable pageable); //검색어 좋아요순
    @Query("select d from DealEntity d where d.state = :state and d.title like %:keyword% and d.idx < :idx")
    Slice<DealEntity> findTitleIdx(String state, String keyword, Integer idx, Pageable pageable); //검색어 최신순


    @Query("select d from DealEntity d where d.state = :state and d.category in (:categorys) and ((d.idx < :idx and d.view = :view) or d.view < :view)")
    Slice<DealEntity> findCategoryView(String state, List<String> categorys, Integer idx, Integer view, Pageable pageable); //카테고리 조회순
    @Query("select d from DealEntity d where d.state = :state and d.category in (:categorys) and ((d.idx < :idx and d.likes = :likes) or d.likes < :likes)")
    Slice<DealEntity> findCategoryLikes(String state, List<String> categorys, Integer idx, Integer likes, Pageable pageable); // 카테고리 좋아요순
    @Query("select d from DealEntity d where d.state = :state and d.category in (:categorys) and d.idx < :idx")
    Slice<DealEntity> findCategoryIdx(String state, List<String> categorys, Integer idx, Pageable pageable); //카테고리 최신순
    @Query("select d from DealEntity d where d.state = :state and d.category in (:categorys) and d.title like %:keyword% and((d.idx < :idx and d.view = :view) or d.view < :view)")
    Slice<DealEntity> findCategoryTitleView(String state, List<String> categorys, String keyword, Integer idx, Integer view, Pageable pageable); //카테고리 검색어 조회순
    @Query("select d from DealEntity d where d.state = :state and d.category in (:categorys) and d.title like %:keyword% and((d.idx < :idx and d.likes = :likes) or d.likes < :likes)")
    Slice<DealEntity> findCategoryTitleLikes(String state, List<String> categorys, String keyword, Integer idx, Integer likes, Pageable pageable);//카테고리 검색어 좋아요순
    @Query("select d from DealEntity d where d.state = :state and d.category in (:categorys) and d.title like %:keyword% and d.idx < :idx")
    Slice<DealEntity> findCategoryTitleIdx(String state, List<String> categorys, String keyword, Integer idx, Pageable pageable); //카테고리 검색어 최신순

    int countByUserId(String id);
}
