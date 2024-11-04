package com.stool.studentcooperationtools.domain.script.service;

import com.stool.studentcooperationtools.domain.script.Script;
import com.stool.studentcooperationtools.domain.script.repository.ScriptRepository;
import com.stool.studentcooperationtools.websocket.controller.script.request.ScriptUpdateSocketRequest;
import com.stool.studentcooperationtools.websocket.controller.script.response.ScriptUpdateSocketResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScriptService {
    private final ScriptRepository scriptRepository;

    @Transactional
    public ScriptUpdateSocketResponse updateScript(ScriptUpdateSocketRequest request) {
        Script script = scriptRepository.findById(request.getScriptId())
                .orElseThrow(()->new IllegalArgumentException("script가 존재하지 않습니다"));
        script.updateScript(request.getScript());
        return ScriptUpdateSocketResponse.of(script);
    }
}
