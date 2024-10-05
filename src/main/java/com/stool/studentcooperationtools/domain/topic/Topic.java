package com.stool.studentcooperationtools.domain.topic;

import com.stool.studentcooperationtools.domain.room.Room;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String topic;

    private int voteCount;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Room room;

}
