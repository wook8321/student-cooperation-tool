package com.stool.studentcooperationtools.websocket.controller.chat;

import com.stool.studentcooperationtools.domain.chat.service.ChatService;
import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
import com.stool.studentcooperationtools.websocket.WebsocketMessageType;
import com.stool.studentcooperationtools.websocket.WebsocketTestSupport;
import com.stool.studentcooperationtools.websocket.controller.chat.request.ChatAddWebsocketRequest;
import com.stool.studentcooperationtools.websocket.controller.chat.request.ChatDeleteWebsocketRequest;
import com.stool.studentcooperationtools.websocket.controller.chat.response.ChatAddWebsocketResponse;
import com.stool.studentcooperationtools.websocket.controller.chat.response.ChatDeleteWebsocketResponse;
import com.stool.studentcooperationtools.websocket.controller.request.WebsocketResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

class ChatWebsocketControllerTest extends WebsocketTestSupport {

    @MockBean
    private ChatService chatService;

    @DisplayName("채팅 추가 요청을 받아서 채팅을 등록 후 응답한다.")
    @Test
    void addChat() throws ExecutionException, InterruptedException, TimeoutException {
        //given
        Long roomId = 1L;
        String chatSubUrl = "/sub/rooms/"+ roomId +"/chat";
        Long memberId = 1L;
        String content = "hi";
        Long chatId = 1L;
        String memberNickname = "test";

        ChatAddWebsocketRequest request = ChatAddWebsocketRequest.builder()
                .content("채팅 내용")
                .roomId(roomId)
                .build();

        ChatAddWebsocketResponse response = ChatAddWebsocketResponse.builder()
                .memberId(memberId)
                .content(content)
                .chatId(chatId)
                .nickName(memberNickname)
                .build();

        Mockito.when(chatService.addChat(
                Mockito.any(ChatAddWebsocketRequest.class),Mockito.any(SessionMember.class)
        )).thenReturn(response);

        stompSession.subscribe(chatSubUrl,resultHandler);

        //when
        stompSession.send("/pub/chats/add",request);
        WebsocketResponse websocketResponse = resultHandler.get(3);

        //then
        assertThat(stompSession.isConnected()).isTrue();
        assertThat(websocketResponse.getMessageType()).isEqualTo(WebsocketMessageType.CHAT_ADD);
        assertThat(websocketResponse.getData()).isNotNull();
    }

    @DisplayName("채팅 삭제 요청을 받아서 채팅을 삭제 후 응답한다.")
    @Test
    void deleteChat() throws ExecutionException, InterruptedException, TimeoutException {
        //given
        Long roomId = 1L;
        String chatSubUrl = "/sub/rooms/"+ roomId +"/chat";
        Long chatId = 1L;

        ChatDeleteWebsocketRequest request = ChatDeleteWebsocketRequest.builder()
                .roomId(roomId)
                .chatId(chatId)
                .build();

        ChatDeleteWebsocketResponse response = new ChatDeleteWebsocketResponse(chatId);

        Mockito.when(chatService.deleteChat(
                Mockito.any(ChatDeleteWebsocketRequest.class),Mockito.any(SessionMember.class)
        )).thenReturn(response);

        stompSession.subscribe(chatSubUrl,resultHandler);

        //when
        stompSession.send("/pub/chats/delete",request);
        WebsocketResponse websocketResponse = resultHandler.get(3);

        //then
        assertThat(stompSession.isConnected()).isTrue();
        assertThat(websocketResponse.getMessageType()).isEqualTo(WebsocketMessageType.CHAT_DELETE);
        assertThat(websocketResponse.getData()).isNotNull();
    }

}