package com.gwangjubob.livealone.backend.domain.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.ConnectionBuilder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name="tips")
public class TipEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name="user_nickname", referencedColumnName = "nickname"),
            @JoinColumn(name="user_id", referencedColumnName = "id")
    })
    private UserEntity user;
    private String category;
    private String title;
    private String content;
    @Column(name = "banner_img", nullable = false)
    private String bannerImg;
    private Integer view;
    private LocalDateTime time;

    @OneToMany(mappedBy = "tip", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OrderBy("time desc") // 댓글 정렬
    private List<TipCommentEntity> tipComments;

}
