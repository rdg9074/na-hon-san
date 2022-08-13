package com.gwangjubob.livealone.backend.domain.entity;

import ch.qos.logback.classic.spi.LoggingEventVO;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Table(name="auth")
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
public class MailEntity {
    @Id
    String id;
    String type;
    String number;
    LocalDateTime time;

    @Builder
    public MailEntity(String id, String type, String number, LocalDateTime time){ //인증번호 발송 빌더
        this.id = id;
        this.type = type;
        this.number = number;
        this.time = time;
    }
}
