package com.gwangjubob.livealone.backend.domain.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.ConnectionBuilder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    @Column(name = "banner_img")
    @Lob
    private byte[] bannerImg;

    private Integer view;
    @Column(name = "likes")
    private Integer like;
    private Integer comment;

    private LocalDateTime time;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @OneToMany(mappedBy = "tip", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @OrderBy("time desc") // 댓글 정렬
    private List<TipCommentEntity> tipComments;

    @OneToMany(mappedBy = "tip", cascade = CascadeType.REMOVE)
    private List<UserLikeTipsEntity> userLikeTips;


    @Override
    public String toString() {
        return "TipEntity{" +
                "idx=" + idx +
                ", category='" + category + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", bannerImg=" + Arrays.toString(bannerImg) +
                ", view=" + view +
                ", like=" + like +
                ", comment=" + comment +
                ", time=" + time +
                '}';
    }
}
