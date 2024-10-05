package com.stool.studentcooperationtools.domain.topic.controller.response;

import com.stool.studentcooperationtools.domain.topic.Topic;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class TopicFindResponse {

    private int num;
    private List<TopicFindDto> topics;

    @Builder
    private TopicFindResponse(final int num, final List<TopicFindDto> topics) {
        this.num = num;
        this.topics = topics;
    }

    public static TopicFindResponse of(List<Topic> topics){
        return TopicFindResponse.builder()
                .num(topics.size())
                .topics(
                        topics.stream()
                                .map(TopicFindDto::of)
                                .toList()
                )
                .build();
    }
}
