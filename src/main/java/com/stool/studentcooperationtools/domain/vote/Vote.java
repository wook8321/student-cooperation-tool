package com.stool.studentcooperationtools.domain.vote;

import com.stool.studentcooperationtools.domain.BaseTimeEntity;
import com.stool.studentcooperationtools.domain.member.Member;
import com.stool.studentcooperationtools.domain.topic.Topic;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    private Member voter;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    private Topic topic;

    @Builder
    private Vote(final Member voter, final Topic topic) {
        this.voter = voter;
        this.topic = topic;
    }

    public static Vote of(Member member, Topic topic){
        Vote vote = Vote.builder()
                .voter(member)
                .topic(topic)
                .build();

        topic.addVote(vote);
        return vote;
    }
}
