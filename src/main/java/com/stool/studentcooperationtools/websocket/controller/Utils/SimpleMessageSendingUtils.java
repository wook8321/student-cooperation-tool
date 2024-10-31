package com.stool.studentcooperationtools.websocket.controller.Utils;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import static com.stool.studentcooperationtools.websocket.config.WebsocketConfig.TOPIC_DECISION_URL_FORMAT;

/*
주제 선정, 자료 조사, 발표 관리 단계 sub url을 생성, 메세지 전송 기능을 분리시키기 위해 만들었다.
 */
@Component
@RequiredArgsConstructor
public class SimpleMessageSendingUtils {

    private final SimpMessageSendingOperations simpMessageSendingOperations;

    /*
    주제 선정단계의 url을 이곳에서 생성하는 메소드이다
     */
    public String createTopicDecisionSubUrl(final Long roomId){
        return String.format(TOPIC_DECISION_URL_FORMAT, roomId);
    }

    /*
    simpMessageSendingOperations를 이용해서 subUrl로 response를 보낸다.
     */
    public void convertAndSend(final String subUrl, final Object response) {
        simpMessageSendingOperations.convertAndSend(subUrl, response);
    }

}
