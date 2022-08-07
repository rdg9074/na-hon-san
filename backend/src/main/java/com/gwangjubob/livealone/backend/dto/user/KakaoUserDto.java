package com.gwangjubob.livealone.backend.dto.user;


import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoUserDto {
    private Long id;
    private Properties properties;
    private KakaoAccount kakao_account;
    private String test;

    @Getter
    @ToString
    public static class KakaoAccount{
        private String email;
    }
    @Getter
    @ToString
    public static class Properties{
        private String nickname;
        private String thumbnail_image;
    }

}
