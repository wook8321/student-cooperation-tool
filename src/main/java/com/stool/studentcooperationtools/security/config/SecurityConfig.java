package com.stool.studentcooperationtools.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests( authorize -> { // 정적 파일들은 접근 허락
                    authorize.requestMatchers("/*.html","/*.png","/*.ico","/static/**")
                            .permitAll();
                })
                .authorizeHttpRequests( authorize -> {
                    authorize.requestMatchers("/","/api/test")
                            .permitAll();
                })
                .build();
    }

}