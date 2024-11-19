package com.stool.studentcooperationtools.websocket.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.session.jdbc.JdbcIndexedSessionRepository;
import org.springframework.stereotype.Component;

import static com.stool.studentcooperationtools.security.config.SecurityConfig.SESSION_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebsocketSecurityInterceptor implements ChannelInterceptor {

    private final JdbcIndexedSessionRepository jdbcIndexedSessionRepository;
    @Override
    public Message<?> preSend(final Message<?> message, final MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (SimpMessageType.CONNECT.equals(accessor.getMessageType())) {
            OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) accessor.getHeader("simpUser");
            if(authentication == null){
                // 웹소켓 연결 전에 인증하지 않은 경우 authentication는 조회되지 않음
                String jsessionid = accessor.getFirstNativeHeader(SESSION_NAME);
                Object session = jdbcIndexedSessionRepository.findById(jsessionid);
                if(session == null){
                    throw new SessionAuthenticationException("Authentication Failed");
                }
            }
        }

        return message;
    }
}
