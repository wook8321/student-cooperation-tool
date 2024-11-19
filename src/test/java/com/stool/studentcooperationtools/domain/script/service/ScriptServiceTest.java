package com.stool.studentcooperationtools.domain.script.service;

import com.stool.studentcooperationtools.IntegrationTest;
import com.stool.studentcooperationtools.domain.script.Script;
import com.stool.studentcooperationtools.domain.script.repository.ScriptRepository;
import com.stool.studentcooperationtools.websocket.controller.script.request.ScriptUpdateSocketRequest;
import com.stool.studentcooperationtools.websocket.controller.script.response.ScriptUpdateSocketResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional(readOnly = true)
class ScriptServiceTest extends IntegrationTest {

    @Autowired
    private ScriptService scriptService;
    @Autowired
    private ScriptRepository scriptRepository;

    @Transactional
    @Test
    @DisplayName("스크립트의 내용 업데이트")
    void updateScript() {
        //given
        Script script = Script.builder()
                .script("")
                .build();
        scriptRepository.save(script);
        ScriptUpdateSocketRequest request = ScriptUpdateSocketRequest.builder()
                .roomId(1L)
                .scriptId(script.getId())
                .script("new")
                .build();
        //when
        ScriptUpdateSocketResponse response = scriptService.updateScript(request);
        //then
        assertEquals("new", response.getScript());
    }

    @Test
    @DisplayName("유효하지 않은 스크립트에 접근 시 에러 발생")
    void updateInvalidScript() {
        //given
        Long invalidScriptId = 1L;
        ScriptUpdateSocketRequest request = ScriptUpdateSocketRequest.builder()
                .scriptId(invalidScriptId)
                .script("new")
                .build();
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> scriptService.updateScript(request));
    }
}