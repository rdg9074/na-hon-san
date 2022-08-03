package com.gwangjubob.livealone.backend.domain.repository;

import com.gwangjubob.livealone.backend.domain.entity.DealEntity;
import com.gwangjubob.livealone.backend.domain.entity.PopularDealEntity;
import com.gwangjubob.livealone.backend.domain.entity.PopularFollowEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import org.springframework.data.domain.Page;
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
    Slice<DealEntity> findByStateAndIdxLessThanAndViewLessThanEqual(String state, Integer idx, Integer view, Pageable pageable);

    Slice<DealEntity> findByStateAndIdxLessThanAndLikesLessThanEqual(String state, Integer idx, Integer likes, Pageable pageable);
    Slice<DealEntity> findByStateAndIdxLessThan(String state , Integer idx, Pageable pageable);

    Slice<DealEntity> findByStateAndTitleContainsAndIdxLessThanAndViewLessThanEqual(String state, String keyword, Integer idx, Integer view,Pageable pageable);
    Slice<DealEntity> findByStateAndTitleContainsAndIdxLessThanAndLikesLessThanEqual(String state, String keyword, Integer idx, Integer likes,Pageable pageable);
    Slice<DealEntity> findByStateAndTitleContainsAndIdxLessThan(String state, String keyword, Integer idx, Pageable pageable);



    Slice<DealEntity> findByStateAndCategoryInAndIdxLessThanAndViewLessThanEqual(String state, List<String> categorys, Integer idx, Integer view, Pageable pageable);

    Slice<DealEntity> findByStateAndCategoryInAndIdxLessThanAndLikesLessThanEqual(String state, List<String> categorys, Integer idx, Integer likes, Pageable pageable);
    Slice<DealEntity> findByStateAndCategoryInAndIdxLessThan(String state, List<String> categorys, Integer idx, Pageable pageable);

    Slice<DealEntity> findByStateAndCategoryInAndTitleContainsAndIdxLessThanAndViewLessThanEqual(String state, List<String> categorys, String keyword, Integer idx, Integer view, Pageable pageable);

    Slice<DealEntity> findByStateAndCategoryInAndTitleContainsAndIdxLessThanAndLikesLessThanEqual(String state, List<String> categorys, String keyword, Integer idx, Integer likes, Pageable pageable);

    Slice<DealEntity> findByStateAndCategoryInAndTitleContainsAndIdxLessThan(String state, List<String> categorys, String keyword, Integer idx, Pageable pageable);

    int countByUserId(String id);
}
