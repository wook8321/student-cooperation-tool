package com.stool.studentcooperationtools.websocket.controller.vote.response;

import com.stool.studentcooperationtools.domain.topic.Topic;
import lombok.Builder;
import lombok.Getter;

@Getter
public class VoteUpdateWebSocketResponse {

    private Long topicId;
    private int voteNum;

    @Builder
    private VoteUpdateWebSocketResponse(final int voteNum, final Long topicId) {
        this.topicId = topicId;
        this.voteNum = voteNum;
    }

    public static VoteUpdateWebSocketResponse of( final Topic topic){
        return VoteUpdateWebSocketResponse.builder()
                .topicId(topic.getId())
                .voteNum(topic.getVoteNum())
                .build();
    }

}
