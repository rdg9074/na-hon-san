package com.gwangjubob.livealone.backend.domain.repository;

import com.gwangjubob.livealone.backend.domain.entity.TipEntity;
import com.gwangjubob.livealone.backend.domain.entity.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface TipRepository extends JpaRepository<TipEntity, Integer> {
    Optional<TipEntity> findByIdx(Integer idx);
    Optional<TipEntity> findByTitle(String title);
    List<TipEntity> findByCategory(String category);
    List<TipEntity> findByUser(UserEntity userEntity);

    Optional<TipEntity> findTop1ByOrderByIdxDesc();

    Optional<TipEntity> findTop1ByOrderByViewDesc();
    Optional<TipEntity> findTop1ByOrderByLikeDesc();

    @Query("SELECT t FROM TipEntity t " +
            "WHERE t.idx < :lastIdx " +
            "order by t.idx DESC")
    Slice<TipEntity> findByOrderByIdxDesc(@Param("lastIdx") Integer lastIdx, Pageable pageable);
    @Query("SELECT t FROM TipEntity t " +
            "WHERE ((t.idx < :lastIdx And t.like = :lastLike) OR t.like < :lastLike)" +
            " order by t.like DESC, t.idx DESC")
    Slice<TipEntity> findByOrderByLikeDescAndIdxDesc(@Param("lastLike") Integer lastLike, @Param("lastIdx") Integer lastIdx, Pageable pageable);
    @Query("SELECT t FROM TipEntity t " +
            "WHERE ((t.idx < :lastIdx And t.view = :lastView) OR t.view < :lastView) " +
            "order by t.view DESC, t.idx DESC")
    Slice<TipEntity> findByOrderByViewDescAndIdxDesc(@Param("lastView") Integer lastView, @Param("lastIdx") Integer lastIdx, Pageable pageable);
    @Query("SELECT t FROM TipEntity t " +
            "WHERE t.category = :category AND t.idx < :lastIdx " +
            "order by t.idx DESC")
    Slice<TipEntity> findByCategoryOrderByIdxDesc(String category, @Param("lastIdx") Integer lastIdx, Pageable pageable);
    @Query("SELECT t FROM TipEntity t " +
            "WHERE t.category = :category " +
            "AND ((t.idx < :lastIdx And t.view = :lastView) OR t.view < :lastView) " +
            "order by t.view DESC, t.idx DESC")
    Slice<TipEntity> findByCategoryOrderByViewDescAndIdxDesc(String category, @Param("lastView") Integer lastView, @Param("lastIdx") Integer lastIdx, Pageable pageable);
    @Query("SELECT t FROM TipEntity t " +
            "WHERE t.category = :category " +
            "AND ((t.idx < :lastIdx And t.like = :lastLike) OR t.like < :lastLike)" +
            "order by t.like DESC, t.idx DESC")
    Slice<TipEntity> findByCategoryOrderByLikeDescAndIdxDesc(String category, @Param("lastLike") Integer lastLike, @Param("lastIdx") Integer lastIdx, Pageable pageable);

    @Query("SELECT t FROM TipEntity t " +
            "WHERE t.category = :category " +
            "AND t.title LIKE %:keyword% AND t.idx < :lastIdx " +
            "ORDER BY t.idx DESC")
    Slice<TipEntity> findByCategoryAndTitleContainsOrderByIdxDesc(String category, Integer lastIdx, String keyword, Pageable pageable);
    @Query("SELECT t FROM TipEntity t " +
            "WHERE t.category = :category AND t.title LIKE %:keyword% " +
            "AND ((t.idx < :lastIdx And t.view = :lastView) OR t.view < :lastView)" +
            "ORDER BY t.like DESC, t.idx DESC")
    Slice<TipEntity> findByCategoryAndTitleContainsOrderByViewDescAndIdxDesc(String category,Integer lastView, Integer lastIdx, String keyword, Pageable pageable);
    @Query("SELECT t FROM TipEntity t " +
            "WHERE t.category = :category AND t.title LIKE %:keyword% " +
            "AND ((t.idx < :lastIdx And t.like = :lastLike) OR t.like < :lastLike) " +
            "ORDER BY t.like DESC, t.idx DESC")
    Slice<TipEntity> findByCategoryAndTitleContainsOrderByLikeDescAndIdxDesc(String category,Integer lastLike, Integer lastIdx, String keyword, Pageable pageable);

    int countByUserId(String id);

    List<TipEntity> findTop3ByUserIdOrderByIdxDesc(String id);
}
