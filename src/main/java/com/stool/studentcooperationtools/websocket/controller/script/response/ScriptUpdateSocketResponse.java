package com.stool.studentcooperationtools.websocket.controller.script.response;

import com.stool.studentcooperationtools.domain.script.Script;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScriptUpdateSocketResponse {

    @NotNull
    private Long scriptId;

    private String script;

    @Builder
    public ScriptUpdateSocketResponse(Long scriptId, String script) {
        this.scriptId = scriptId;
        this.script = script;
    }

    public static ScriptUpdateSocketResponse of(Script script){
        return ScriptUpdateSocketResponse.builder()
                .script(script.getScript())
                .scriptId(script.getId())
                .build();
    }
}
