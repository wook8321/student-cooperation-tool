package com.stool.studentcooperationtools.domain.participation;

import com.stool.studentcooperationtools.domain.BaseTimeEntity;
import com.stool.studentcooperationtools.domain.member.Member;
import com.stool.studentcooperationtools.domain.room.Room;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Participation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Room room;

    @Builder
    private Participation(final Member member, final Room room) {
        this.member = member;
        this.room = room;
    }

<<<<<<< HEAD
    public Participation of(Member member, Room room){
=======
    public static Participation of(Member member, Room room){
>>>>>>> daa7c781c6c5fad1d18527e8a96ea8b1d0f2a862
        Participation participation = Participation.builder()
                .room(room)
                .member(member)
                .build();
        room.addParticipation(participation);
        return participation;
    }
}
