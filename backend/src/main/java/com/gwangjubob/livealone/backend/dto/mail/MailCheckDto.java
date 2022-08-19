package com.gwangjubob.livealone.backend.dto.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MailCheckDto {
    private String id;
    private String type;
    private String number;
}


