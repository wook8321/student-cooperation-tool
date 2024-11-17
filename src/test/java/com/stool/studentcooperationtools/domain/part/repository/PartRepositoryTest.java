package com.stool.studentcooperationtools.domain.part.repository;

import com.stool.studentcooperationtools.domain.IntegrationTest;
import com.stool.studentcooperationtools.domain.file.File;
import com.stool.studentcooperationtools.domain.file.FileType;
import com.stool.studentcooperationtools.domain.file.repository.FileRepository;
import com.stool.studentcooperationtools.domain.member.Member;
import com.stool.studentcooperationtools.domain.member.Role;
import com.stool.studentcooperationtools.domain.member.repository.MemberRepository;
import com.stool.studentcooperationtools.domain.part.Part;
import com.stool.studentcooperationtools.domain.room.Room;
import com.stool.studentcooperationtools.domain.room.repository.RoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;


@Transactional
class PartRepositoryTest extends IntegrationTest {

    @Autowired
    PartRepository partRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    FileRepository fileRepository;

    @DisplayName("조회할 자료조사가 속하는 방의 식별키를 받아 자료조사 역할들을 조회 한다.")
    @Test
    void findAllByRoomId(){
        //given
        Member member = Member.builder()
                .email("email")
                .nickName("닉네임")
                .profile("profile")
                .role(Role.USER)
                .build();
        memberRepository.save(member);
        Room room = Room.builder()
                .password("password")
                .title("제목")
                .leader(member)
                .participationNum(1)
                .build();
        roomRepository.save(room);

        String content = "조사할 부분";
        Part part = Part.builder()
                .partName(content)
                .room(room)
                .member(member)
                .build();

        String originalName = "originalFileName";
        String fileName = UUID.randomUUID().toString();
        String filePath = "filePath";
        File file = File.builder()
                .part(part)
                .originalName(originalName)
                .fileName(fileName)
                .fileType(FileType.JPG)
                .build();
        part.addFile(file);
        partRepository.save(part);
        fileRepository.save(file);

        //when
        List<Part> result = partRepository.findAllByRoomId(room.getId());

        //then
        assertThat(result).hasSize(1)
                .extracting("partName")
                .containsExactly(content);
        assertThat(result.get(0).getFileList()).hasSize(1)
                .extracting("originalName","fileName")
                .containsExactly(tuple(originalName,fileName));
    }

    @DisplayName("역할의 id와 방장 id를 받아 역할을 삭제한다.")
    @Test
    void deletePartByLeader(){
        //given
        Member leader = Member.builder()
                .role(Role.USER)
                .email("email")
                .profile("profile")
                .nickName("nickname")
                .build();

        Member owner = Member.builder()
                .role(Role.USER)
                .email("email")
                .profile("profile")
                .nickName("nickname")
                .build();

        memberRepository.saveAll(List.of(leader,owner));
        Room room = Room.builder()
                .leader(leader)
                .password("password")
                .participationNum(2)
                .build();
        roomRepository.save(room);
        Part part = Part.builder()
                .room(room)
                .partName("조사할 부분")
                .member(owner)
                .build();
        partRepository.save(part);

        //when
        partRepository.deletePartByLeaderOrOwner(part.getId(), leader.getId());
        List<Part> result = partRepository.findAll();
        //then
        assertThat(result).hasSize(0);
    }

    @DisplayName("역할의 id와 역할을 만든 본인의 id를 받아 역할을 삭제한다.")
    @Test
    void deletePartByOwner(){
        //given
        Member leader = Member.builder()
                .role(Role.USER)
                .email("email")
                .profile("profile")
                .nickName("nickname")
                .build();

        Member owner = Member.builder()
                .role(Role.USER)
                .email("email")
                .profile("profile")
                .nickName("nickname")
                .build();

        memberRepository.saveAll(List.of(leader,owner));
        Room room = Room.builder()
                .leader(leader)
                .password("password")
                .participationNum(2)
                .build();
        roomRepository.save(room);
        Part part = Part.builder()
                .room(room)
                .partName("조사할 부분")
                .member(owner)
                .build();
        partRepository.save(part);

        //when
        partRepository.deletePartByLeaderOrOwner(part.getId(), owner.getId());
        List<Part> result = partRepository.findAll();
        //then
        assertThat(result).hasSize(0);
    }
}