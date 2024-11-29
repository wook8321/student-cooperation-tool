
import React, { useState } from 'react';
import axios from 'axios';
import { Client } from "@stomp/stompjs";
import { useNavigate } from "react-router-dom";
import './ppt.css';
import ChatPage from "./chatroom";
import chatImage from './images/chat.svg';
import EditImage from './images/edit.svg';

const domain = "http://localhost:8080"

const PPT = ({ roomId }) => {
  const [pptModal, setPPTModal] = useState(false); // ppt 생성 클릭 시 나오는 모달
  const [pptData, setPPTData] = useState({presentationId: null, presentationPath: '', updatedTime: null, }); // get한 ppt 데이터 저장 공간
  const [editMode, setEditMode] = useState(false); // 처음 생성인 지, 이후 수정인 지 구별
  const [chatModal, setChatModal] = useState(false); 

  const navigate = useNavigate();

  // PPT 생성 클릭
  const createPPT = () => {
    // Google Slides 새 창 열기
    window.open('https://docs.google.com/presentation', '_blank');
    setPPTModal(true);
  };

  const closePPT = () => {
    setPPTModal(false);
  };

  const stompClient = new Client({
    brokerURL: `ws://sub/rooms/${roomId}/presentations`, // 역할 추가 단계 WebSocket
    reconnectDelay: 5000,
    onConnect: () => {
        stompClient.subscribe(`${domain}/sub/rooms/${roomId}/presentations`);
    },
    onStompError: (frame) => {
        console.error("STOMP error: ", frame.headers["message"]);
    },
  });

  stompClient.activate();

  /* 등록 버튼 클릭 */
  const registerPPT = () => {
      axios.get(`${domain}/api/rooms/${roomId}/presentation`)
        .then((res) => {
            const { presentationId, presentationPath, updatedTime } = res.data.data;
              setPPTData({
                  presentationId,
                  presentationPath,
                  updatedTime,
              });
            setPPTModal(false);
        })
        .catch((error) => {
            console.error('PPT 등록 실패:', error);
            });
        };

  // 수정 버튼 클릭 시
  const editPPT = () => {
    if (stompClient && pptData.presentationId) {
      const payload = {
        presentationId: pptData.presentationId,
        presentationPath: pptData.presentationPath,
      };

      stompClient.publish({
        destination: "/pub/presentations/update",
        body: payload,
      });

    setEditMode(true);
    setPPTModal(true);
  }};

  const goBack = () => {
    navigate("/project"); // "/project" 경로로 이동
  };

  return (
    <>
      <button onClick={goBack} className="back_link">
        뒤로 가기
      </button>
      <div className="PPT">
        
        {!pptData.presentationPath ? (
          // PPT 경로가 없으면 생성 버튼
          <button className="create-ppt-btn" onClick={() =>createPPT()}>
            PPT 생성
          </button>
        ) : (
          <div className="ppt-thumbnail-container">
            <img
              src={`https://drive.google.com/thumbnail?authuser=0&sz=w320&id=${pptData.presentationId}`}
              alt="PPT 썸네일"
              className="ppt-thumbnail"
              onClick={() => navigate("/slide")}
            />
            
          <button className="edit-ppt-btn">
              <img className="edit_image" onClick={() => editPPT()} src={EditImage} alt="수정버튼 이미지"/>
          </button>
          </div>
        )}

        {pptModal && (
          <div className="ppt-modal">
            <h2>{editMode ? 'PPT 수정' : 'PPT 등록'}</h2>
            <p>생성할 ppt URL을 아래에 작성해주세요.</p>
            <input
              type="text"
              placeholder="ppt URL을 작성해주세요."
              value={pptData.presentationPath}
              onChange={(e) => setPPTData({ ...pptData, presentationPath: e.target.value })}
            />
            <div className="modal-buttons">
              <button className="close-modal-btn" onClick={closePPT}>
                닫기
              </button>
              <button className="register-btn" onClick={registerPPT}>
                등록
              </button>
            </div>
          </div>
        )}

          <div className="process-container">
            <div className="process-step">
              <div className="process-text">주제 선정</div>
            </div>
            <div className="process-step">
              <div className="process-text">자료 조사</div>
            </div>
            <div className="process-step">
              <div className="process-text">발표 자료</div>
            </div>
            <div className="process-step">
              <div className="process-text">발표 준비</div>
            </div>
          </div>

          <img className="chat_image" onClick={() => setChatModal(true)} src={chatImage} alt="채팅창 이미지"/>

          {chatModal && (
              <div className="chat-overlay">
                  <div className="chat-content">
                      <ChatPage />
                      <button className="chat-close-button" onClick={() => setChatModal(false)}> X</button>
                  </div>
              </div>
          )}
      </div>
    </>
  );
};

export default PPT;
