package com.stool.studentcooperationtools.domain.room.controller.request;

import jakarta.validation.constraints.NotBlank;
<<<<<<< HEAD
=======
import jakarta.validation.constraints.NotNull;
>>>>>>> daa7c781c6c5fad1d18527e8a96ea8b1d0f2a862
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoomPasswordValidRequest {

<<<<<<< HEAD
=======
    @NotNull
    private Long roomId;
>>>>>>> daa7c781c6c5fad1d18527e8a96ea8b1d0f2a862
    @NotBlank
    private String password;

    @Builder
<<<<<<< HEAD
    private RoomPasswordValidRequest(final String password) {
        this.password = password;
=======
    private RoomPasswordValidRequest(final String password, final Long roomId) {
        this.password = password;
        this.roomId = roomId;
>>>>>>> daa7c781c6c5fad1d18527e8a96ea8b1d0f2a862
    }
}
