package com.stool.studentcooperationtools.domain.file.repository;

import com.stool.studentcooperationtools.IntegrationTest;
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
                .fileType(FileType.DOCX)
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

    @DisplayName("part의 id들을 받아서 해당 id들을 참조하는 File을 삭제한다.")
    @Test
    void deleteAllByInPartId(){
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
        Part part1 = Part.builder()
                .partName(content)
                .room(room)
                .member(member)
                .build();
        Part part2 = Part.builder()
                .partName(content)
                .room(room)
                .member(member)
                .build();

        String originalName = "originalFileName";
        String fileName = UUID.randomUUID().toString();
        File file1 = File.builder()
                .part(part1)
                .originalName(originalName)
                .fileName(fileName)
                .fileType(FileType.DOCX)
                .build();
        File file2 = File.builder()
                .part(part2)
                .originalName(originalName)
                .fileName(fileName)
                .fileType(FileType.DOCX)
                .build();
        part1.addFile(file1);
        part2.addFile(file2);
        partRepository.saveAll(List.of(part1,part2));
        fileRepository.saveAll(List.of(file1,file2));
        //when
        fileRepository.deleteAllByInPartId(List.of(part1.getId(),part2.getId()));
        List<File> files = fileRepository.findAll();
        //then
        assertThat(files).isEmpty();
    }
}