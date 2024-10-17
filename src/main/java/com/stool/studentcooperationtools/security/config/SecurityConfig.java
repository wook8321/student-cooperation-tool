package com.stool.studentcooperationtools.security.config;

<<<<<<< HEAD
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
=======
import com.stool.studentcooperationtools.security.oauth2.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
>>>>>>> daa7c781c6c5fad1d18527e8a96ea8b1d0f2a862
                .authorizeHttpRequests( authorize -> { // 정적 파일들은 접근 허락
                    authorize.requestMatchers("/*.html","/*.png","/*.ico","/static/**")
                            .permitAll();
                })
<<<<<<< HEAD
                .authorizeHttpRequests( authorize -> {
                    authorize.requestMatchers("/","/api/test")
                            .permitAll();
                })
=======
                .authorizeHttpRequests( authorize -> { //인증 없이 접근가능한 url
                    authorize.requestMatchers("/","/api/test","/login","/oauth/**","/logout")
                            .permitAll();
                })
                .sessionManagement(session->session //세션 고정 공격 보호
                        .sessionFixation().changeSessionId()
                )
                .formLogin(AbstractHttpConfigurer::disable) // form 로그인 불가능 설정
                .authorizeHttpRequests( authorize -> { // 나머지 모든 url은 인증이 필요
                    authorize.anyRequest().authenticated();
                })
                .oauth2Login((oauth)->oauth
                        .userInfoEndpoint((endPoint)->endPoint
                                .userService(customOAuth2UserService)
                        )
                        .defaultSuccessUrl("/")
                )
                .logout(logout->logout //로그아웃 설정
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
>>>>>>> daa7c781c6c5fad1d18527e8a96ea8b1d0f2a862
                .build();
    }

}