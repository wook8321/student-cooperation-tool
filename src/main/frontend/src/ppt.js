
import React, {useEffect, useRef, useState} from 'react';
import axios from 'axios';
import { useNavigate } from "react-router-dom";
import { useWebSocket } from './WebsocketContext'; // WebSocketProvider의 훅 사용
import './ppt.css';
import ChatPage from "./chatroom";
import chatImage from './images/chat.svg';
import EditImage from './images/edit.svg';
import pptImage from './images/ppt.svg';
import {domain} from "./domain";
import emptyBox from "./images/emptyBox.svg";

const PPT = () => {
  const [pptModal, setPPTModal] = useState(false); // ppt 생성 클릭 시 나오는 모달
  const [pptData, setPPTData] = useState(null); // get한 ppt 데이터 저장 공간
  const [editMode, setEditMode] = useState(false); // 처음 생성인 지, 이후 수정인 지 구별
  const [chatModal, setChatModal] = useState(false);
  const {stompClient, isConnected, roomId, userId, leaderId} = useWebSocket();
  const subscriptions = useRef([]); // 구독후 반환하는 객체로, 해당 객체로 구독을 취소해야 한다.
  const navigate = useNavigate();
  const pptThumbURL = 'https://drive.google.com/thumbnail?authuser=0&sz=w320&id='
  const [newPPTName, setNewPPTName] = useState(' ');
  const [isLoading, setIsLoading] = useState(false);
  const isLeader = (userId === leaderId);
  // 방의 PPT를 가져오는 함수
  const fetchPPT = () => {
      console.log(userId, leaderId);
      axios.get(`${domain}/api/v1/rooms/${roomId}/presentation`)
          .then((res) => {
            setPPTData(res.data.data);
            })
          .catch((error) => {
                if(error.response){
                    if(error.response.status === 400){
                        setPPTData(null);
                    }
                }
            });
  }
  //=============================================웹소켓========================================================
  const receiveMessage = (message) => {
      //3-1 구독한 url에서 온 메세지를 받았을 때
      const frame = JSON.parse(message.body)
      if (frame.messageType === "PRESENTATION_UPDATE") {
          //updatePPTInScreen(frame.data)
      } else if (frame.messageType === "PRESENTATION_CREATE") {
          setIsLoading(false);
          setPPTModal(false);
          createPPTInScreen(frame.data)
      } else {
          console.log("Not Supported Message Type")
      }
  }

    const receiveError = (error) => {
        //3-2 구독한 url에서 온 메세지를 못 받아 에러가 발생했을 때
        console.error("STOMP Error", error);
        window.location.href = "/";
    }

    const onConnect = () => {
        //2-1 연결 성공의 경우
        fetchPPT();
        subscriptions.current = stompClient.current.subscribe(
            `/sub/rooms/${roomId}/presentation`,
            receiveMessage,
            receiveError
        );
    }

    useEffect(() => {
        //1. broker endPoint에 연결, WebsocketConfig에 설정한 EndPoint를 말함
        if (stompClient.current) {
            stompClient.current.activate(); // 웹소켓 활성화
        }

        return () => {
            if (stompClient.current) {
                subscriptions.current.unsubscribe();
            }
        };
    }, [roomId]);

    useEffect(() => {
        if (isConnected) {
            onConnect(); // 연결이 완료되면 onConnect 호출
        }
    }, [isConnected]); //isConnected 상태가 바뀌면 실행된다.
    //===============================================================================
    //======================================PPT 생성 ====================================
    // PPT 생성 클릭
  const createPPT =  () => {
    setIsLoading(true);
    const data = {
        roomId,
        presentationName: newPPTName
    }
        stompClient.current.publish({
            destination: '/pub/presentation/create',
            body: JSON.stringify(data)
        });
    };

  // 생성된 PPT 반영
  const createPPTInScreen = (frame) => {
      console.log('new PPT : ', frame);
      setPPTData(frame);
  }
  //==================================PPT 업데이트========================================
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
  //================================PPT 다운로드===================================
    const downloadPDF = () => {

    }

    const downloadPPT = () => {

    }

  //뒤로가기
  const goBack = () => {
    navigate("/project"); // "/project" 경로로 이동
  };

  //채팅창 토글로 구현
  const toggleChatModal = () => {
        setChatModal((prevState) => !prevState);
  };

  useEffect(() => {
      fetchPPT();
  }, []);

    useEffect(() => {
        if(pptData){
           setEditMode(true);
        }
        else{
            setEditMode(false);
        }
    }, [pptData]);

  return (
      <>
      <div className="background">
          <button onClick={goBack} className="back_link">
              뒤로 가기
          </button>
          <div className="ppt-container">
              {/* 발표자료가 없을 때 */}
              {!pptData ? (
                  isLeader ? (
                      <div className="no-ppt-container">
                          <p className="no-ppt-text">새로운 발표자료를 생성해 보세요!</p>
                          <button className="create-ppt-btn" onClick={() => setPPTModal(true)}>
                              생성
                          </button>
                      </div>
                  ) : (
                      <div className="no-ppt-view">
                          <img src={pptImage} height="300" width="300"/>
                          <p className="no-ppt-text">아직 발표자료가 등록되지 않았어요.</p>
                      </div>
                  )
              ) : (
                  <div className="ppt-content">
                      {/* 썸네일 */}
                      <div className="ppt-thumbnail-container">
                          <img
                              src={`${pptThumbURL}${pptData.presentationId}`}
                              alt="PPT 썸네일"
                              className="ppt-thumbnail"
                          />
                      </div>

                      {/* 오른쪽 버튼 영역 */}
                      <div className="ppt-actions">
                          <div className="download-buttons">
                              <button onClick={() => downloadPDF()} className="download-pdf-btn">
                                  PDF로 다운하기
                              </button>
                              <button onClick={() => downloadPPT()} className="download-ppt-btn">
                                  PPT로 다운하기
                              </button>
                          </div>
                          {isLeader && (
                              <button className="edit-ppt-btn" onClick={() => editPPT()}>
                                  수정
                              </button>
                          )}
                      </div>
                  </div>
              )}

          {pptModal && (
              <div className="ppt-modal">
                  <h2>{editMode ? 'PPT 수정' : 'PPT 등록'}</h2>
                  <p>생성할 ppt 제목을 아래에 작성해주세요.</p>
                  <input
                      type="text"
                      value={newPPTName}
                      onChange={(e) => setNewPPTName(e.target.value)}
                  />
                  <div className="modal-buttons">
                      <button className="close-modal-btn" onClick={() => setPPTModal(false)}>
                          닫기
                      </button>
                      <div>
                          {isLoading && (
                              <div className="loading-overlay">
                                  <div className="spinner"></div>
                                  <p>Loading...</p>
                              </div>
                          )}
                      </div>
                      <button className="register-btn" onClick={createPPT} disabled={isLoading}>
                          {isLoading ? '생성 중...' : '생성'}
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

          <div>
              <button className="chat-button" onClick={toggleChatModal}>
                  <img className="chat_image" src={chatImage} alt="채팅창 이미지"/>
              </button>
              <div className={`chat-modal ${chatModal ? 'open' : ''}`}>
                  {chatModal && <ChatPage/>}
              </div>
          </div>
      </div>
      </div>
</>
  );
};

export default PPT;
