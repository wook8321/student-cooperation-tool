package com.stool.studentcooperationtools.domain.room.controller.response;

import com.stool.studentcooperationtools.domain.room.Room;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class RoomSearchResponse {

    private int num;
    private List<RoomSearchDto> rooms;

    @Builder
    private RoomSearchResponse(final int num, final List<RoomSearchDto> rooms) {
        this.num = num;
        this.rooms = rooms;
    }

    public static RoomSearchResponse of(List<Room> rooms){
        return RoomSearchResponse.builder()
                .num(rooms.size())
                .rooms(
                        rooms.stream()
                                .map(RoomSearchDto::of)
                                .toList()
                )
                .build();
    }

}
