package com.stool.studentcooperationtools.domain.friendship;

import com.stool.studentcooperationtools.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friendship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member me;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member friend;

    @Builder
    private Friendship(final Member me, final Member friend) {
        this.me = me;
        this.friend = friend;
    }

    public static Friendship of(Member me, Member friend){
        return Friendship.builder()
                .me(me)
                .friend(friend)
                .build();
    }
}
