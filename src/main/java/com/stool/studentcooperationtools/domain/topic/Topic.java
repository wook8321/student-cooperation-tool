package com.stool.studentcooperationtools.domain.topic;

import com.stool.studentcooperationtools.domain.BaseTimeEntity;
import com.stool.studentcooperationtools.domain.member.Member;
import com.stool.studentcooperationtools.domain.room.Room;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Topic extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String topic;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false)
    private int voteNum;

    @Builder
    private Topic(final String topic, final Room room,final Member member,final int voteNum) {
        this.topic = topic;
        this.room = room;
        this.member = member;
        this.voteNum = voteNum;
    }

    public static Topic of(final String topic, final Room room,final Member member){
        return new Topic(topic,room,member,0);
    }

    public void addVoteNum(){
        voteNum += 1;
    }

    public void minusVoteNum(){
        voteNum -= 1;
    }
}
