package com.stool.studentcooperationtools.web.config;

import com.stool.studentcooperationtools.web.resolver.SessionMemberArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final SessionMemberArgumentResolver sessionMemberArgumentResolver;

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(sessionMemberArgumentResolver);
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }
}
