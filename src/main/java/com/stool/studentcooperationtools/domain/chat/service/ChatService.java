package com.stool.studentcooperationtools.domain.chat.service;

import com.stool.studentcooperationtools.domain.chat.controller.response.ChatFindResponse;
import com.stool.studentcooperationtools.domain.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    public ChatFindResponse findChats(final Long roomId,final int page) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        return ChatFindResponse.of(
                chatRepository.findChatsByIdAndSlicingASC(roomId,pageRequest)
        );
    }
}
