package com.stool.studentcooperationtools.websocket.controller.Utils;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import static com.stool.studentcooperationtools.websocket.config.WebsocketConfig.*;
import static com.stool.studentcooperationtools.websocket.config.WebsocketConfig.PRESENTATION_MANAGE_URL_FORMAT;
import static com.stool.studentcooperationtools.websocket.config.WebsocketConfig.CHAT_ROOM_URL_FORMAT;
import static com.stool.studentcooperationtools.websocket.config.WebsocketConfig.TOPIC_DECISION_URL_FORMAT;

/*
주제 선정, 자료 조사, 발표 관리 단계 sub url을 생성, 메세지 전송 기능을 분리시키기 위해 만들었다.
 */
@Component
@RequiredArgsConstructor
public class SimpleMessageSendingUtils {

    private final SimpMessageSendingOperations simpMessageSendingOperations;

    /*
        프로젝트 방에 입장한 유저들이 구독하는 url을 생성하는 메소드이다.
    */
    public String creatRoomEnterSubUrl(final Long roomId) {
        return String.format(ROOM_PARTICIPATION_URL_FORMAT,roomId);
    }

    /*
    주제 선정단계의 sub url을 생성하는 메소드이다
     */
    public String createTopicDecisionSubUrl(final Long roomId){
        return String.format(TOPIC_DECISION_URL_FORMAT, roomId);
    }

    /*
    발표자료 관리 단계의 url을 이곳에서 생성하는 메소드이다
     */
    public String createPresentationManageSubUrl(final Long roomId){
        return String.format(PRESENTATION_MANAGE_URL_FORMAT, roomId);
    }
  
    /*
    채팅방 sub url을 생성하는 메소드이다.
     */
    public String createChatRoomSubUrl(final Long roomId){
        return String.format(CHAT_ROOM_URL_FORMAT,roomId);
    }

    /*
    자료 역할 sub url을 생성하는 메소드이다.
     */
    public String creatPartResearchSubUrl(final Long roomId){
        return String.format(PART_RESEARCH_URL_FORMAT,roomId);
    }

    /*
    스크립트 관리 sub url을 생성하는 메소드이다.
    */
    public String createScriptManageSubUrl(final Long roomId){
        return String.format(SCRIPT_MANAGE_URL_FORMAT,roomId);
    }

    /*
    simpMessageSendingOperations를 이용해서 subUrl로 response를 보낸다.
     */
    public void convertAndSend(final String subUrl, final Object response) {
        simpMessageSendingOperations.convertAndSend(subUrl, response);
    }

}
