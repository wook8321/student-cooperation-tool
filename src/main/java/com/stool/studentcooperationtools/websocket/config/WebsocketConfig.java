package com.stool.studentcooperationtools.websocket.config;

import com.stool.studentcooperationtools.websocket.WebsocketErrorHandler;
import com.stool.studentcooperationtools.websocket.interceptor.WebsocketSecurityInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {
    public static final String TOPIC_DECISION_URL_FORMAT = "/sub/rooms/%d/topics";
    public static final String PRESENTATION_MANAGE_URL_FORMAT = "/sub/rooms/%d/presentation";
    public static final String CHAT_ROOM_URL_FORMAT = "/sub/rooms/%d/chat";
    
    private final WebsocketErrorHandler websocketErrorHandler;
    private final WebsocketSecurityInterceptor websocketSecurityInterceptor;

    @Override
    public void registerStompEndpoints(final StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp")
                .setAllowedOriginPatterns("*")
                .withSockJS();
        registry.setErrorHandler(websocketErrorHandler);
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
