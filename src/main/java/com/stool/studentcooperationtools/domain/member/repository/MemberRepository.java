package com.stool.studentcooperationtools.domain.member.repository;

import com.stool.studentcooperationtools.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findMemberByEmail(String email);

}
