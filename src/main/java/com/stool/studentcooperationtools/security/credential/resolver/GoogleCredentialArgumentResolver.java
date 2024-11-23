package com.stool.studentcooperationtools.security.credential.resolver;

import com.google.api.client.auth.oauth2.Credential;
import com.google.common.base.VerifyException;
import com.stool.studentcooperationtools.security.credential.GoogleCredentialProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class GoogleCredentialArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private final GoogleCredentialProvider credentialProvider;

    @Autowired
    public GoogleCredentialArgumentResolver(GoogleCredentialProvider credentialProvider) {
        this.credentialProvider = credentialProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Credential.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Credential credential = credentialProvider.getCredential();
        if(credential == null) {
            return new VerifyException("Credential이 없습니다");
        }
        return credential;
    }

}