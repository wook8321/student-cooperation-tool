package com.stool.studentcooperationtools.domain.review.service;

import com.stool.studentcooperationtools.domain.member.Member;
import com.stool.studentcooperationtools.domain.member.Role;
import com.stool.studentcooperationtools.domain.member.repository.MemberRepository;
import com.stool.studentcooperationtools.domain.part.Part;
import com.stool.studentcooperationtools.domain.part.repository.PartRepository;
import com.stool.studentcooperationtools.domain.review.Review;
import com.stool.studentcooperationtools.domain.review.controller.response.ReviewFindResponse;
import com.stool.studentcooperationtools.domain.review.repository.ReviewRepository;
import com.stool.studentcooperationtools.domain.room.Room;
import com.stool.studentcooperationtools.domain.room.repository.RoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest
@Transactional
class ReviewServiceTest {

    @Autowired
    ReviewService reviewService;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    PartRepository partRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RoomRepository roomRepository;

    @DisplayName("역할의 id로 리뷰들을 최신순으로 조회한다.")
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

        String content1 = "평가 댓글1";
        String content2 = "평가 댓글2";
        String content3 = "평가 댓글3";
        reviewRepository.save(
                Review.builder()
                        .content(content1)
                        .member(anotherMember)
                        .part(part)
                        .build()
        );
        reviewRepository.save(
                Review.builder()
                        .content(content2)
                        .member(anotherMember)
                        .part(part)
                        .build()
        );
        reviewRepository.save(
                Review.builder()
                        .content(content3)
                        .member(anotherMember)
                        .part(part)
                        .build()
        );

        //when
        ReviewFindResponse response = reviewService.findReview(part.getId());

        //then
        assertThat(response.getNum()).isEqualTo(3);
        assertThat(response.getReviews()).hasSize(3)
                .extracting("content","memberId","nickName","profile")
                .containsExactly(
                        tuple(content3,anotherMember.getId(),anotherMember.getNickName(),anotherMember.getProfile()),
                        tuple(content2,anotherMember.getId(),anotherMember.getNickName(),anotherMember.getProfile()),
                        tuple(content1,anotherMember.getId(),anotherMember.getNickName(),anotherMember.getProfile())
                );
    }

}