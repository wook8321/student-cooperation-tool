package com.stool.studentcooperationtools.domain.room;

import com.stool.studentcooperationtools.domain.BaseTimeEntity;
import com.stool.studentcooperationtools.domain.topic.Topic;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,length = 20)
    private String title;

    @Column
    private int participationNum;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Topic mainTopic;

    public String getTopic(){
        return this.mainTopic.getTopic();
    }

}
