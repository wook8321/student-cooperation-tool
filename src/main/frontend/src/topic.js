import React, { useEffect, useState, useCallback } from 'react';
import axios from "axios";
import { Client } from "@stomp/stompjs";
import { Link } from 'react-router-dom';
import './topic.css';
import ChatPage from "./chat.tsx";
import chatImage from './images/chat.svg';
import likeImage from './images/like.svg';

const domain = "http://localhost:8080"

const Topic = ({ roomId }) => {
  const [topics, setTopics] = useState({num: 0, topics: []});
  const [newTopic, setNewTopic] = useState("");
  const [error, setError] = useState(null);
  const [addModal, setAddModal] = useState(false);
  const [chatModal, setChatModal] = useState(false);

  // 방의 주제를 가져오는 함수
  const TopicsList = ({roomId}) => {
    useEffect(() => {
      axios.get(`${domain}/api/v1/rooms/${roomId}/topics`)
          .then((res) => {
            setTopics(res.data.data);
          })
          .catch(() => {
            setError('failed to load friends');
          });
    }, []);
  }

  const stompClient = new Client({
    brokerURL: `ws://http://localhost:8080/sub/rooms/${roomId}/topics`, // 주제 선정 단계 WebSocket
    reconnectDelay: 5000,
    onConnect: () => {
      stompClient.subscribe(`${domain}/sub/rooms/${roomId}/topics`);
    },
  });

  // 주제 추가 함수
  const addTopic = () => {
    const newTopic = {roomId, title: newTopic,};
    setTopics([...topics, newTopic]); // 새 주제 추가
    stompClient.publish(`${domain}/pub/topics/add`);
    setNewTopic(""); // 입력 초기화
  };

  const deleteTopic = (topic_Id) => {
    const updatedTopic = topics.filter((topic) => topic.topicId !== topic_Id);
    setTopics(updatedTopic);
    stompClient.publish(`${domain}/pub/topics/delete`);
  }

  const addVote = () => {
    stompClient.publish(`${domain}/pub/votes/add`);
  }

  const deleteVote = () => {
    stompClient.publish(`${domain}/pub/votes/delete`);
  }

  if (error) {
    return <p>{error}</p>;
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
              <TopicsList/>
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