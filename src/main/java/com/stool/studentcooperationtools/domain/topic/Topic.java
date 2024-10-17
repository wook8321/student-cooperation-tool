package com.stool.studentcooperationtools.domain.topic;

import com.stool.studentcooperationtools.domain.BaseTimeEntity;
<<<<<<< HEAD
=======
import com.stool.studentcooperationtools.domain.member.Member;
>>>>>>> daa7c781c6c5fad1d18527e8a96ea8b1d0f2a862
import com.stool.studentcooperationtools.domain.room.Room;
import com.stool.studentcooperationtools.domain.vote.Vote;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Topic extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String topic;

    @BatchSize(size = 10)
    @OneToMany(mappedBy = "topic")
    private List<Vote> votes = new ArrayList<>();

<<<<<<< HEAD
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Room room;

    @Builder
    private Topic(final String topic, final Room room) {
        this.topic = topic;
        this.room = room;
    }

    public static Topic of(final String topic, final Room room){
        return new Topic(topic,room);
=======
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    private Topic(final String topic, final Room room,final Member member) {
        this.topic = topic;
        this.room = room;
        this.member = member;
    }

    public static Topic of(final String topic, final Room room,final Member member){
        return new Topic(topic,room,member);
>>>>>>> daa7c781c6c5fad1d18527e8a96ea8b1d0f2a862
    }

    public void addVote(Vote vote){
        votes.add(vote);
    }
}
