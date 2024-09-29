package com.stool.studentcooperationtools.websocket.controller.request;

import lombok.Getter;

@Getter
public class MessageTestRequest {

    private String message;

    public MessageTestRequest(final String message) {
        this.message = message;
    }
}
