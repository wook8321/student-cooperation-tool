package com.stool.studentcooperationtools.security.credential.resolver;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.common.base.VerifyException;
import com.stool.studentcooperationtools.security.credential.GoogleCredentialProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.io.IOException;

@Component
public class GoogleRequestInitializerArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private final GoogleCredentialProvider googleCredentialProvider;

    @Autowired
    public GoogleRequestInitializerArgumentResolver(GoogleCredentialProvider googleCredentialProvider) {
        this.googleCredentialProvider = googleCredentialProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(HttpRequestInitializer.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpRequestInitializer requestInitializer = googleCredentialProvider.getRequestInitializer();
        if (requestInitializer == null) {
            return new VerifyException("Initializer가 생성되지 않았습니다");
        }
        return requestInitializer;
    }
}