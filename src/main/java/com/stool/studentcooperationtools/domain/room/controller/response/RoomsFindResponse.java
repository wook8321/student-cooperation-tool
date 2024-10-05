package com.stool.studentcooperationtools.domain.room.controller.response;

import com.stool.studentcooperationtools.domain.room.Room;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class RoomsFindResponse {

    private int num;
    private List<RoomFindDto> rooms;

    @Builder
    private RoomsFindResponse(final int num, final List<RoomFindDto> rooms) {
        this.num = num;
        this.rooms = rooms;
    }

    public static RoomsFindResponse of(List<Room> rooms){
        return RoomsFindResponse.builder()
                .num(rooms.size())
                .rooms(
                        rooms.stream()
                                .map(RoomFindDto::of)
                                .toList()
                )
                .build();
    }
}
