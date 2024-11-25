package com.stool.studentcooperationtools.websocket.interceptor;

import com.stool.studentcooperationtools.domain.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
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
    private final RoomRepository roomRepository;
    @Override
    public Message<?> preSend(final Message<?> message, final MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) accessor.getHeader("simpUser");
        validAuthentication(accessor, authentication);
        validMemberInRoom(accessor, authentication);
        return message;
    }

    private void validAuthentication(final StompHeaderAccessor accessor, final OAuth2AuthenticationToken authentication) {
        //WebSocket의 EndPoint에 연결할 인증을 했는지 확인하는 함수
        if (!SimpMessageType.CONNECT.equals(accessor.getMessageType())) {
            return;
        }
        if(authentication == null){
            // 웹소켓 연결 전에 인증하지 않은 경우 authentication는 조회되지 않음
            String jsessionid = accessor.getFirstNativeHeader(SESSION_NAME);
            Object session = jdbcIndexedSessionRepository.findById(jsessionid);
            if(session == null){
                throw new SessionAuthenticationException("Authentication Failed");
            }
        }
    }

    private void validMemberInRoom(final StompHeaderAccessor accessor, final OAuth2AuthenticationToken authentication) {
        // 방에 들어가서 구독할 권한이 없는지 확인하는
        if(SimpMessageType.SUBSCRIBE.equals(accessor.getMessageType())){
            //방에 들어갈 권한이 없는 경우
            Long roomId =  getRoomIdBy(accessor.getDestination());
            String email = authentication.getPrincipal().getAttribute("email");
            if(!roomRepository.existMemberInRoom(email, roomId)){
                throw new AccessDeniedException("Authentication Failed");
            }
        }
    }

    private Long getRoomIdBy(final String destination){
        //방의 id를 추출하는 메소드
        return Long.valueOf(destination.split("/")[3]);
    }
}
