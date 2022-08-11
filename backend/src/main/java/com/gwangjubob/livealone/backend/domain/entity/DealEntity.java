package com.gwangjubob.livealone.backend.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "deals")
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
public class DealEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "user_nickname", referencedColumnName = "nickname"),
            @JoinColumn(name = "user_id", referencedColumnName = "id")
    })
    private UserEntity user;

    LocalDateTime time;
    @Column(name = "update_time")
    LocalDateTime updateTime;
    private String title;
    private String content;
    private String category;
    @Lob
    @Column(name = "banner_img")
    private byte[] bannerImg;
    private String state;
    private String area;
    private Integer view;
    private Integer likes;
    private Integer comment;
    @OneToMany(mappedBy = "deal")
    List<DealCommentEntity> comments = new ArrayList<>();

    @Override
    public String toString() {
        return "DealEntity{" +
                "idx=" + idx +
                ", time=" + time +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", category='" + category + '\'' +
                ", bannerImg='" + bannerImg + '\'' +
                ", state='" + state + '\'' +
                ", area='" + area + '\'' +
                ", view=" + view +
                '}';
    }
}
