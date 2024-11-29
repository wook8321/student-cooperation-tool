package com.stool.studentcooperationtools.web.resolver;


import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.naming.AuthenticationException;

import static com.stool.studentcooperationtools.security.oauth2.CustomOAuth2UserService.SESSION_MEMBER_NAME;

@Component
@RequiredArgsConstructor
public class SessionMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final HttpSession httpSession;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return SessionMember.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer, final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) throws Exception {
        SessionMember memberInfo = (SessionMember) httpSession.getAttribute(SESSION_MEMBER_NAME);
        if(memberInfo == null){
            return new AuthenticationException("접근할 권한이 없습니다.");
        }
        return memberInfo;
    }
}
