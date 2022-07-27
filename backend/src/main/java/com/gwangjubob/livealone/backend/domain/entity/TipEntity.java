package com.gwangjubob.livealone.backend.domain.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.ConnectionBuilder;
import java.time.LocalDate;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Builder
@Entity
@Table(name="tips")
public class TipEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx; // 게시글 번호

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name="user_nickname", referencedColumnName = "nickname"),
            @JoinColumn(name="user_id", referencedColumnName = "id")
    })
    private UserEntity user; // 사용자 아이디(FK), 사용자 닉네임

//    @Column(nullable = false)
    private String category; // 카테고리(꿀팁,꿀템,꿀시피)

//    @Column(length = 30, nullable = false)
    private String title; // 글 제목

//    @Column(length = 2000, nullable = false)
    private String content; // 글 내용

//    @Column(name = "banner_img", nullable = false)
    private String bannerImg; // 배너 이미지

//    @ColumnDefault("0")
    private Integer view; // 조회수

    private LocalDate time; // 글 작성 시간


//    @OneToMany(mappedBy = "tip", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
//    @OrderBy("time desc") // 댓글 정렬
//    private List<TipCommentEntity> tipComments;

}
