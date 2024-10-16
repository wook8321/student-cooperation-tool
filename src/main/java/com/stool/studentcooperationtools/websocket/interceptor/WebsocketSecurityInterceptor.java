package com.stool.studentcooperationtools.websocket.interceptor;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebsocketSecurityInterceptor implements ChannelInterceptor {

    private final HttpSession httpSession;

    @Override
    public Message<?> preSend(final Message<?> message, final MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (SimpMessageType.CONNECT.equals(accessor.getMessageType()) ||
                SimpMessageType.MESSAGE.equals(accessor.getMessageType()) ||
                SimpMessageType.SUBSCRIBE.equals(accessor.getMessageType())) {
            Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();
            if(authentication == null){
                // 웹소켓 연결 전에 인증하지 않은 경우 authentication는 조회되지 않음
                throw new SessionAuthenticationException("Authentication Failed");
            }

            accessor.setUser(authentication);
        }

        return ChannelInterceptor.super.preSend(message, channel);
    }
}
