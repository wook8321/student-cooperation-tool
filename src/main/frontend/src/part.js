import React, { useState } from "react";
import axios from "axios";
import { Client } from "@stomp/stompjs";
import "./App.css";
import ChatPage from "./chat.tsx";
import chatImage from './images/chat.svg';

const Part = (roomId) => {
    const [parts, setParts] = useState([]);
    const [newPart, setNewPart] = useState("");
    const [error, setError] = useState(null);
    const [addModal, setAddModal] = useState(false);
    const [updateModal, setUpdateModal] = useState(false);
    const [reviewModal, setReviewModal] = useState(false);
    const [reviewContent, setReviewContent] = useState(""); // 평가 내용
    const [isMenuOpen, setIsMenuOpen] = useState(false); // 드롭다운 열림/닫힘 상태
    const [chatModal, setChatModal] = useState(false);

    const PartsList = ({ roomId }) => {
        useEffect( () => {
            axios.get(`${domain}/api/v1/rooms/${roomId}/parts`)
                .then((res) => {
                    setParts(res.data);
                })
                .catch(() => {
                    setError('failed to load friends');
                });
        }, []);
      }
    
    const stompClient = new Client({
        brokerURL: `ws://http://localhost:8080/sub/rooms/${roomId}/part`, // 역할 추가 단계 WebSocket
        reconnectDelay: 5000,
        onConnect: () => {
            socket.subscribe(`${domain}/sub/rooms/${roomId}/part`);
        },
        onStompError: (frame) => {
            console.error("STOMP error: ", frame.headers["message"]);
        },
    });
    
    socket.activate();

    // 주제 추가 함수
    const addPart = () => {
        const newPart = { roomId, title: newTopic, };
        setParts([...parts, newPart]); // 새 주제 추가
        socket.publish(`${domain}/pub/parts/add`);
        setNewPart(""); // 입력 초기화
        setAddModal(false);
    };

    const deletePart = (part_Id) => {
        const deletedPart = parts.filter((part) => part.partId !== part_Id);
        setTopics(deletedPart);
        socket.publish(`${domain}/pub/parts/delete`);
    };

    const updatePart = (part_Id, memberId) => {
        const updatedParts = parts.map((part) =>
            part.partId === part_Id
                ? { ...part, member: memberId }
                : part // 다른 역할은 변경하지 않음
        );
        setParts(updatedParts);
        socket.publish(`${domain}/pub/parts/update`);
        setUpdateModal(false);
    };

    const openAddModal = () => setAddModal(true);
    const closeAddModal = () => setAddModal(false);
    const openUpdateModal = () => setUpdateModal(true);
    const closeUpdateModal = () => setUpdateModal(false);
    const openReviewModal = () => setReviewModal(true);
    const closeReviewModal = () => setReviewModal(false);

    const toggleMenu = () => {
        setIsMenuOpen(!isMenuOpen); // 드롭다운 열림/닫힘 토글
    };
    
    const [files, setFiles] = useState([]); //업로드한 파일 데이터 보관

    const handleFilesChange = (e) => {
        setFiles(Array.from(e.target.files));
    }

    const uploadFiles = (e) => {
        e.preventDefault();

       // 선택된 파일을 하나씩 읽어서 전송
        files.forEach((file) => {
            const reader = new FileReader();
            reader.onload = (event) => {
            console.log("파일 내용:", event.target.result);
    
            // STOMP 메시지 전송
            if (stompClient.connected) {
                stompClient.publish({
                destination: "/pub/part/file/add",
                body: JSON.stringify({
                    fileName: file.name,
                    fileCode: event.target.result, // Base64로 인코딩된 파일 내용
                }),
                headers: { "content-type": "multipart-form-data" },
                });
    
                console.log(`${file.name} file is sent.`);
            } else {
                setError("STOMP Client is not connected");
            }
            };
    
            reader.onerror = () => {
                setError("Failed to read the file");
            };
    
            reader.readAsDataURL(file); // 파일을 Base64로 읽기
        });
    }

    const handleSubmit = () => {
        const payload = {
          content: reviewContent,
          reviewId: filereviewId
        };

        axios
        .post(`/api/v1/parts/${partId}/review`, payload)
        .then(() => {
            setReviewContent(""); // 입력창 초기화
            setIsModalOpen(false); // 모달 닫기
        })
        .catch((error) => {
            setError(error);
        });
    };

    return (
    <>
        <PartsList />
        <ul>
          {parts.map((part) => (
            <li key={part.partId}>
              {part.partName} : {part.memberId}
              <button className="arrow-button" onClick={toggleMenu}>
                ▼
              </button>
            </li>
          ))}
        </ul>

        <div className="add_modal">
          <button className="role-add-btn" onClick={openAddModal}>
            역할 추가
          </button>
    
          {addModal && (
            <div className="modal-overlay" onClick={closeAddModal}>
              <div className="modal" onClick={(e) => e.stopPropagation()}>
                <h3 className="modal-title">역할 추가</h3>
                <form>
                  <label htmlFor="partName">역할 이름</label>
                  <input
                    id="partName"
                    type="text"
                    placeholder="역할 이름을 입력하세요"
                  />
                  <h4>담당자</h4>
                  <ul className="members-list">
                    {parts.map((part) => (
                        <li key={part.partId}>
                            <button>
                                {part.memberId}
                            </button>
                        </li>
                    ))}
                  </ul>
                  <button className="create-btn" onClick={addPart}>
                    생성
                  </button>
                </form>
              </div>
            </div>
          )}
        </div>

        {isMenuOpen && (
        <div className="dropdown-menu">
          <button className="menu-item" onClick={deletePart}>역할 삭제</button>
          <button className="menu-item" onClick={openUpdateModal}>역할 수정</button>
          <button className="menu-item" onClick={openReviewModal}>자료 평가</button>
          <form>
            <input 
                className='file-input'
                type="file"
                mulitple
                onChange={handleFilesChange}
            />
            <button onClick={uploadFiles}>파일 업로드</button>
          </form>
        </div>
        )}

        {updateModal && (
            <div className="modal-overlay" onClick={closeUpdateModal}>
              <div className="modal" onClick={(e) => e.stopPropagation()}>
                <h3 className="modal-title">역할 추가</h3>
                <form>
                  <label htmlFor="partName">역할 이름</label>
                  <input
                    id="partName"
                    type="text"
                    placeholder="역할 이름을 입력하세요"
                  />
                  <h4>담당자</h4>
                  <ul className="members-list">
                    {parts.map((part) => (
                        <li key={part.partId}>
                            <button>
                                {part.participantId}
                            </button>
                        </li>
                    ))}
                  </ul>
                  <button className="create-btn" onClick={updatePart}>
                    수정
                  </button>
                </form>
              </div>
            </div>
        )}

        {reviewModal && (
            <div className="review-modal-overlay" onClick={closeReviewModal}>
              <div className="review-modal" onClick={(e) => e.stopPropagation()}>
                <h3 className="review-modal-title">역할 추가</h3>
                <textarea
                value={reviewContent}
                onChange={(e) => setReviewContent(e.target.value)}
                placeholder="자료 평가 내용을 입력하세요."
                />
                <div className="review-actions">
                    <button className="submit-review-btn" onClick={handleSubmit}>등록</button>
                    <button className="close-review-btn"onClick={closeReviewModal}>닫기</button>
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
                    <ChatPage />
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
    </>
    );
} 
export default Part;