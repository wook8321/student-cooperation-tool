import React, { useState, useEffect, useCallback } from "react";
import axios from "axios";
import { Client } from "@stomp/stompjs";
import "./App.css";
import ChatPage from "./chat.tsx";
import chatImage from './images/chat.svg';
import CheckImage from './images/check-circle.svg';
import unCheckImage from './images/check.svg';
import userImage from './images/user.svg';

const domain = "http://localhost:8080"

const Part = (roomId) => {
    const [parts, setParts] = useState({num: 0, parts: []});
    const [partID, setPartID] = useState(""); // 존재하는 역할을 수정하거나 삭제할 때 필요한 주제 ID

    const [newPartName, setNewPartName] = useState(""); // 역할 추가 시 필요한 역할 이름
    const [selectedMember, setSelectedMember] = useState(""); // 역할 추가 시 필요한 역할 담당자
    
    const [error, setError] = useState(null);
    const [addModal, setAddModal] = useState(false);
    const [updateModal, setUpdateModal] = useState(false);
    const [reviewModal, setReviewModal] = useState(false);
    const [menuOpen, setMenuOpen] = useState(false); // 드롭다운 열림/닫힘 상태
    const [chatModal, setChatModal] = useState(false);
    const [reviewPartId, setReviewPartId] = useState(null);

    const [review, setReview] = useState({num: 0, reviews:[]}); // 기존 평가 내용
    const [reviewContent, setReviewContent] = useState(""); // 평가 내용

    const PartsList = ({ roomId }) => {
        useEffect( () => {
            axios.get(`${domain}/api/v1/rooms/${roomId}/parts`)
                .then((res) => {
                    setParts(res.data.data);
                })
                .catch(() => {
                    setError(new Error("Failed to get partslist"));
                });
        }, []);
      }
    
    const stompClient = new Client({
      brokerURL: `ws://http://localhost:8080/sub/rooms/${roomId}/part`, // 역할 추가 단계 WebSocket
      reconnectDelay: 5000,
      onConnect: () => {
        stompClient.subscribe(`${domain}/sub/rooms/${roomId}/part`);
      },
      onStompError: (frame) => {
        setError(new Error("STOMP error: ", frame.headers["message"]));
      },
    });
    
    stompClient.activate();

    // 주제 추가 함수
    const addPart = (newPart) => {
      if (!newPart.trim()) return;

      setParts((prev) => {
        const newPartObject = [{
          partId : prev.num + 1,
          partName : newPart,
          /* nickName : userId,
          profile : userProfile, */
          files : [],
        }];

        const updatedParts = {
          num: prev.num + 1,
          parts: [...prev.parts, newPartObject],
        };

      stompClient.publish({
        destination : `${domain}/pub/parts/add`,
        body : updatedParts
      });

      return updatedParts; // 상태 업데이트
      });

      setNewPartName(""); // 입력 초기화
    };

    const deletePart = (part_id) => {
      setParts((prev) => {
        const deletedPart = prev.parts.filter((part) => part.partId !== part_id);

        stompClient.publish({
          destination :`${domain}/pub/parts/delete`,
          body : parts
        });

        return deletedPart;
      });


    const updatePart = (part_id, memberId) => {

        const updatedParts = parts.parts.map((part) =>
        part.partId === part_id
          ? { ...part, nickName: memberId } // 담당자만 변경
          : part // 다른 역할은 변경하지 않음
        );
        
        setParts(updatedParts);

        stompClient.publish({
          destination : `${domain}/pub/parts/update`,
          body : parts
      });
    };

    const openAddModal = () => {
      setAddModal(true);
    }
    const closeAddModal = () => setAddModal(false);
    const openUpdateModal = () => setUpdateModal(true);
    const closeUpdateModal = () => setUpdateModal(false);
    const openReviewModal = (partId) => setReviewPartId(partId); setReviewModal(true);
    const closeReviewModal = () => setReviewModal(false);

    const toggleMenu = (part_id) => {
        setMenuOpen(!menuOpen); // 드롭다운 열림/닫힘 토글
        if(menuOpen) setPartID(part_id); // 드롭다운이 열리면 주제 ID 저장
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
                  body: ({
                      fileName: file.name,
                      fileCode: event.target.result, // Base64로 인코딩된 파일 내용
                  }),
                  headers: { "content-type": "multipart-form-data" },
                  });
                  console.log(`${file.name} file is sent.`);

              } else {
                  setError(new Error("STOMP Client is not connected"));
              }
            };
    
            reader.onerror = () => {
                setError(new Error("Failed to read the file"));
            };
    
            reader.readAsDataURL(file); // 파일을 Base64로 읽기
        });
    }

    const submitReview = (reviewContent, partId) => {
        const payload = {
          /* reviewId: userId,  (유저 아이디 필요) */
          content: reviewContent
        };

        axios.post(`/api/v1/parts/${partId}/review`, payload)
          .then(() => {
            setReview((prev) => [...prev, reviewContent]);
            setReviewContent(""); // 입력창 초기화
          })
          .catch(() => {
              setError(new Error("Failed to submit review"));
          });
    };

    const ClickMember = (part_name) => {
      const [isClicked, setIsClicked] = useState(false);
      const filteredPart = parts.parts.filter((part) => part.partName === part_name);
      
      const changeLike = useCallback(() => {
        setIsClicked(!isClicked);
      }, [isClicked]);

      if(isClicked) {
        setSelectedMember(filteredPart.nickName);
        setNewPartName(filteredPart.partName);
      }

      return (
        <div className="member-container">
          <button className="check-button" onClick={() => changeLike}>
            <MemberImage Click={isClicked} />
          </button>
        </div>
      )
    }

    const MemberImage = ({Click}) => { 
      return Click ? (
          <img src={CheckImage} width={24} height={24}/>
      ) : (
          <img src={unCheckImage} width={24} height={24}/>
      );
    };

    const ReviewList = ({ partId }) => {
      useEffect( () => {
          axios.get(`${domain}/api/v1/parts/${partId}/review`)
              .then((res) => {
                  setReview(res.data.data);
              })
              .catch(() => {
                  setError(new Error("Failed to get review"));
              });
      }, [partId]);

      if (review.length === 0) 
        setError(new Error("There is no error"));

      return (
        <div>
            <h3> 자료 평가 </h3>
            <ul>
                {review.map((review) => (
                    <li key={review.reviewId}>  
                       <img src={review.profile || userImage} alt={`${review.nickName}'s 프로필`} />
                       {review.nickName} ({review.createdTime[0]-review.createdTime[1]-review.createdTime[2]}) : {review.content}
                    </li>
                ))}
            </ul>
        </div>
      );
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

    const downloadFile = (file) => {
      const url = URL.createObjectURL(file); // 파일 객체를 Blob URL로 변환
      const link = document.createElement("a"); // <a> 태그 생성으로 파일 다운로드
      link.href = url;
      link.download = file.name; // 원래 파일 이름으로 다운로드
      link.click();
      URL.revokeObjectURL(url); // Blob URL 해제
  };

    const deleteFile = (file) => {
        setFiles((prevFiles) =>
          prevFiles.filter((_, i) => i !== file)
        );
      
        stompClient.publish({
          destination: "/pub/part/file/delete"});
    }

    return (
    <>
      <ErrorModal error={error} closeErrorModal={closeErrorModal} /> {/* 에러 발생 시 모달 창*/}
        <PartsList />
        <ul>
          {parts.map((part) => (
            <li key={part.partId}>
              {part.partName} : {part.memberId}
              <button className="arrow-button" onClick={toggleMenu(part.partId)}>
                ▼
              </button>
            </li>
          ))}
        </ul>

        <div className="add_modal">
          <button className="role-add-btn" onClick={() => openAddModal}>
            역할 추가
          </button>
    
          {addModal && (
            <div className="modal-overlay">
              <div className="modal" onClick={(e) => e.stopPropagation()}>
                <h3 className="modal-title">역할 추가</h3>
                <button className="modal-close-button" onClick={() => closeAddModal}></button>
                <form>
                  <label htmlFor="partName">역할 이름</label>
                  <input
                    id="partName"
                    type="text"
                    placeholder="역할 이름을 입력하세요"
                    onChange={(e) => setNewPartName(e.target.value)}
                  />
                  <h4>담당자</h4>
                  
                  <ul className="members-list">
                    {parts.map((part) => (
                        <li key={part.partId}>
                            {part.parts.profile} {part.parts.nickname}
                            <button img src={unCheckImage} width={24} height={24} onClick={ClickMember(part.partId)}/>
                        </li>
                    ))}
                  </ul>
                  <button className="add-button" type="submit" onClick={addPart(newPartName)}>
                    생성
                  </button>
                </form>
              </div>
            </div>
          )}
        </div>

        {menuOpen && (
        <div className="dropdown-menu">
          <button className="menu-item" onClick={deletePart(partID)}>역할 삭제</button>
          <button className="menu-item" onClick={openUpdateModal}>역할 수정</button>
          <button className="menu-item" onClick={openReviewModal}>자료 평가</button>
          <form>
            <input 
                className='file-input'
                type="file"
                mulitple
                style={{ display: "none" }}
                onChange={handleFilesChange}
            />
            <button onClick={() => uploadFiles}> 파일 업로드</button>
          </form>
            <div className="uploaded-files">
              <h3>업로드된 파일 목록</h3>
                <ul>
                    {files.map((file, index) => (
                        <li key={index}>
                          {file.name}
                          <button className="download-file-button" onClick={() => downloadFile(file)}>
                            다운로드
                          </button>
                          <button className="delete-file-button" onClick={() => deleteFile(index)}>
                            삭제
                          </button>
                        </li>
                    ))}
                </ul>
            </div>
        </div>
        )}

        {updateModal && (
            <div className="modal-overlay">
              <div className="modal" onClick={(e) => e.stopPropagation()}>
                <h3 className="modal-title">역할 추가</h3>
                <button className="close-add-button" onClick={() => closeUpdateModal}></button>
                <form>
                  <label htmlFor="partName">역할 이름</label>
                  <input
                    id="partName"
                    type="text"
                    placeholder="역할 이름을 입력하세요"
                    onChange={(e) => setNewPartName(e.target.value)}
                  />
                  <h4>담당자</h4>
                  <ul className="members-list">
                    {parts.map((part) => (
                        <li key={part.partId}>
                          {part.parts.profile} {part.parts.nickName}
                            {/* if(part.parts.nickName === part.participation) 참가자들의 id를 하나씩 비교해서 역할 담당자와 이름이 같으면 체크 표시가 되어있어야 함. */}
                              <button img src={unCheckImage} width={24} height={24} onClick={ClickMember(part.partId)}/>
                            
                        </li>
                    ))}
                  </ul>
                  <button className="update-button" onClick={updatePart(newPartName, selectedMember)}>
                    수정
                  </button>
                </form>
              </div>
            </div>
        )}

        {reviewModal && (
            <div className="review-modal-overlay">
              <div className="review-modal" onClick={(e) => e.stopPropagation()}>
                <ReviewList />
                <button className="close-review-button"onClick={() => closeReviewModal}> X </button>
                
                <textarea
                  value={reviewContent}
                  onChange={(e) => setReviewContent(e.target.value)}
                  placeholder="자료 평가 내용을 입력하세요."
                />
                <div className="review-actions">
                    <button className="submit-review-button" onClick={() => submitReview(reviewContent,)}> 등록 </button>
                    
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