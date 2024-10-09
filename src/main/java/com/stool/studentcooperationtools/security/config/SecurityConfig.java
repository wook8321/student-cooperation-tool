package com.stool.studentcooperationtools.security.config;

import com.stool.studentcooperationtools.security.oauth2.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

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
                .oauth2Login((oauth)->oauth
                        .userInfoEndpoint((endPoint)->endPoint
                                .userService(customOAuth2UserService)
                        )
                        .defaultSuccessUrl("/")
                )
                .build();
    }

}