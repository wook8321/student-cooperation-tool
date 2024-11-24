import React, { useEffect, useState, useCallback } from 'react';
import axios from "axios";
import { Client } from "@stomp/stompjs";
import { Link } from 'react-router-dom';
import './topic.css';
import ChatPage from "./chat.tsx";
import chatImage from './images/chat.svg';
import likeImage from './images/like.svg';
import {useId} from 'react'; 

const domain = "http://localhost:8080"

const Topic = ({ roomId }) => {
  const [topics, setTopics] = useState({num: 0, topics:[]});

  const [newTopic, setNewTopic] = useState("");
  const [userId, setUserID] = useState("");

  const [vote_num, setVoteNum] = useState(0);
  const [decidedTopic, setDecidedTopic] = useState("");

  const [error, setError] = useState(null);
  const [addModal, setAddModal] = useState(false);
  const [chatModal, setChatModal] = useState(false);
  const [decideModal, setDecideModal] = useState(false);

  // 방의 주제를 가져오는 함수
  const TopicsList = ({ roomId }) => {
    useEffect( () => {
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

  stompClient.activate(); // 웹소켓 활성화
  userId = useId(); // 고유한 유저 아이디 생성

  // 주제 추가 함수
  const addTopic = (newTopic) => {
    if (!newTopic.trim()) return; // 빈 값은 제외

    topics.num += 1;

    setTopics((prev) => {
      const newTopicObject = {
        topicId: prev.num + 1,
        memberId: userId,
        topic: newTopic,
        voteNum: 0,
        votes: [],
      };
  
      const updatedTopics = {
        num: prev.num + 1,
        topics: [...prev.topics, newTopicObject],
      };

      stompClient.publish({
        destination : `${domain}/pub/topics/add`,
        body: topics
      });

      return updatedTopics;
    });

    setNewTopic(""); // 입력 초기화
  };

  const deleteTopic = (topic_id) => {
    const updatedTopic = topics.topics.filter((topic) => topic.topicId !== topic_id);

    setTopics(updatedTopic);

    stompClient.publish({
      destination : `${domain}/pub/topics/delete`,
      body : topics
    });
  };

  const addVote = (topic_id) => {
    setTopics((prev) => {
      const updatedTopics = prev.topics.map((topic) => {
        if (topic.topicId === topic_id) {
          return {
            ...topic,
            voteNum: topic.voteNum + 1,
            votes: [...topic.votes, { memberId: userId, voteId: topic.voteNum + 1 }],
          };
        }
        return topic;
      });

      const updatedState = { ...prev, topics: updatedTopics };

      stompClient.publish({
        destination : `${domain}/pub/votes/add`,
        body : topics
      });

      return updatedState;
    });
  }

  const deleteVote = (topic_id) => {
    setTopics((prev) => {
      const updatedTopics = prev.topics.map((topic) => {
        if (topic.topicId === topic_id) {
          return {
            ...topic,
            voteNum: topic.voteNum - 1,
            votes: topic.votes,  // 사용자 id와 비교해서 같은 것만 삭제해야함.
          };
        }
        return topic;
      });

      const updatedState = { ...prev, topics: updatedTopics };

      stompClient.publish({
        destination : `${domain}/pub/votes/delete`,
        body : topics 
      });

      return updatedState;
    })
  }

  if (error) {
    return <p>{error}</p>;
  }

  const ClickLike = (topic_id) => {
    const [isClicked, setIsClicked] = useState(false);

    const changeLike = useCallback(() => {
        //버튼을 클릭할 때 마다 현재의 반대 상태로 변경
        setIsClicked(!isClicked);

        if(!isClicked)
          addVote(topic_id);
        else
          deleteVote(topic_id);

    },[isClicked]);

    /*
      if (userId == topics.topics.memberId)
        setDecideModal(true);
        setDecidedTopic(filteredTopic.topics.topic);
    */

    return (
    	<div className="like-container">
        <button className="like-button" onClick={() => changeLike}>
          <LikeImage likeClick={isClicked} />
        </button>
        <span className="like-count">{vote_num}</span>
      </div>
    );
  };

  //LikeImage : 버튼 클릭 시 이미지가 변경되는 컴포넌트
  const LikeImage = ({likeClick}) => { 
    return likeClick ? (
        <img src={likeImage} width={24} height={24} fill="red" />
    ) : (
        <img src={likeImage} width={24} height={24} fill="gray" />
    );
  };

  const cancel = () => {
    <div>
      <Link to={"/topic"}>

      </Link>
    </div>
  }

  const check = () => {
    <div>
      <Link to={"/part"}>

      </Link>
    </div>
  }

  const closeAddModal = (e) => {
    if(e.target.id === "add-modal-overlay")
      setAddModal(false);
  }

  const closeChatModal = (e) => {
    if(e.target.id === "chat-modal-overlay")
      setChatModal(false);
  }

return (
  <>
    <div>
      <Link to={"/project"} className="back_link">
        뒤로 가기
      </Link>
    </div>

    <div className="background">
      <div className="topics_overlay" >
        <div className="topics_container">
          <TopicsList />
          {topics.map((topics) => (
            <div className="topics_content" key={topics.topicId}>
              <button onClick={() => ClickLike(topics.topics.topicId)}>
                <h3>{topics.title}</h3>
                <button onClick={() => deleteTopic(topics.topics.topicId)}>
                  X  
                </button>
              </button>
            </div>
          ))}
        </div>

        <div>
          <button onClick={() => setAddModal(true)} className="add_topic">
            주제 추가
          </button>
        </div>
      </div>

      {addModal && (
        <div id = "add-modal-overlay" className="modal-overlay">
          <label className="add_main_label">주제 추가</label>
          <button className="add_close_button" onClick={() => closeAddModal}> X </button>
          <form className="search_box" onSubmit={(e) => e.preventDefault()}>
            <label className="add_name_label">주제 이름</label>
              <input
                className="modal_input"
                type="Topic_Add"
                placeholder="주제 이름을 입력하세요."
                value={newTopic}
                onChange={(e) => setNewTopic(e.target.value)}
              />
            <button 
              className="add_button"
              onClick={() => addTopic(newTopic)}
            >
              추가
            </button>
          </form>
        </div>                
      )}

      <button>
        <img className="chat_image" onClick={() => setChatModal(true)} src={chatImage} alt="채팅창 이미지"/>
      </button>

      {chatModal && (
        <div id="chat-modal-overlay" className="chat-overlay">
            <div className="chat-content">
              <ChatPage />
              <button className="chat-close-button" onClick={() => closeChatModal}> X</button>
          </div>
        </div>
      )}

      {decideModal && (
        <div className="decide-overlay">
          <div className="decide-content">
            주제를 결정하시겠습니까?
            <label className="decide-label">{decidedTopic}</label>
            <button className="check-button" onClick={check}> 확인 </button>
            <button className="cancel-button" onClick={cancel}> 취소 </button>
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