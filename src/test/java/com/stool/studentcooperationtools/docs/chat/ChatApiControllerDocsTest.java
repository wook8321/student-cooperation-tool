package com.stool.studentcooperationtools.docs.chat;


import com.stool.studentcooperationtools.docs.RestDocsSupport;
import com.stool.studentcooperationtools.domain.chat.controller.ChatApiController;
import com.stool.studentcooperationtools.domain.chat.controller.response.ChatFindDto;
import com.stool.studentcooperationtools.domain.chat.controller.response.ChatFindResponse;
import com.stool.studentcooperationtools.domain.chat.service.ChatService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChatApiControllerDocsTest extends RestDocsSupport {

    private final ChatService chatService = mock(ChatService.class);
    @Override
    protected Object initController() {
        return new ChatApiController(chatService);
    }

    @Test
    void findChats() throws Exception {
        //given
        String roomId = "1";
        List<ChatFindDto> chatFindDtoList = List.of(
                ChatFindDto.builder()
                        .chatId(1L)
                        .createdTime(LocalDate.of(2024,10,6))
                        .nickName("라이푸니")
                        .content("안녕하세요")
                        .profile("프로필")
                        .build()
        );

        ChatFindResponse response = ChatFindResponse.builder()
                .num(chatFindDtoList.size())
                .chats(chatFindDtoList)
                .build();

        Mockito.when(chatService.findChats(Mockito.anyLong()))
                .thenReturn(response);

        //when
        //then
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/rooms/"+ roomId + "/chats")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("chat-find",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(NUMBER)
                                        .description("상태 코드"),
                                fieldWithPath("status").type(STRING)
                                        .description("응답 상태"),
                                fieldWithPath("data").type(OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.num").type(NUMBER)
                                        .description("조회된 채팅 개수"),
                                fieldWithPath("data.chats[]").type(ARRAY)
                                        .description("채팅 정보 리스트"),
                                fieldWithPath("data.chats[].chatId").type(NUMBER)
                                        .description("채팅의 식별키"),
                                fieldWithPath("data.chats[].createdTime").type(ARRAY)
                                        .description("채팅의 생성 날짜"),
                                fieldWithPath("data.chats[].nickName").type(STRING)
                                        .description("채팅을 작성한 유저의 닉네임"),
                                fieldWithPath("data.chats[].profile").type(STRING)
                                        .description("유저 프로필"),
                                fieldWithPath("data.chats[].content").type(STRING)
                                        .description("채팅의 내용")
                        )
                )
                );

    }
}
