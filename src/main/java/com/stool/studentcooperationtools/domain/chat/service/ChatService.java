package com.stool.studentcooperationtools.domain.chat.service;

import com.stool.studentcooperationtools.domain.chat.Chat;
import com.stool.studentcooperationtools.domain.chat.controller.response.ChatFindResponse;
import com.stool.studentcooperationtools.domain.chat.repository.ChatRepository;
import com.stool.studentcooperationtools.domain.member.Member;
import com.stool.studentcooperationtools.domain.member.repository.MemberRepository;
import com.stool.studentcooperationtools.domain.room.Room;
import com.stool.studentcooperationtools.domain.room.repository.RoomRepository;
import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
import com.stool.studentcooperationtools.websocket.controller.chat.request.ChatAddWebsocketRequest;
import com.stool.studentcooperationtools.websocket.controller.chat.response.ChatAddWebsocketResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatService {

    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final ChatRepository chatRepository;

    public ChatFindResponse findChats(final Long roomId,final int page) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        return ChatFindResponse.of(
                chatRepository.findChatsByIdAndSlicingASC(roomId,pageRequest)
        );
    }

    @Transactional
    public ChatAddWebsocketResponse addChat(final ChatAddWebsocketRequest request, final SessionMember sessionMember) {
        Member member = memberRepository.findById(sessionMember.getMemberSeq())
                .orElseThrow(() -> new IllegalArgumentException("채팅 등록에 실패했습니다."));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("채팅을 등록할 방이 존재하지 않습니다."));

        Chat chat = Chat.of(request.getContent(), member,room);
        return ChatAddWebsocketResponse.of(chatRepository.save(chat));
    }
}
