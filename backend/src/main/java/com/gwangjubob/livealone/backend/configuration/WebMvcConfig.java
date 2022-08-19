package com.gwangjubob.livealone.backend.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Properties;

@Configuration
@PropertySource("classpath:application.yml")
@EnableAspectJAutoProxy
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://localhost:3001","http://localhost:80","http://i7c208.p.ssafy.io", "http://i7c208.p.ssafy.io:3000", "https://i7c208.p.ssafy.io", "https://i7c208.p.ssafy.io:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH","OPTIONS")
                .maxAge(6000)
                .allowCredentials(true);
    }
}