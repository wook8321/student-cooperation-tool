package com.stool.studentcooperationtools.domain.room.controller.response;

import com.stool.studentcooperationtools.domain.room.Room;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RoomSearchDto {
    private Long roomId;
    private String title;
    private String topic;
    private int participationNum;

    @Builder
    private RoomSearchDto(final Long roomId,final String title, final String topic, final int participationNum) {
        this.roomId = roomId;
        this.title = title;
        this.topic = topic;
        this.participationNum = participationNum;
    }

    public static RoomSearchDto of(Room room) {
        return RoomSearchDto.builder()
                .topic(room.getTopic())
                .title(room.getTitle())
                .roomId(room.getId())
                .participationNum(room.getParticipationNum())
                .build();
    }
}
