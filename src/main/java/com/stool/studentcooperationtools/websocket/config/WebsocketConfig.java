package com.stool.studentcooperationtools.websocket.config;

import com.stool.studentcooperationtools.websocket.WebsocketErrorHandler;
import com.stool.studentcooperationtools.websocket.converter.SessionMemberMessageConverter;
import com.stool.studentcooperationtools.websocket.interceptor.WebsocketSecurityInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {
    public static final String ROOM_PARTICIPATION_URL_FORMAT = "/sub/rooms/%d/online";
    public static final String TOPIC_DECISION_URL_FORMAT = "/sub/rooms/%d/topics";
    public static final String PRESENTATION_MANAGE_URL_FORMAT = "/sub/rooms/%d/presentation";
    public static final String CHAT_ROOM_URL_FORMAT = "/sub/rooms/%d/chat";
    public static final String PART_RESEARCH_URL_FORMAT = "/sub/rooms/%d/part";
    public static final String SCRIPT_MANAGE_URL_FORMAT = "/sub/rooms/%d/scripts";

    private final WebsocketErrorHandler websocketErrorHandler;
    private final WebsocketSecurityInterceptor websocketSecurityInterceptor;
    private final SessionMemberMessageConverter sessionMemberMessageConverter;

    @Override
    public void registerStompEndpoints(final StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp")
                .setAllowedOriginPatterns("*")
                .withSockJS();
        registry.setErrorHandler(websocketErrorHandler);
    }

    @Override
    public boolean configureMessageConverters(final List<MessageConverter> messageConverters) {
        //spring mvc가 작동하지 않아 SessionMember를 바인딩하지 못한다.
        //웹소켓은 MessageConverter가 파라미터를 바인딩하기 때문에 MessageConverter로 SessionMember를 바인딩한다.
        messageConverters.add(sessionMemberMessageConverter);
        //기본 converter를 사용하지 않으면 false
        return false;
    }

    @Override
    public void configureWebSocketTransport(final WebSocketTransportRegistration registry) {
        //stomp 메세지의 length의 최대치를 160*64*1024까지 늘린다.
        registry.setMessageSizeLimit(160 * 64 * 1024);
    }

    @Override
    public void configureMessageBroker(final MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub");
        registry.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void configureClientInboundChannel(final ChannelRegistration registration) {
        registration.interceptors(websocketSecurityInterceptor);
    }
}
