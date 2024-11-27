import React, { useState, useEffect, useRef } from "react";
import axios from "axios";
import { domain } from "./domain";
import "./ChatRoom.css";

function ChatRoom({ roomId }) {
    const [chatList, setChatList] = useState([]);
    const [page, setPage] = useState(0);
    const [inputMessage, setInputMessage] = useState("");
    const chatRef = useRef(null);
    const [isShowDownBtn, setIsShowDownBtn] = useState(false);
    const [lastMessageId, setLastMessageId] = useState(null);
    const [userId, setUserId] = useState(null);
    const [newMessage, setNewMessage] = useState(null); // 새 메시지 정보
    const [scrollDown, setScrollDown] = useState(false);

    let prevScrollHeight = 0;

    const userFetch = async () => {
        try {
            const res = await axios.get(`${domain}/api/user-info`);
            setUserId(res.data);
            console.log(res.data);
        } catch (error) {
            console.error("유저 정보를 가져오는 데 실패했습니다.", error);
        }
    };

    const chatFetch = async () => {
        try {
            const res = await axios.get(`${domain}/api/v1/rooms/${roomId}/chats`, {
                params: {
                    page,
                    lastMessageId,
                },
            });
            const newData = res.data.data.chats.reverse();
            if (newData.length > 0) {
                setLastMessageId(newData[0].id);
                setChatList((prev) => [...newData, ...prev]);
                setPage((prevPage) => prevPage + 1);
            }
            console.log(newData);
        } catch (error) {
            console.error("채팅 내역을 불러오는데 실패했습니다.", error);
        }
    };

    const scrollDownButton = () => {
        if (chatRef.current) {
            chatRef.current.scrollTop = chatRef.current.scrollHeight;
        }
    }
    const onScroll = () => {
        const messageRef = chatRef.current;
        const scrollTop = messageRef.scrollTop;

        //스크롤 최상단일 때 추가로 chat 목록 가져옴
        if (scrollTop === 0) {
            chatFetch();
            prevScrollHeight = messageRef.scrollHeight;
        }

        //스크롤이 일정 길이만큼 올라갔을 때 내려가는 버튼 표시
        if (messageRef.scrollHeight - scrollTop <= messageRef.clientHeight + 50) {  // 50은 여유 공간
            setIsShowDownBtn(false);
        } else {
            setIsShowDownBtn(true);
        }
    };

    const handleSendMessage = () => {
        if (inputMessage.trim() === "") return;
        const newMessage = {
            id: chatList.length + 1,
            nickName: "horit",
            content: inputMessage,
            userId: 123,
        };
        setChatList((prev) => [...prev, newMessage]);
        setInputMessage("");

        if (newMessage.userId === userId) {
           setScrollDown((prev) => !prev);
        } else {
            // 본인이 아니면, 스크롤이 맨 아래에 있지 않으면 알림을 보여줌
            const messageRef = chatRef.current;
            if (messageRef && messageRef.scrollTop + messageRef.clientHeight < messageRef.scrollHeight) {
                setNewMessage(newMessage); // 새 메시지 알림 표시
            }
        }

    };

    const handleNewMessageClick = () => {
        scrollDownButton(); // 알림 클릭 시 스크롤을 맨 아래로 이동
        setNewMessage(null); // 알림 숨기기
    };

    const handleKeyPress = (e) => {
        if (e.key === "Enter") {
            handleSendMessage();
        }
    };

    useEffect(() => {
        userFetch();
        chatFetch();
        setScrollDown((prev) => !prev);
    }, [roomId]);

    useEffect(() => {
        if (chatRef.current) {
            chatRef.current.scrollTop = chatRef.current.scrollHeight;
        }
    }, [scrollDown]);


    return (
        <div className="chat-container">
            <div className="chat-header">채팅방</div>
            <div
                ref={chatRef}
                onScroll={onScroll}
                className="chat-messages"
            >
                {chatList.length > 0 ? (
                    chatList.map((chat) => (
                        <ChatMessage
                            key={chat.id}
                            message={chat}
                            isMine={userId === chat.userId}
                        />
                    ))
                ) : (
                    <div style={{ textAlign: "center", color: "#999" }}>채팅이 없습니다.</div>
                )}

                {newMessage && !newMessage.userId === userId && (
                    <div
                        className="new-message-notification"
                        style={{
                            position: "absolute",
                            top: "10px",
                            left: "50%",
                            transform: "translateX(-50%)",
                            backgroundColor: "#ff9800",
                            padding: "10px",
                            borderRadius: "20px",
                            color: "white",
                            cursor: "pointer",
                        }}
                        onClick={handleNewMessageClick}
                    >
                        새로운 메시지: {newMessage.nickName} - {newMessage.content}
                    </div>
                )}

                {isShowDownBtn && (
                    <button
                        onClick={scrollDownButton}
                        className="scroll-down-button"
                    >
                        ▼
                    </button>
                )}
            </div>
            <div className="chat-input-container">
                <input
                    type="text"
                    value={inputMessage}
                    onChange={(e) => setInputMessage(e.target.value)}
                    onKeyPress={handleKeyPress}
                    className="chat-input"
                    placeholder="메시지를 입력하세요"
                />
                <button onClick={handleSendMessage} className="chat-send-button">
                    전송
                </button>
            </div>
        </div>
    );
}

function ChatMessage({ message, isMine }) {
    return (
        <div
            className={`chat-message-container ${isMine ? "chat-message-mine" : "chat-message-other"}`}
        >
            {!isMine && <div className="chat-author">{message.nickName}</div>}
            <div className="chat-message">{message.content}</div>
        </div>
    );
}

export default ChatRoom;
