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

  const receiveMessage = (frame) => {
    //3-1 구독한 url에서 온 메세지를 받았을 때
    alert(JSON.stringify(frame.body))

    switch (frame.body.messageType){
      case "TOPIC_ADD" :
        // 누군가 주제를 추가한 메세지를 받았을 때
        break;
      case "TOPIC_DELETE" :
        // 누군가 주제를 삭제한 메세지를 받았을 때
        break;
      case "VOTE_ADD" :
        // 누군가 투표한 메세지를 받았을 때
        break;
      case "VOTE_DELETE" :
        // 누군가 투표를 취소한 메세지를 받았을 때
        break;
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

  // 주제 추가 함수
  const addTopic = () => {
    const newTopic = {roomId, title: newTopic,};
    setTopics([...topics, newTopic]); // 새 주제 추가
    stompClient.current.publish(`/pub/topics/add`);
    setNewTopic(""); // 입력 초기화
  };

  const deleteTopic = (topic_Id) => {
    const updatedTopic = topics.filter((topic) => topic.topicId !== topic_Id);
    setTopics(updatedTopic);
    stompClient.current.publish(`/pub/topics/delete`);
  }

  const addVote = () => {
    stompClient.current.publish(`/pub/votes/add`);
  }

  const deleteVote = () => {
    stompClient.current.publish(`/pub/votes/delete`);
  }

  const testWebsocket = () => {
    alert("웹소켓 테스트")
    stompClient.current.publish({
      destination: "/pub/topics/add", // 발행할 경로
      body: JSON.stringify({ roomId: roomId, topic : "웹소켓 테스트 주제"}), // 메시지 내용
    });
  }

  const ClickLike = () => {
    const [isClicked, setIsClicked] = useState(false);
    const [changeNum, setChangeNum] = useState(0); //버튼 클릭 시 숫자가 변경되는 함수

    const changeLike = useCallback(() => {
      //버튼을 클릭할 때 마다 현재의 반대 상태로 변경
      setIsClicked(!isClicked);
      setChangeNum((prev) => (isClicked ? prev - 1 : prev + 1)); // 좋아요 수 조정

      if (!isClicked)
        addVote();
      else
        deleteVote();

    }, [isClicked]);

    return (
        <div className="like-container">
          <button className="like-button" onClick={changeLike}>
            <LikeImage likeClick={isClicked}/>
          </button>
          <span className="like-count">{changeNum}</span>
        </div>
    );
  };

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
            <div className="topics_container">
              {topics.num > 0 ? (
                  topics.topics.map((topic) => (
                      <div className="topics_content" key={topic.topicId}>
                          <button onClick={ClickLike}>
                          <h3>{topic.title}</h3>
                        </button>
                        <button onClick={()=>deleteTopic(topic.topicId)}>
                          X
                        </button>
                      </div>
                  ))
              ) : <h2>해당 방의 주제가 없습니다.</h2>}
            </div>
            <div>
              <button onClick={() => testWebsocket()}> 웹소켓 테스트 버튼</button>
            </div>
            <div>
              <button onClick={()=>setAddModal(true)} className="add_topic">
                주제 추가
              </button>
            </div>
          </div>

          {addModal && (
              <div className="modal_section">
                <label className="modal_label">주제 추가</label>
                <input
                    className="modal_input"
                    type="Topic_Add"
                    value={newTopic}
                />
                <button
                    className="add_button"
                    onClick={addTopic}
                />
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