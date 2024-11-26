import React, { useEffect, useRef, useState, useCallback } from 'react';
import { useLocation } from 'react-router-dom';// 방 id를 받아오기 위해 선언한 import
import axios from "axios";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import { Link } from 'react-router-dom';
import './topic.css';
import ChatPage from "./chat.tsx";
import chatImage from './images/chat.svg';
import likeImage from './images/like.svg';
import {domain} from "./domain";

const Topic = () => {
  const [topics, setTopics] = useState({num: 0, topics: []});
  const [newTopic, setNewTopic] = useState("");
  const [error, setError] = useState(null);
  const [addModal, setAddModal] = useState(false);
  const [chatModal, setChatModal] = useState(false);
  const [isSubscribed, setIsSubscribed] = useState(true); // TopicList를 그렸는지 여부
  const [isConnected, setIsConnected] = useState(false); // 웹소켓 연결 상태 관리
  const location = useLocation(); // 방 id를 받아오기 위해 선언한 hook
  const { roomId } = location.state || {}; //방 id를 받아온다.
  const stompClient = useRef(null); //

  // 방의 주제를 가져오는 함수
  const TopicsList = () => {
      axios.get(`${domain}/api/v1/rooms/${roomId}/topics`)
          .then((res) => {
            setTopics(res.data.data);
          })
          .catch(() => {
            alert('주제를 불러오는데 실패 했습니다.');
          });
  }

  const receiveMessage = (message) => {
    //3-1 구독한 url에서 온 메세지를 받았을 때
    const frame = JSON.parse(message.body)

    if(frame.messageType === "TOPIC_ADD"){
      updateTopicInScreen(frame.data)
    } else if(frame.messageType === "TOPIC_DELETE"){
      deleteTopicInScreen(frame.data)
    } else if(frame.messageType === "VOTE_ADD"){

    } else if(frame.messageType === "VOTE_DELETE"){

    } else{
      console.log("Not Supported Message Type")
    }
  }

  const receiveError = (error) => {
    //3-2 구독한 url에서 온 메세지를 못 받아 에러가 발생했을 때
    alert("방에 입장에 실패하였습니다.");
    setIsSubscribed(false)
    console.error("STOMP Error", error);
    window.location.href = "/";
  }

  const onConnect = () => {
    //2-1 연결 성공의 경우
    TopicsList()
    setIsConnected(true)
    stompClient.current.subscribe(`/sub/rooms/${roomId}/topics`, receiveMessage, receiveError);
  }

  const onStompError = (error) => {
    //2-2 연결 실패의 경우
    alert("방에 입장에 실패하였습니다.");
    console.error("STOMP Error", error);
    window.location.href = "/";
  }

  useEffect(() => {
    //1. WebSocket 클라이언트 초기화 및 broker endPoint에 연결, WebsocketConfig에 설정한 EndPoint를 말함
    stompClient.current = new Client({
      webSocketFactory: () => new SockJS(`${domain}/ws-stomp`),
      connectHeaders: {
        SubscribeUrl : `/sub/rooms/${roomId}/topics` // 어디에 구독할 지 헤더에 담아서 보냄
      },
      reconnectDelay: 5000,
      onConnect,
      onStompError,
    });

    stompClient.current.activate();

    // 컴포넌트 언마운트 시 WebSocket 연결 해제
    return () => {
      if (stompClient.current) {
        stompClient.current.deactivate();
      }
    };
  }, [roomId]);
  // ================================================ 토픽 제거 ======================================

  const deleteTopicInScreen = (topic) => {
    setTopics((prevTopics) => ({
      ...prevTopics,
      topics: prevTopics.topics.filter((t) => t.topicId !== topic.topicId), // topicId가 일치하지 않는 토픽만 남김
    }));
  };
  const deleteTopic = (topicId) => {
    alert(topicId + "번 삭제하기")
    const data = {
      roomId : roomId,
      topicId : topicId
    }
    stompClient.current.publish({
      destination: '/pub/topics/delete',
      body: JSON.stringify(data)
    })
  }
  // ================================================ 토픽 제거 ======================================
  // ================================================ 토픽 생성 ======================================

  const updateTopicInScreen = (topic) => {
    setTopics((prevTopics) => ({
      ...prevTopics,
      num: prevTopics.num + 1, // 주제 개수 증가
      topics: [...prevTopics.topics, topic], // 기존 토픽 배열에 새 토픽 추가
    }));
  };

  // 주제 추가 함수
  const addTopic = () => {
    setAddModal(false)
    const topic = document.getElementById("topicTitleInput").value;
    const data = {
        topic : topic,
        roomId : roomId
    }
    stompClient.current.publish({
        destination: '/pub/topics/add',
        body: JSON.stringify(data)
    })
  };

  // ================================================ 토픽 생성 ======================================
  // ================================================ 투표 생성 ======================================


  const addVote = () => {
    stompClient.current.publish(`/pub/votes/add`);
  }


  const testWebsocket = () => {
    alert("웹소켓 테스트")
    stompClient.current.publish({
      destination: "/pub/topics/add", // 발행할 경로
      body: JSON.stringify({ roomId: roomId, topic : "웹소켓 테스트 주제"}), // 메시지 내용
    });
  }

  //LikeImage : 버튼 클릭 시 이미지가 변경되는 컴포넌트
  const LikeImage = ({likeClick}) => {
    return likeClick ? (
        <img src={likeImage} width={24} height={24} fill="red"/>
    ) : (
        <img src={likeImage} width={24} height={24} fill="gray"/>
    );
  };

  if (!isConnected) {
    // 연결 중인 상태일 때는 로딩 상태로
    return   <div className="loading">
                <div className="loading-container">
                  <div className="spinner"></div>
                  <p>로딩 중...</p>
                </div>
             </div>;
  }

  return (
      <>
        <div>
          <Link to={"/project"} className="back_link">
            뒤로 가기
          </Link>
        </div>

        <div className="background">
          <div className="topics_overlay">
            <div className="card-container" id="topicsDiv">
              {topics.num > 0 ? (
                  topics.topics.map((topic) => (
                      <div className="card" id={ "topic" + topic.topicId}>
                        <button className="card-button" onClick={() => deleteTopic(topic.topicId)}>
                          X
                        </button>
                        <h3 className="card-title">
                          {topic.title}
                        </h3>
                        <span className="card-text">좋아요 : 3</span>
                        <span className="card-text">싫어요 : 3</span>
                      </div>
                  ))
              ) : <h2 id="notExsistTopicH">
                    해당 방의 주제가 없습니다.
                  </h2>
              }
            </div>
            <div>
              <button onClick={()=>setAddModal(true)} className="add_topic">
                주제 추가
              </button>
            </div>
          </div>

          {addModal && (
              <div className="modal">
                <div className="modal_overlay">
                  <div className="modal_content">
                    <label className="modal_label">주제 추가</label>
                    <input className="modal_input" id="topicTitleInput" type="text"/>
                    <button onClick={() => addTopic()}>
                      등록하기
                    </button>
                  </div>
                </div>
              </div>
          )}

          <button>
            <img className="chat_image" onClick={() => setChatModal(true)} src={chatImage} alt="채팅창 이미지"/>
          </button>

          {chatModal && (
              <div className="chat-overlay">
                <div className="chat-content">
                  <ChatPage/>
                  <button className="chat-close-button" onClick={() => setChatModal(false)}> X</button>
                </div>
              </div>
          )}

          <div className="process">
            <div>주제 선정</div>
            <div>자료 조사</div>
            <div>발표 자료</div>
            <div>발표 준비</div>
          </div>
        </div>

      </>
  );
}

export default Topic;