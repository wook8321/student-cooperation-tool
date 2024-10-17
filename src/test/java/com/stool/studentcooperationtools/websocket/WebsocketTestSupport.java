package com.stool.studentcooperationtools.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class WebsocketTestSupport {

    @LocalServerPort
    int port;

    @Autowired
    protected DataSource dataSource;
    protected String URL;
    protected StompSession stompSession;
    protected WebSocketStompClient stompClient;


    @BeforeEach
    void setUp() throws ExecutionException, InterruptedException, TimeoutException {
        URL = "ws://localhost:%d/ws-stomp".formatted(port);
        stompClient = new WebSocketStompClient(
                new SockJsClient(List.of(new WebSocketTransport(new StandardWebSocketClient()))));
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        ObjectMapper objectMapper = messageConverter.getObjectMapper();
        objectMapper.registerModules(new JavaTimeModule(), new ParameterNamesModule());
        stompClient.setMessageConverter(messageConverter);
        try (Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("sql/SpringSessionCreate.sql"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        StompHeaders stompHeaders = new StompHeaders();
        stompHeaders.add("JSESSIONID","testSession");
        stompSession = stompClient.connectAsync(URL,
                new WebSocketHttpHeaders(),
                stompHeaders,
                new StompSessionHandlerAdapter() {
                }).get(10, TimeUnit.SECONDS);
    }

    @AfterEach
    void tearDown(){
        if(stompSession.isConnected()){
            stompSession.disconnect();
        }

        try (Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("sql/SpringSessionDelete.sql"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
