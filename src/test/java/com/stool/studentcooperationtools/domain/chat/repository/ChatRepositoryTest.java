package com.stool.studentcooperationtools.domain.chat.repository;

import com.stool.studentcooperationtools.IntegrationTest;
import com.stool.studentcooperationtools.domain.chat.Chat;
import com.stool.studentcooperationtools.domain.member.Member;
import com.stool.studentcooperationtools.domain.member.Role;
import com.stool.studentcooperationtools.domain.member.repository.MemberRepository;
import com.stool.studentcooperationtools.domain.room.Room;
import com.stool.studentcooperationtools.domain.room.repository.RoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Transactional
class ChatRepositoryTest extends IntegrationTest {

    @Autowired
    ChatRepository chatRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    MemberRepository memberRepository;

    // Chat을 생성하는 테스트 용 함수
    // Builder를 쓰다보니 테스트 코드가 길어진다
    // 해당 테스트 클래스에서는 chat을 많이 생성하기 때문에 코드 가독성을 높일 수 있다.
    private Chat createChat(String content, Member member,Room room){
        return Chat.builder()
                .member(member)
                .room(room)
                .content(content)
                .build();
    }

    @DisplayName("방에 속하는 채팅을 10개씩 조회하고 10개만 존재할 때, 다음 page 채팅은 존재 하지 않는다.")
    @Test
    void findChatsByIdAndSlicingASCWithNotExistNext(){
        //given
        int pageSize = 10;
        int page = 0;
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
        List<Chat> chats = List.of(
                createChat("content1",member,room),
                createChat("content2",member,room),
                createChat("content3",member,room),
                createChat("content4",member,room),
                createChat("content5",member,room),
                createChat("content6",member,room),
                createChat("content7",member,room),
                createChat("content8",member,room),
                createChat("content9",member,room),
                createChat("content10",member,room)
        );
        chatRepository.saveAll(chats);
        PageRequest pageRequest = PageRequest.of(page, pageSize);

        //when
        Slice<Chat> result = chatRepository.findChatsByIdAndSlicingASC(room.getId(), pageRequest);
        List<Chat> content = result.getContent();

        //then
        assertThat(result.hasNext()).isFalse();
        assertThat(content).hasSize(10)
                .extracting("content")
                .containsExactlyInAnyOrder(
                        "content1","content2","content3",
                        "content4","content5","content6","content7",
                        "content8","content9","content10"
                );

    }

    @DisplayName("방에 속하는 채팅이 11개 있고, 10개씩 조회할 경우 마지막은 1개가 조회된다.")
    @Test
    void findChatsByIdAndSlicingASCWithLastPage(){
        //given
        int pageSize = 10;
        int page = 1;
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
        List<Chat> chats = List.of(
                createChat("content1",member,room),
                createChat("content2",member,room),
                createChat("content3",member,room),
                createChat("content4",member,room),
                createChat("content5",member,room),
                createChat("content6",member,room),
                createChat("content7",member,room),
                createChat("content8",member,room),
                createChat("content9",member,room),
                createChat("content10",member,room),
                createChat("last",member,room)
                );
        chatRepository.saveAll(chats);
        PageRequest pageRequest = PageRequest.of(page, pageSize);

        //when
        Slice<Chat> result = chatRepository.findChatsByIdAndSlicingASC(room.getId(), pageRequest);
        List<Chat> content = result.getContent();

        //then
        assertThat(result.hasPrevious()).isTrue();
        assertThat(content).hasSize(1)
                .extracting("content")
                .containsExactlyInAnyOrder(
                        "last"
                );

    }

}