package com.stool.studentcooperationtools.domain.room.controller.response;

import com.stool.studentcooperationtools.domain.room.Room;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

import static com.stool.studentcooperationtools.domain.PagingUtils.getEndPage;
import static com.stool.studentcooperationtools.domain.PagingUtils.getRoomPagingStartPage;

@Getter
public class RoomsFindResponse {

    private long num;
    private int totalPage;
    private int firstPage;
    private int lastPage;
    private List<RoomFindDto> rooms;

    @Builder
    private RoomsFindResponse(final long num, final int totalPage, final int firstPage, final int lastPage, final List<RoomFindDto> rooms) {
        this.num = num;
        this.firstPage = firstPage;
        this.lastPage = lastPage;
        this.totalPage = totalPage;
        this.rooms = rooms;
    }

    public static RoomsFindResponse of(final long num,final int nowPage, final int totalPage, final List<Room> rooms){
        int roomPagingStartPage = getRoomPagingStartPage(nowPage);
        return RoomsFindResponse.builder()
                .num(num)
                .firstPage(roomPagingStartPage)
                .lastPage(getEndPage(roomPagingStartPage,totalPage))
                .totalPage(totalPage)
                .rooms(
                        rooms.stream()
                                .map(RoomFindDto::of)
                                .toList()
                )
                .build();
    }
}
