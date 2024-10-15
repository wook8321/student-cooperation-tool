package com.stool.studentcooperationtools.domain.room;

import com.stool.studentcooperationtools.domain.BaseTimeEntity;
import com.stool.studentcooperationtools.domain.member.Member;
import com.stool.studentcooperationtools.domain.participation.Participation;
import com.stool.studentcooperationtools.domain.topic.Topic;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,length = 20)
    private String title;

    @Column(nullable = false, length = 20)
    private String password;

    @OneToMany(mappedBy = "room")
    private List<Participation> participationList = new ArrayList<>();

    @Column
    private int participationNum;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Topic mainTopic;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member leader;

    @Builder
    private Room(final String title, final String password, final int participationNum, final Member leader) {
        this.title = title;
        this.password = password;
        this.participationNum = participationNum;
        this.leader = leader;
    }
    
    public String getTopic(){
        if(mainTopic == null){
            return "미정";
        }
        return this.mainTopic.getTopic();
    }

    public void addParticipation(Participation participation){
        participationList.add(participation);
    }

    public Boolean verifyPassword(String password){
        return this.password.equals(password);
    }

    public void updateTopic(Topic topic){
        this.mainTopic = topic;
    }

    public void addParticipant(){
        this.participationNum++;
    }
}
