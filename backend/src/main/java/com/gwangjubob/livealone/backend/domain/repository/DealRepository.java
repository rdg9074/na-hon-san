package com.gwangjubob.livealone.backend.domain.repository;

import com.gwangjubob.livealone.backend.domain.entity.DealEntity;
import com.gwangjubob.livealone.backend.domain.entity.PopularDealEntity;
import com.gwangjubob.livealone.backend.domain.entity.PopularFollowEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DealRepository extends JpaRepository<DealEntity, Integer> {

    Optional<DealEntity> findByIdx(Integer postIdx);
    List<DealEntity> findByUser(UserEntity userEntity);
    List<DealEntity> findTop6ByCategoryAndStateAndAreaOrderByViewDesc(String category,String state,String area);

    List<DealEntity> findByCategory(String category);
    List<DealEntity> findByStateOrderByViewDesc(String state, Pageable pageable);

    List<DealEntity> findByStateOrderByLikesDesc(String state, Pageable pageable);
    List<DealEntity> findByStateOrderByIdxDesc(String state , Pageable pageable);

    List<DealEntity> findByStateAndTitleContainsOrderByViewDesc(String state, String keyword, Pageable pageable);
    List<DealEntity> findByStateAndTitleContainsOrderByLikesDesc(String state, String keyword, Pageable pageable);
    List<DealEntity> findByStateAndTitleContainsOrderByIdxDesc(String state, String keyword, Pageable pageable);

    List<DealEntity> findByStateAndCategoryInAndTitleContainsOrderByLikesDesc(String state, List<String> categorys, String keyword, Pageable pageable);
    List<DealEntity> findByStateAndCategoryInOrderByLikesDesc(String state, List<String> categorys, Pageable pageable);
    List<DealEntity> findByStateAndCategoryInAndTitleContainsOrderByIdxDesc(String state, List<String> categorys, String keyword, Pageable pageable);
    List<DealEntity> findByStateAndCategoryInOrderByIdxDesc(String state, List<String> categorys, Pageable pageable);
    List<DealEntity> findByStateAndCategoryInAndTitleContainsOrderByViewDesc(String state, List<String> categorys, String keyword, Pageable pageable);
    List<DealEntity> findByStateAndCategoryInOrderByViewDesc(String state, List<String> categorys, Pageable pageable);
}
