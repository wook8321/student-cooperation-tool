package com.stool.studentcooperationtools.domain.member.service;

import com.stool.studentcooperationtools.domain.friendship.Friendship;
import com.stool.studentcooperationtools.domain.friendship.repository.FriendshipRepository;
import com.stool.studentcooperationtools.domain.member.Member;
import com.stool.studentcooperationtools.domain.member.repository.MemberRepository;
import com.stool.studentcooperationtools.domain.member.controller.request.MemberAddRequest;
import com.stool.studentcooperationtools.domain.member.controller.response.MemberFindResponse;
import com.stool.studentcooperationtools.domain.member.controller.response.MemberSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final FriendshipRepository friendshipRepository;

    //유저의 친구 목록 조회
    public MemberFindResponse findFriends() {
        //todo user의 id 파라미터 입력
        List<Member> friends = memberRepository.findFriendsByMemberId(1L);
        return MemberFindResponse.of(friends);
    }

    //relation에 따라 "nickName"과 친구 관계거나 아닌 유저들 조회
    public MemberSearchResponse searchFriend(final boolean relation, final String nickName) {
        //todo user의 id 파라미터 입력
        if (relation){
            List<Member> findFriends = memberRepository.findFriendsByMemberNickName(nickName, 1L);
            return MemberSearchResponse.of(findFriends);
        }
        else{
            List<Member> findNonFriends = memberRepository.findNonFriendsByMemberNickName(nickName, 1L);
            return MemberSearchResponse.of(findNonFriends);
        }
    }

    //친구추가 - 1. 검색한 email을 가진 멤버 조회 2. friendship 엔티티 만들어서 등록
    @Transactional
    public Boolean addFriend(final MemberAddRequest request) {
        //todo 본인 member 엔티티 조회
        String findEmail = request.getEmail();
        Member friend = memberRepository.findMemberByEmail(findEmail)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
        friendshipRepository.save(Friendship.of(friend, friend)); //첫 파라미터 유저로 변경 필요
        return true;
    }
}
