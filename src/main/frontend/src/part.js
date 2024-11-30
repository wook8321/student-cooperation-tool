import React, {useState, useEffect, useCallback, useRef} from "react";
import axios from "axios";
import "./review-modal.css"
import {domain} from "./domain";
import "./part.css"
import "./dropbox.css"
import { useWebSocket } from './WebsocketContext';
import {useNavigate} from "react-router-dom"; // WebSocketProvider의 훅 사용


//
const Part = () => {
    const [parts, setParts] = useState({num: 0, parts: []});
    const [partID, setPartID] = useState(""); // 존재하는 역할을 수정하거나 삭제할 때 필요한 주제 ID
    const [newPartName, setNewPartName] = useState(""); // 역할 추가 시 필요한 역할 이름
    const [selectedMember, setSelectedMember] = useState(""); // 역할 추가 시 필요한 역할 담당자
    const [error, setError] = useState(null);
    const [addModal, setAddModal] = useState(false);
    const [menuOpen, setMenuOpen] = useState(false); // 드롭다운 열림/닫힘 상태

    const [isOpen, setIsOpen] = useState(false);
    const [partName, setPartName] = useState("")
    const [selectedMemberId, setSelectedMemberId] = useState(null);
    const [participation, setParticipation] = useState({ num: 0, participation: [] });
    const {stompClient, isConnected, roomId} = useWebSocket(); // WebSocket 연결 관리
    const subscriptions = useRef([]); // 구독후 반환하는 객체로, 해당 객체로 구독을 취소해야 한다.
    const navigate = useNavigate();


    const PartsList = () => {
        axios.get(`${domain}/api/v1/rooms/${roomId}/parts`)
            .then((res) => {
                console.log(res.data.data)
                setParts(res.data.data);
            })
            .catch(() => {
                setError(new Error("Failed to get partslist"));
            });
    }

    //=============================================웹소켓========================================================

    const receiveMessage = (message) => {
        //3-1 구독한 url에서 온 메세지를 받았을 때
        console.log(JSON.parse(message.body))
        const frame = JSON.parse(message.body)

        if(frame.messageType === "PART_ADD"){
            addPartInScreen(frame.data);
        } else if(frame.messageType === "PART_DELETE"){
            deletePartInScreen(frame.data)
        } else if(frame.messageType === "PART_UPDATE"){
            updatePartInScreen(frame.data)
        } else if(frame.messageType === "PART_FILE_UPLOAD"){
            uploadFileInScreen(frame.data)
        } else if(frame.messageType === "PART_FILE_REMOVE"){
            deleteFileInScreen(frame.data)
        } else {
            console.log("Not Supported Message Type")
        }
    }

    const receiveError = (error) => {
        //3-2 구독한 url에서 온 메세지를 못 받아 에러가 발생했을 때
        alert("방에 입장에 실패하였습니다.");
        console.error("STOMP Error", error);
        window.location.href = "/";
    }

    const onConnect = () => {
        //2-1 연결 성공의 경우
        PartsList()
        subscriptions.current = stompClient.current.subscribe(
            `/sub/rooms/${roomId}/part`,
            receiveMessage,
            receiveError
        );
    }

    useEffect(() => {
        //1. broker endPoint에 연결, WebsocketConfig에 설정한 EndPoint를 말함
        alert("part" + roomId)
        if (stompClient.current) {
            stompClient.current.activate(); // 웹소켓 활성화
        }

        return () => {
            if (stompClient.current) {
                alert(`/sub/rooms/${roomId}/part, 구독 취소`)
                subscriptions.current.unsubscribe(); // 언마운트 시 웹소켓 비활성화
            }
        };
    }, []);

    useEffect(() => {
        if (isConnected) {
            onConnect(); // 연결이 완료되면 onConnect 호출
        }
    }, [isConnected]); //isConnected 상태가 바뀌면 실행된다.

    const openAddModal = () => {
        axios
            .get(`/api/v1/rooms/${roomId}/participations`)
            .then((res) => {
                setParticipation(() => ({
                    num: res.data.data.num, // 역할 개수 증가
                    participation: res.data.data.participations
                }));
            })
            .catch((error) => {
                console.error("Failed to fetch participation data:", error);
            });
        setAddModal(true);
    }
    // ========================================== 역할 추가 ============================================
    const addPartInScreen = (part) => {
        alert("파트 추가!")
        setParts((preParts) => ({
            ...preParts,
            num: preParts.num + 1,
            parts: [...preParts.parts, part]
        }))
    }

    const addPart = () => {
        closeAddModal()

        const data = {
            roomId : roomId,
            partName : partName,
            memberId : selectedMemberId
        }

        stompClient.current.publish({
            destination: '/pub/parts/add',
            body: JSON.stringify(data)
        })
    };

    // ========================================== 파일 업로드 ============================================

    const uploadFileInScreen = (file) => {
        console.log(file);  // 업로드한 파일 정보 확인

        // 파트를 업데이트하고 해당 파트의 파일 목록을 수정
        setParts((preParts) => ({
            ...preParts,
            parts: preParts.parts.map((part) =>
                // partId가 일치하는 파트만 파일 목록을 업데이트
                part.partId === file.partId
                    ? {
                        ...part,
                        files: part.files ? [...part.files, file] : [file],  // 새 파일을 파일 목록에 추가
                    }
                    : part // 해당 파트는 그대로 두기
            ),
        }));
    };


    // ========================================== 파일 다운로드 ============================================

    const downloadFile = (fileName,fileOriginalName) => {
        alert("다운로드 = " + fileName + "s3에 저장된 이름 = " + fileOriginalName)
        axios
            .get(`/api/v1/files/${fileName}?roomId=${roomId}&fileOriginalName=${fileOriginalName}`,{
                responseType: 'blob' // 없다면 다운로드하지 않음
            })
            .then((res) =>{
                const url = window.URL.createObjectURL(new Blob([res.data])); // Blob 생성
                const link = document.createElement('a'); // a 태그 생성
                link.href = url;
                link.setAttribute('download', fileOriginalName); // 원래 파일명으로 다운로드
                document.body.appendChild(link);
                link.click(); // 다운로드 실행
                link.remove(); // DOM에서 제거
            })
            .catch((error) =>{
                console.error(error)
            })
    };

    // ========================================== 파일 삭제 ============================================

    const deleteFileInScreen = (file) => {
        setParts((preParts) => ({
            ...preParts,
            parts: preParts.parts.map((part) =>
                // partId가 일치하는 파트만 파일 목록을 업데이트
                part.partId === file.partId
                    ? {
                        ...part,
                        files: part.files ? part.files.filter((preFile) => preFile.fileId !== file.fileId) : [],
                    }
                    : part // 해당 파트는 그대로 두기
            ),
        }));
    };

    const deleteFile = (fileName,fileId,partId) => {
        alert("삭제 = " + fileName + "fileId = " + fileId)
        const data = {
            roomId : roomId,
            fileId : fileId,
            partId : partId,
            fileName : fileName
        }

        stompClient.current.publish({
            destination:"/pub/file/delete",
            body : JSON.stringify(data)
        })
    }

    //============================================파일 미리보기=========================================================
    const PreviewFile = ({fileUrl, fileType}) => {
        console.log(fileUrl)
        console.log(fileType)
        if (fileType === "PNG" || fileType ==="JPG") {
            return <img src={fileUrl} alt="미리보기 이미지" style={{ maxWidth: "50%" }} />;
        } else if (fileType === "PDF") {
            return <iframe src={fileUrl} width="50%" height="300px" />;
        } else if ( fileType === "DOCX" || fileType === "XLS" || fileType === "XLSX" ) {
            const encodedUrl = encodeURIComponent(fileUrl);
            return (
                <iframe
                    src={`https://view.officeapps.live.com/op/embed.aspx?src=${encodedUrl}`}
                    width="50%"
                    height="300px"
                />
            );
        } else {
            return <span>미리 보여줄 수 없는 파일입니다.</span>;
        }
    };
    //==============================================================================================================

    const closeAddModal = () => {
        setSelectedMemberId(0)
        setAddModal(false)
    }

    //역할 삭제 후, 웹소켓 메세지를 받아서 화면에 반영
    const deletePartInScreen = (part) => {
        setParts((preParts) =>({
            ...preParts,
            num: preParts.num - 1,
            parts: preParts.parts.filter((p) => p.partId !== part.partId)
        }))
    }

    //역할 수정 후, 웹소켓 메시지를 받아서 화면에 반영
    const updatePartInScreen = (updatedPart) => {
        setParts((preParts) => ({
            ...preParts,
            parts: preParts.parts.map((part) =>
                part.partId === updatedPart.partId
                    ? { ...part, partName: updatedPart.partName,
                        nickname: updatedPart.nickname,
                        profile: updatedPart.profile,
                        memberId: updatedPart.memberId
                      }
                    : part
            )
        }));
    };

    // ========================================== 역할 메뉴 ============================================

    //<< 다른 코드를 볼때는 접어서 보는 것을 추천(해당 dropbox에는 삭제,수정, 파일 업로드 기능들이 있음)
    const Dropdown = ({ part }) => {
        const [isOpen, setIsOpen] = useState(false);
        const [updateModal, setUpdateModal] = useState(false);
        const [participation, setParticipation] = useState({ num: 0, participation: [] });
        const [updatedMemberId, setUpdatedMemberId] = useState(null);
        const [updatedPartName, setUpdatedPartName] = useState("")
        const [reviewModal, setReviewModal] = useState(false);
        const [reviews, setReviews] = useState({num : 0, reviews : []})
        const [reviewAddModal, setReviewAddModal] = useState(false)
        const [reviewTextArea, setTextArea] = useState("")
        const [fileUploadModal, setFileUploadModal] = useState(false)
        const [uploadingFile, setUploadingFile] = useState(null);
        const MAX_FILE_SIZE = 7 * 1024 * 1024; // 5MB 제한

        // 드롭다운 열기/닫기
        const toggleDropdown = () => {
            if(isOpen){
                setIsOpen(false)
            } else{
                setIsOpen(true)
            }
        };

        // 드롭다운 외부 클릭 시 닫기
        const closeDropdown = (e) => {
            if (!e.currentTarget.contains(e.relatedTarget)) {
                //자기 자신이 아닌 다른 곳을 클릭했다면 드롭박스 닫기
                setIsOpen(false);
            }
        };


        // 1.역할 삭제 함수
        const deletePart = (partId) => {
            const data = {
                roomId: roomId,
                partId: partId
            }

            stompClient.current.publish({
                destination: "/pub/parts/delete",
                body: JSON.stringify(data)
            })
        };

        //2. 역할 수정
        const openUpdateModal = () => {
            alert("수정 모달")
            setIsOpen(false)
            //방에 유저들을 조회
            axios
                .get(`/api/v1/rooms/${roomId}/participations`)
                .then((res) => {
                    setParticipation(() => ({
                        num: res.data.data.num, // 역할 개수 증가
                        participation: res.data.data.participations
                    }));
                })
                .catch((error) => {
                    console.error("Failed to fetch participation data:", error);
                });
            setUpdatedPartName(part.partName);  // partName 초기화
            setUpdatedMemberId(part.memberId);  // memberId 초기화
            setUpdateModal(true);
        }

        const closeUpdateModal = () => {
            setUpdatedMemberId(part.memberId)
            setUpdatedPartName(part.partName)
            setUpdateModal(false);
        }

        const updatePart = () => {
            setUpdateModal(false)
            const data = {
                roomId: roomId,
                partId: part.partId,
                partName : updatedPartName,
                memberId : updatedMemberId
            }
            stompClient.current.publish({
                destination : "/pub/parts/update",
                body : JSON.stringify(data)
            })
        }

        //3. 리뷰 조회
        const closeReviewModal = (e) => {
            if (!e.currentTarget.contains(e.relatedTarget)) {
                //자기 자신이 아닌 다른 곳을 클릭했다면 드롭박스 닫기
                setReviewModal(false);
            }
        }

        //4. 리뷰 작성
        const openAddReviewModal = () => {
            setReviewModal(false)
            setReviewAddModal(true)
        }

        const openReviewModal = () => {
            setIsOpen(false)
            axios
                .get(`/api/v1/parts/${part.partId}/review`)
                .then((res) => {
                    setReviews(() => ({
                        num: res.data.data.num, // 역할 개수 증가
                        reviews: res.data.data.reviews
                    }));
                    console.log(res.data.data)
                })
                .catch((error) =>{
                    console.log(error.message)
                })
            //리뷰창 뛰우기
            setReviewModal(true)
        }

        const addReview = () => {
            const data = {
                partId : part.partId,
                content : reviewTextArea
            }
            axios
                .post("/api/v1/parts/review",data)
                .then(() => {
                    // 정상 등록 후, 리뷰 등록 모달을 내리고, 리뷰 조회 모달을 뛰운다.
                    setReviewAddModal(false)
                    openReviewModal()
                })
                .catch((error) =>{
                    console.error(error)
                })
        }

        //5. 파일 업로드

        const closeFileUploadModal = () => {
            setFileUploadModal(false)
            setUploadingFile(null)
        }

        const uploadFileWebsocket = (file) => {
            const data ={
                roomId : roomId,
                partId : part.partId,
                fileId: file.fileId,
                originalName: file.originalName,
                fileType: file.fileType,
                fileName: file.fileName,
            }
            console.log(data)
            stompClient.current.publish({
                destination:"/pub/file/upload",
                body : JSON.stringify(data)
            })
        }

        const uploadFile = () =>{
            if (uploadingFile) {
                if (uploadingFile.size > MAX_FILE_SIZE) {
                    // 업로드 할 수 있는 파일 크기 제한보다 큰 파일을 업로드는 올릴 수 없다.(서버엔 10MB 설정)
                    alert("파일 크기가 너무 큽니다. 업로드 제한은 7MB입니다.");
                    return;
                }

                // 파일 이름 출력
                console.log("파일 이름: " + uploadingFile.name);
                // FileReader로 파일 읽기
                const reader = new FileReader();
                reader.onload = function(e) {
                    const data = {
                        roomId : roomId,
                        partId : part.partId,
                        fileName : uploadingFile.name,
                        fileCode: e.target.result
                    }
                    console.log("파일 내용:", data);
                    axios
                        .post("/api/v1/files",data)
                        .then((res) =>{
                            uploadFileWebsocket(res.data.data.files[0])
                        })
                        .catch((error) =>{
                            console.error(error)
                        })
                }
                reader.readAsDataURL(uploadingFile);
            }
        }

        return (
            <div className="dropdown" onBlur={(e) => closeDropdown(e)} tabIndex={0}>
                <button className="dropdown-btn" onClick={() => toggleDropdown()}>
                    ⚙
                </button>
                {isOpen && (
                    <ul className="dropdown-menu">
                        <li>
                            <button onClick={() => deletePart(part.partId)} className="dropdown-item">
                                역할 삭제하기
                            </button>
                        </li>
                        <li>
                            <button className="dropdown-item" onClick={() => openUpdateModal()}>
                                역할 수정하기
                            </button>
                        </li>
                        <li>
                            <button className="dropdown-item" onClick={() => openReviewModal()}>
                                리뷰 보기
                            </button>
                        </li>
                        <li>
                            <button className="dropdown-item" onClick={() => setFileUploadModal(true)}>
                                파일 올리기
                            </button>
                        </li>
                    </ul>
                )}

                {fileUploadModal && (
                    <div className="review-modal-overlay">
                        <div className="review-modal-content" onClick={(e) => e.stopPropagation()}>
                            <button className="review-close-button" onClick={() => closeFileUploadModal()}> X </button>
                            <h2 className="review-modal-title">파일 올리기</h2>
                            <input id="file-upload" className="file-input" type="file"
                                   onChange={(e) => setUploadingFile(e.target.files[0])}/>
                            <div className="review-write-buttons">
                                <button className="review-write-button" onClick={() => uploadFile()}>파일 업로드</button>
                            </div>
                        </div>
                    </div>
                )}

                {updateModal && (
                    <div style={{ textAlign: "center", justifyContent: "center" }} className="modal_overlay">
                        <div className="modal_content">
                            <button className="close_button" onClick={() => closeUpdateModal()}>
                                x
                            </button>
                            <div className="modal_body">
                                <h3>역할 추가</h3>
                                <div className="modal_section">
                                    <label className="modal_label" htmlFor="partName">
                                        역할 이름
                                    </label>
                                    <input className="modal_input" id="partName"
                                        type="text" value={updatedPartName}
                                        onChange={(e) => setUpdatedPartName(e.target.value)}/>
                                </div>
                                <div className="modal_section">
                                    <h4>담당자</h4>
                                    <ul className="members-list">
                                        {participation.num > 0 ?
                                            (participation.participation.map((p) => (
                                                <li key={p.memberId} id={"part" + p.memberId}>
                                                    <img src={p.profile} alt={`${p.nickname}'s profile`} />
                                                    <p>{p.nickname}</p>
                                                    <input type="radio" value={p.memberId}
                                                        checked={updatedMemberId === p.memberId}
                                                        onChange={() => setUpdatedMemberId(p.memberId)}/>
                                                </li>
                                            ))) : <h2> 참여자가 없습니다.</h2>
                                        }
                                    </ul>
                                </div>
                                <button className="add-button" onClick={() => updatePart()}>
                                    수정하기
                                </button>
                            </div>
                        </div>
                    </div>
                )}

                {reviewModal && (
                    <div className="review-modal-overlay" onBlur={(e) => closeReviewModal(e)}>
                        <div className="review-modal-content" onClick={(e) => e.stopPropagation()}>
                            <button className="review-close-button" onClick={() => setReviewModal(false)}> X </button>
                            <h2 className="review-modal-title">Review</h2>
                            {reviews.reviews.map((review) => (
                                <div key={review.reviewId} className="review-card">
                                    <img src={review.profile} alt={`${review.nickName} 프로필`} className="review-card-profile"/>
                                    <div className="review-card-details">
                                        <p className="review-card-name">{review.nickName}</p>
                                        <p className="review-card-text">{review.content}</p>
                                    </div>
                                </div>
                            ))}
                            <button className="review-write-button" onClick={() => openAddReviewModal(true)}>
                                리뷰 작성하기
                            </button>
                        </div>
                    </div>
                )}

                {reviewAddModal && (
                    <div className="review-modal-overlay">
                        <div className="review-modal-content">
                            <h2 className="review-modal-title">리뷰 작성</h2>
                            <div className="review-cart">
                                <textarea id="review" className="review-write-textarea" placeholder="리뷰를 작성해주세요"
                                          onChange={(e) => setTextArea(e.target.value)}>
                                </textarea>
                            </div>
                            <div className="review-write-buttons">
                                <button className="review-write-button" onClick={() => setReviewAddModal(false)}>
                                    취소
                                </button>
                                <button className="review-write-button" onClick={() => addReview()}>
                                    등록
                                </button>
                            </div>

                        </div>
                    </div>
                )}
            </div>
        );
    };
    // ================================================================================================

    const goSection = (path, subUrl) => {
        alert(path + " " + subUrl)
        navigate(path, {state: {
                roomId,
                subUrl: subUrl
            }})
    }

    if (!isConnected) {
        // 연결 중인 상태일 때는 로딩 상태로
        return (<div className="loading">
            <div className="loading-container">
                <div className="spinner"></div>
                <p>로딩 중...</p>
            </div>
        </div>);
    }

    return (
    <>
        <main className="topic-background">
            <ul>
                {parts.parts.map((part) => (
                    <li key={part.partId}>
                        <span>{part.partName}</span>
                        <img src={part.profile} alt="프로필" />
                        <span>{part.nickName}</span>
                        <Dropdown part={part} />
                        <ul>
                            {part.files?.length > 0 ? (
                                part.files.map((file) => (
                                    <li key={file.fileId}>
                                        <span>{file.originalName}</span>
                                        <PreviewFile fileUrl={file.fileUrl} fileType={file.fileType} />
                                        <button onClick={() => deleteFile(file.fileName, file.fileId,part.partId)}>x</button>
                                        <button onClick={() => downloadFile(file.fileName, file.originalName)}>
                                            다운로드
                                        </button>
                                    </li>
                                ))
                            ) : (
                                <span>업로드한 파일이 없습니다.</span>
                            )}
                        </ul>
                    </li>
                ))}
                <li>
                    <button className="role-add-btn" onClick={() => openAddModal()}>
                        역할 추가
                    </button>
                </li>
            </ul>


            {addModal && (
                <div style={{ textAlign: "center", justifyContent: "center" }} className="modal_overlay">
                    <div className="modal_content">
                        <button className="close_button" onClick={() => closeAddModal()}>x</button>
                        <div className="modal_body">
                            <h3>역할 추가</h3>
                            <div className="modal_section">
                                <label className="modal_label" htmlFor="partName">역할 이름</label>
                                <input className="modal_input"
                                       id="partName" type="text"
                                       onChange={(e) => setPartName(e.target.value)}
                                       placeholder="역할 이름을 입력하세요"/>
                            </div>
                            <div className="modal_section">
                                <h4>담당자</h4>
                                <ul className="members-list">
                                    {participation.num > 0 ?
                                        (participation.participation.map((p) => (
                                            <li key={p.memberId} id={"part" + p.memberId}>
                                                <img src={p.profile} alt={`${p.nickname}'s profile`} />
                                                <p>{p.nickname}</p>
                                                <input
                                                    type="radio"
                                                    value={p.memberId}
                                                    checked={selectedMemberId === p.memberId}
                                                    onChange={() => setSelectedMemberId(p.memberId)}
                                                />
                                            </li>
                                        ))) : <h2> 참여자가 없습니다.</h2>
                                    }
                                </ul>
                            </div>
                            <button className="add-button" onClick={() => addPart()}>
                                생성
                            </button>
                        </div>
                    </div>
                </div>
            )}


        </main>
        <div className="process">
        <div onClick={() => goSection('/topic', `/sub/rooms/${roomId}/topics`)}>
            자료 조사
        </div>
        <div>자료 조사</div>
        <div>발표 자료</div>
        <div>발표 준비</div>
    </div>
    </>
    );
}
export default Part;