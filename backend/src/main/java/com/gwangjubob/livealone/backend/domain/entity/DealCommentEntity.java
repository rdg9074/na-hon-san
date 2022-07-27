package com.gwangjubob.livealone.backend.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name="deal_comments")
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
public class DealCommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "user_nickname", referencedColumnName = "nickname"),
            @JoinColumn(name = "user_id", referencedColumnName = "id")
    })
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "post_idx")
    private DealEntity deal;

    LocalDateTime time;
    @Column(name = "update_time")
    LocalDateTime updateTime;
    private String content;
    @Column(name = "banner_img")
    @Lob
    private byte[] bannerImg;
    @Column(name = "up_idx")
    Integer upIdx;
    @Column(name = "position_x")
    Integer positionX;
    @Column(name = "position_y")
    Integer positionY;

    @Override
    public String toString() {
        return "DealCommentEntity{" +
                "idx=" + idx +
                ", time=" + time +
                ", content='" + content + '\'' +
                ", bannerImg='" + bannerImg + '\'' +
                ", upIdx=" + upIdx +
                ", positionX=" + positionX +
                ", positionY=" + positionY +
                '}';
    }
}
