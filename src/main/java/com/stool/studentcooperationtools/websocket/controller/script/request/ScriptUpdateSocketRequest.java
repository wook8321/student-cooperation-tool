package com.stool.studentcooperationtools.websocket.controller.script.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScriptUpdateSocketRequest {

    @NotNull
    private Long roomId;

    @NotNull
    private Long scriptId;

    private String script;

    @Builder
    public ScriptUpdateSocketRequest(Long roomId, Long scriptId, String script) {
        this.roomId = roomId;
        this.scriptId = scriptId;
        this.script = script;
    }

}
