package com.stool.studentcooperationtools.domain.file.repository;

import com.stool.studentcooperationtools.domain.IntegrationTest;
import com.stool.studentcooperationtools.domain.file.File;
import com.stool.studentcooperationtools.domain.file.FileType;
import com.stool.studentcooperationtools.domain.member.Member;
import com.stool.studentcooperationtools.domain.member.Role;
import com.stool.studentcooperationtools.domain.member.repository.MemberRepository;
import com.stool.studentcooperationtools.domain.part.Part;
import com.stool.studentcooperationtools.domain.part.repository.PartRepository;
import com.stool.studentcooperationtools.domain.room.Room;
import com.stool.studentcooperationtools.domain.room.repository.RoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@Transactional
class FileRepositoryTest extends IntegrationTest {

    @Autowired
    FileRepository fileRepository;

    @Autowired
    PartRepository partRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RoomRepository roomRepository;

    @DisplayName("삭제할 유저의 id와 삭제할 파일의 id로 파일을 삭제한다.")
    @Test
    void deleteFileByIdAndLeaderOrOwner(){
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
        File file = File.builder()
                .part(part)
                .originalName(originalName)
                .fileName(fileName)
                .fileType(FileType.DOCS)
                .build();
        part.addFile(file);
        partRepository.save(part);
        fileRepository.save(file);
        //when
        fileRepository.deleteFileByIdAndLeaderOrOwner(member.getId(), file.getId());
        List<File> files = fileRepository.findAll();
        //then
        assertThat(files).isEmpty();
    }
}