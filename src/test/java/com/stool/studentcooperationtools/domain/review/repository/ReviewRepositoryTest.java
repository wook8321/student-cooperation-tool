package com.stool.studentcooperationtools.domain.review.repository;

import com.stool.studentcooperationtools.IntegrationTest;
import com.stool.studentcooperationtools.domain.member.Member;
import com.stool.studentcooperationtools.domain.member.Role;
import com.stool.studentcooperationtools.domain.member.repository.MemberRepository;
import com.stool.studentcooperationtools.domain.part.Part;
import com.stool.studentcooperationtools.domain.part.repository.PartRepository;
import com.stool.studentcooperationtools.domain.review.Review;
import com.stool.studentcooperationtools.domain.room.Room;
import com.stool.studentcooperationtools.domain.room.repository.RoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class ReviewRepositoryTest extends IntegrationTest {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    PartRepository partRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RoomRepository roomRepository;

    @DisplayName("역할의 id로 리뷰들을 조회한다.")
    @Test
    void findAllByPartOrderByIdIdAsc(){
        //given
        Member member = Member.builder()
                .email("email")
                .nickName("닉네임")
                .profile("profile")
                .role(Role.USER)
                .build();

        Member anotherMember = Member.builder()
                .email("평가자 이메일")
                .nickName("평가자")
                .profile("평가자 프로필")
                .role(Role.USER)
                .build();

        memberRepository.saveAll(List.of(anotherMember,member));
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
        partRepository.save(part);

        Review review = Review.builder()
                .content("평가 댓글")
                .member(anotherMember)
                .build();

        String content1 = "평가 댓글1";
        String content2 = "평가 댓글2";
        String content3 = "평가 댓글3";
        reviewRepository.saveAll(List.of(
                Review.builder()
                        .content(content1)
                        .member(anotherMember)
                        .part(part)
                        .build(),
                Review.builder()
                        .content(content2)
                        .member(anotherMember)
                        .part(part)
                        .build(),
                Review.builder()
                        .content(content3)
                        .member(anotherMember)
                        .part(part)
                        .build()
        ));

        //when
        List<Review> result = reviewRepository.findAllByPartIdOrderByIdDesc(part.getId());

        //then
        assertThat(result).hasSize(3)
                .extracting("content")
                .containsExactly(content3,content2,content1);
    }

    @DisplayName("제거할 리뷰의 id와 제거하는 유저의 id로 리뷰를 삭제한다.")
    @Test
    void deleteReviewByMemberIdAndReviewId(){
        //given
        Member member = Member.builder()
                .email("email")
                .nickName("닉네임")
                .profile("profile")
                .role(Role.USER)
                .build();

        Member owner = Member.builder()
                .email("평가자 이메일")
                .nickName("평가자")
                .profile("평가자 프로필")
                .role(Role.USER)
                .build();

        memberRepository.saveAll(List.of(owner,member));
        Room room = Room.builder()
                .password("password")
                .title("제목")
                .leader(member)
                .participationNum(2)
                .build();
        roomRepository.save(room);

        String content = "조사할 부분";
        Part part = Part.builder()
                .partName(content)
                .room(room)
                .member(member)
                .build();
        partRepository.save(part);

        Review review = Review.builder()
                .content("평가 댓글")
                .member(owner)
                .part(part)
                .build();

        reviewRepository.save(review);

        //when
        int result = reviewRepository.deleteReviewByMemberIdAndReviewId(review.getId(), owner.getId());
        List<Review> reviews = reviewRepository.findAll();
        //then
        assertThat(result).isEqualTo(1);
        assertThat(reviews).isEmpty();
    }

    @DisplayName("part의 id들을 받아서 해당 part id를 참조하는 Review를 모두 제거한다.")
    @Test
    void deleteAllByInPartId(){
        //given
        Member member = Member.builder()
                .email("email")
                .nickName("닉네임")
                .profile("profile")
                .role(Role.USER)
                .build();

        Member owner = Member.builder()
                .email("평가자 이메일")
                .nickName("평가자")
                .profile("평가자 프로필")
                .role(Role.USER)
                .build();

        memberRepository.saveAll(List.of(owner,member));
        Room room = Room.builder()
                .password("password")
                .title("제목")
                .leader(member)
                .participationNum(2)
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
        partRepository.saveAll(List.of(part1,part2));
        //when
        reviewRepository.deleteAllByInPartId(List.of(part1.getId(),part2.getId()));
        List<Review> reviews = reviewRepository.findAll();
        //then
        assertThat(reviews).isEmpty();
    }
}