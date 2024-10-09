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
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final FriendshipRepository friendshipRepository;

    //유저의 친구 목록 조회
    public MemberFindResponse findFriends() {
        List<Member> friends = memberRepository.findFriendsByMemberId(1L);
        return MemberFindResponse.of(friends);
    }

    //relation에 따라 "nickName"과 친구 관계거나 아닌 유저들 조회
    public MemberSearchResponse searchFriend(final boolean relation, final String nickName) {
        if (relation){
            Optional<List<Member>> findFriends = memberRepository.findFriendsByMemberNickName(nickName);
            if(findFriends.isEmpty()){
                throw new IllegalArgumentException("해당 검색어와 일치하는 유저가 존재하지 않습니다.");
            }
            return MemberSearchResponse.of(findFriends.get());
        }
        else{
            Optional<List<Member>> findNonFriends = memberRepository.findNonFriendsByMemberNickName(nickName);
            if(findNonFriends.isEmpty()){
                throw new IllegalArgumentException("해당 검색어와 일치하는 유저가 존재하지 않습니다.");
            }
            return MemberSearchResponse.of(findNonFriends.get());
        }
    }

    //친구추가 - 1. 검색한 email을 가진 멤버 조회 2. 이미 친구인지 조회 3. friendship 엔티티 만들어서 등록
    public Boolean addFriend(final MemberAddRequest request) {
        String findEmail = request.getEmail();
        Optional<Member> optionalMember = memberRepository.findMemberByEmail(findEmail);
        if (optionalMember.isEmpty()){
            throw new IllegalArgumentException("해당 유저가 존재하지 않습니다.");
        }
        Member newFriend = optionalMember.get();
        boolean existsFriendship = friendshipRepository.existsByMemberIdAndFriendId(1L, newFriend.getId());
        if (existsFriendship){
            throw new IllegalArgumentException("이미 친구 관계의 유저입니다.");
        }
        friendshipRepository.save(Friendship.of(newFriend, newFriend)); //첫 파라미터 유저로 변경 필요
        return true;
    }
}
