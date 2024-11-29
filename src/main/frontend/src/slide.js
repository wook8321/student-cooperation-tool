
import React, { useEffect, useState } from "react";
import axios from "axios";
import "./slide.css";
import { Client } from "@stomp/stompjs";
import { Link } from "react-router-dom";
import ChatPage from "./chat.tsx";
import chatImage from './images/chat.svg';

// 역할 추가 버튼을 누르면 나오는 모달에서 담당자 이름을 입력받고 따로 저장해서 사용하게 만들었어요

const domain = "http://localhost:8080"

const Slide = ({ roomId, presentationId }) => {
  const [slides, setSlides] = useState([]);
  const [selectedSlide, setSelectedSlide] = useState(null); // 모달에 사용할 선택한 슬라이드
  const [newScript, setNewScript] = useState(""); // 새로 입력된 스크립트
  const [scriptModal, setScriptModal] = useState(false); // 스크립트 등록 모달
  const [selectedMember, setSelectedMember] = useState(""); // 담당자 이름 따로 저장
  const [chatModal, setChatModal] = useState(false); 
  const [error, setError] = useState(null);

  const [scripts, setScripts] = useState([]); // 담당자와 스크립트ID 매칭을 위해 따로 저장하는 스크립트 데이터

  const stompClient = new Client({
        brokerURL: `ws://sub/rooms/${roomId}/presentations`, // 발표 자료 단계 WebSocket
        reconnectDelay: 5000,
        onConnect: () => {
            stompClient.subscribe(`${domain}/sub/rooms/${roomId}/presentations`);
        },
        onStompError: (frame) => {
            setError(new Error("STOMP error: ", frame.headers["message"]));
        },
      });

  useEffect(() => {
    if (!presentationId) return;

    stompClient.activate();

    // 슬라이드 데이터
    axios
      .get(`${domain}/api/v1/presentations/${presentationId}/slides`)
      .then((res) => {
        const { slides } = res.data.data;
        setSlides(slides);
      })
      .catch((error) => {
        setError(new Error("슬라이드 데이터 가져오기 실패:", error));
      });
  }, [presentationId]); 

  const addScript = (slide) => {

    if (stompClient) {
      stompClient.publish({
        destination: `${domain}/pub/presentations/update`,
        body: {
          roomId: roomId,
          slideId: selectedSlide.slideId,
          script: newScript,
      },
      });
    
    setSelectedSlide(slide); // 선택한 슬라이드 저장
    setScriptModal(true); // 스크립트 등록 모달 열기
  }};

  const saveScript = () => {
    if (!selectedSlide || !newScript.trim()) return;

    const updatedSlides = slides.map((slide) =>
      slide.slideId === selectedSlide.slideId
        ? { ...slide, script: newScript }
        : slide
    );

    const newScriptObj = {
      scriptId: selectedSlide.scriptId,
      member : selectedMember, // 담당자
    };

    setScripts((prev) => [...prev, newScriptObj]); // 새로운 스크립트 추가
    setSlides(updatedSlides); // 슬라이드 데이터 업데이트
    setScriptModal(false); // 모달 닫기
    setNewScript(""); // 입력값 초기화
    setSelectedMember("");
  }

  const ErrorModal = ({ error, closeErrorModal }) => {
    if (!error) return null; // 에러가 없을 때

    return (
        <div className="error-modal-overlay">
          <h2>오류 발생</h2>
          <button className="close-error-button" onClick={closeErrorModal}>
            X
          </button>
            <div className="error-modal" onClick={(e) => e.stopPropagation()}>
                <p>{error.message || "알 수 없는 에러가 발생했습니다."}</p>
            </div>
        </div>
    );
  };

  const closeErrorModal = () => { setError(null) };

  return (
    <>
      <Link to="/project" className="back_link">
        뒤로 가기
      </Link>

      <ErrorModal error={error} closeErrorModal={closeErrorModal} />

      <div className="slide-view-container">
        <div className="slide-content">
          <div className="slides-preview">
            <h3>슬라이드</h3>
            {slides.length > 0 ? (
              slides.map((slide) => {
                const scriptData = scripts.find(
                  (script) => script.slideId === slide.slideId
                );
                <div key={slide.slideId} className="slide-item">
                  <div className="slide-thumbnail"
                      onClick={() => window.open(slide.slideUrl, "_blank")}
                      style={{ cursor: "pointer" }}>
                    <img
                      src={slide.thumbnailUrl}
                      alt={`Slide ${slide.slideId}`}
                    />
                  </div>
                  <div className="slide-script">
                    <h4>발표 스크립트</h4>
                    {slide.script ? (
                      <div>
                        <p>
                          <strong>담당자:</strong> {scriptData.member || "미지정"}
                        </p>
                        <p>{slide.script}</p>
                      </div>
                    ) : (
                      <button
                        onClick={() => addScript(slide)}
                        className="add-script-btn"
                      >
                        스크립트 추가
                      </button>
                    )}
                  </div>
                </div>
              })
            ) : (
              <p>슬라이드가 없습니다.</p>
            )}
          </div>
        </div>
      </div>

      {scriptModal && (
        <div className="script-overlay">
          <div className="script-content">
            <h3>스크립트 추가</h3>
            <br></br>
            <h1>담당자</h1>
            <input
              type="text"
              value={selectedMember}
              onChange={(e) => setSelectedMember(e.target.value)}
              placeholder="담당자 이름을 입력하세요"
            />
            <textarea
              value={newScript}
              onChange={(e) => setNewScript(e.target.value)}
              placeholder="스크립트를 입력하세요"
              rows="5"
            />
            <div className="script-buttons">
              <button onClick={() => setScriptModal(false)} className="cancel-btn">
                취소
              </button>
              <button onClick={saveScript} className="save-btn">
                저장
              </button>
            </div>
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
      
    </>
  );
};

export default Slide;