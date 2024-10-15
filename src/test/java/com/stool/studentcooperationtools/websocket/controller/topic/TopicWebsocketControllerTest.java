package com.stool.studentcooperationtools.websocket.controller.topic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.stool.studentcooperationtools.domain.topic.service.TopicService;
import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
import com.stool.studentcooperationtools.websocket.CustomSessionHandlerAdapter;
import com.stool.studentcooperationtools.websocket.controller.topic.request.TopicAddSocketRequest;
import com.stool.studentcooperationtools.websocket.controller.topic.request.TopicDeleteSocketRequest;
import com.stool.studentcooperationtools.websocket.controller.topic.response.TopicAddSocketResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TopicWebsocketControllerTest {
    @LocalServerPort
    private int port;

    @MockBean
    private TopicService topicService;
    private String URL;
    private StompSession stompSession;
    private WebSocketStompClient stompClient;

    @BeforeEach
    void setUp() throws ExecutionException, InterruptedException, TimeoutException {
        URL = "ws://localhost:%d/ws-stomp".formatted(port);
        stompClient = new WebSocketStompClient(
                new SockJsClient(List.of(new WebSocketTransport(new StandardWebSocketClient()))));
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        ObjectMapper objectMapper = messageConverter.getObjectMapper();
        objectMapper.registerModules(new JavaTimeModule(), new ParameterNamesModule());
        stompClient.setMessageConverter(messageConverter);
        stompSession = stompClient.connectAsync(URL,
                new WebSocketHttpHeaders(),
                new StompSessionHandlerAdapter() {
                }).get(1, TimeUnit.SECONDS);
    }

    @AfterEach
    void tearDown(){
        if(stompSession.isConnected()){
            stompSession.disconnect();
        }
    }


    @DisplayName("웹소켓으로 추가할 주제를 추가한다.")
    @Test
    void addTopic() throws ExecutionException, InterruptedException, TimeoutException {
        //given
        Long roomId = 1L;
        String TopicDecisionSubUrl = "/sub/rooms/%d/topics".formatted(roomId);
        CustomSessionHandlerAdapter<TopicAddSocketResponse> handler =
                new CustomSessionHandlerAdapter<>(TopicAddSocketResponse.class);
        TopicAddSocketRequest request = TopicAddSocketRequest.builder()
                .topic("주제")
                .roomId(roomId)
                .build();
        TopicAddSocketResponse response = TopicAddSocketResponse.builder()
                .topicId(1L)
                .topic("주제")
                .memberId(1L)
                .build();

        Mockito.when(topicService.addTopic(Mockito.any(TopicAddSocketRequest.class),Mockito.any(SessionMember.class)))
                .thenReturn(response);

        stompSession.subscribe(TopicDecisionSubUrl,handler);
        //when
        stompSession.send("/pub/topics/add",request);
        TopicAddSocketResponse result = handler.get(1);
        //then
        assertThat(stompSession.isConnected()).isTrue();
        assertThat(result).isNotNull()
                .extracting("topicId","topic","memberId")
                .containsExactly(1L,"주제",1L);
    }

    @DisplayName("웹소켓으로 삭제할 주제를 받아 삭제한다.")
    @Test
    void deleteTopic() throws ExecutionException, InterruptedException, TimeoutException {
        //given
        String TopicDecisionSubUrl = "/sub/rooms/%d/topics".formatted(1L);
        CustomSessionHandlerAdapter<Boolean> handler =
                new CustomSessionHandlerAdapter<>(Boolean.class);
        TopicDeleteSocketRequest request = TopicDeleteSocketRequest.builder()
                .roomId(1L)
                .topicId(1L)
                .build();
        Mockito.when(topicService.deleteTopic(Mockito.any(TopicDeleteSocketRequest.class)))
                .thenReturn(true);

        stompSession.subscribe(TopicDecisionSubUrl,handler);
        //when
        stompSession.send("/pub/topics/delete",request);
        boolean result = handler.get(3);
        //then
        assertThat(stompSession.isConnected()).isTrue();
        assertThat(result).isTrue();
    }

}