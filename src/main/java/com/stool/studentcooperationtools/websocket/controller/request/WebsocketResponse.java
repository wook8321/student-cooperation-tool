package com.stool.studentcooperationtools.websocket.controller.request;

import com.stool.studentcooperationtools.websocket.WebsocketMessageType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WebsocketResponse <T>{

    private WebsocketMessageType messageType;
    private T data;

    @Builder
    private WebsocketResponse(final WebsocketMessageType messageType, final T data) {
        this.messageType = messageType;
        this.data = data;
    }

    public static <T>WebsocketResponse<T> of(WebsocketMessageType messageType, T data){
        return new WebsocketResponse<>(messageType,data);
    }
}
