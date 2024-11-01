package com.stool.studentcooperationtools.domain.chat.service;

import com.stool.studentcooperationtools.domain.chat.Chat;
import com.stool.studentcooperationtools.domain.chat.repository.ChatRepository;
import com.stool.studentcooperationtools.domain.member.Member;
import com.stool.studentcooperationtools.domain.member.Role;
import com.stool.studentcooperationtools.domain.member.repository.MemberRepository;
import com.stool.studentcooperationtools.domain.room.Room;
import com.stool.studentcooperationtools.domain.room.repository.RoomRepository;
import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
import com.stool.studentcooperationtools.websocket.controller.chat.request.ChatAddWebsocketRequest;
import com.stool.studentcooperationtools.websocket.controller.chat.response.ChatAddWebsocketResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ChatServiceTest {

    @Autowired
    ChatService chatService;

    @Autowired
    ChatRepository chatRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RoomRepository roomRepository;

    @DisplayName("채팅 등록할 때, 채팅을 등록할 유저가 존재하지 않는다면 에러가 발생한다.")
    @Test
    void addChatWithNotExistUser(){
        //given
        Long invalidRoomId = 1L;
        Long invalidMemberId = 1L;
        ChatAddWebsocketRequest request = ChatAddWebsocketRequest.builder()
                .roomId(invalidRoomId)
                .content("채팅 내용")
                .build();

        SessionMember sessionMember =SessionMember.builder()
                .memberSeq(invalidMemberId)
                .nickName("닉네임")
                .profile("프로필")
                .build();

        //when
        //then
        assertThatThrownBy(() -> chatService.addChat(request,sessionMember))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("채팅 등록에 실패했습니다.");
    }


    @DisplayName("채팅 등록할 때, 채팅을 등록할 방이 존재하지 않는다면 에러가 발생한다.")
    @Test
    void addChatWithNotExistRoom(){
        //given
        Long invalidRoomId = 1L;

        Member member = Member.builder()
                .role(Role.USER)
                .email("email")
                .profile("profile")
                .nickName("nickname")
                .build();
        memberRepository.save(member);

        ChatAddWebsocketRequest request = ChatAddWebsocketRequest.builder()
                .roomId(invalidRoomId)
                .content("채팅 내용")
                .build();

        SessionMember sessionMember =SessionMember.builder()
                .memberSeq(member.getId())
                .nickName("닉네임")
                .profile("프로필")
                .build();

        //when
        //then
        assertThatThrownBy(() -> chatService.addChat(request,sessionMember))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("채팅을 등록할 방이 존재하지 않습니다.");
    }



    @DisplayName("채팅 등록 요청을 받아 채팅을 등록한다.")
    @Test
    void addChat(){
        //given
        Member member = Member.builder()
                .role(Role.USER)
                .email("email")
                .profile("profile")
                .nickName("nickname")
                .build();
        memberRepository.save(member);
        Room room = Room.builder()
                .leader(member)
                .password("password")
                .participationNum(1)
                .build();
        roomRepository.save(room);

        ChatAddWebsocketRequest request = ChatAddWebsocketRequest.builder()
                .roomId(room.getId())
                .content("채팅 내용")
                .build();

        SessionMember sessionMember =SessionMember.builder()
                .memberSeq(member.getId())
                .nickName("닉네임")
                .profile("프로필")
                .build();
        //when
        ChatAddWebsocketResponse response = chatService.addChat(request, sessionMember);
        List<Chat> chats = chatRepository.findAll();
        //then
        assertThat(chats).hasSize(1);
    }
}