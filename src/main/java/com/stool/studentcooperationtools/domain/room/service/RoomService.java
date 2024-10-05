package com.stool.studentcooperationtools.domain.room.service;

import com.stool.studentcooperationtools.domain.room.controller.request.RoomAddRequest;
import com.stool.studentcooperationtools.domain.room.controller.request.RoomPasswordValidRequest;
import com.stool.studentcooperationtools.domain.room.controller.request.RoomRemoveRequest;
import com.stool.studentcooperationtools.domain.room.controller.response.RoomAddResponse;
import com.stool.studentcooperationtools.domain.room.controller.response.RoomSearchResponse;
import com.stool.studentcooperationtools.domain.room.controller.response.RoomsFindResponse;
import org.springframework.stereotype.Service;

@Service
public class RoomService {
    public RoomsFindResponse findRooms(final int page) {
        return null;
    }

    public RoomAddResponse addRoom(final RoomAddRequest request) {
        return null;

    }

    public RoomSearchResponse searchRoom(final String title, final int page) {
        return null;
    }

    public Boolean removeRoom(final RoomRemoveRequest request) {
        return null;
    }

    public Boolean validRoomPassword(final RoomPasswordValidRequest request) {
        return null;
    }
}
