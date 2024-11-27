import React, { useState, useEffect, useRef } from "react";
import axios from "axios";
import { domain } from "./domain";

function ChatRoom({ roomId }) {
    const [chatList, setChatList] = useState([]);
    const [page, setPage] = useState(0);
    const [inputMessage, setInputMessage] = useState('');
    const chatRef = useRef(null);
    const [isShowDownBtn, setIsShowDownBtn] = useState(false);
    const [isNewMessage, setIsNewMessage] = useState(false);
    const [lastMessageId, setLastMessageId] = useState(null);
    const [userId, setUserId] = useState(null);

    const userFetch = () => {
        axios.get(`${domain}/api/user-info`)
            .then((res) =>{
                setUserId(res.data.data);
            })
            .catch((e) =>{
            console.log(e)
            })
    }

    const chatFetch = async () => {
        try {
            const res = await axios.get(`${domain}/api/v1/rooms/${roomId}/chats`, {
                params: {
                    page,
                    lastMessageId
                },
            });
            const newData = res.data.data.chats.reverse();
            const currentScrollTop = chatRef.current ? chatRef.current.scrollTop : 0;
            const currentScrollHeight = chatRef.current ? chatRef.current.scrollHeight : 0;

            if(newData.length > 0) {
                setLastMessageId(newData[0].id)
                setChatList((prev) => [...newData, ...prev]);
                setPage((prevPage) => prevPage + 1);

                if (chatRef.current) {
                    const newScrollHeight = chatRef.current.scrollHeight;
                    chatRef.current.scrollTop = currentScrollTop + (newScrollHeight - currentScrollHeight);
                }
            }
        } catch (error) {
            console.error("채팅 내역을 불러오는데 실패 했습니다.", error);
        }
    };

    const scrollBottom = () => {
        if (chatRef.current) {
            chatRef.current.scrollTop = chatRef.current.scrollHeight;
        }
    };

    let prevScrollHeight = 0;

    const onScroll = () => {
        const messageRef = chatRef.current;
        const scrollTop = messageRef.scrollTop;

        if (scrollTop === 0) {
            chatFetch();
            prevScrollHeight = messageRef.scrollHeight;
        }

        if (prevScrollHeight - messageRef.scrollTop > messageRef.clientHeight + 100) {
            setIsShowDownBtn(true);
        } else {
            setIsShowDownBtn(false);
        }
    };

    const handleSendMessage = () => {
        if (inputMessage.trim() === '') return;

        const newMessage = {
            id: chatList.length + 1,
            nickName: "나",
            content: inputMessage,
            userId: userId,
        };

        setChatList((prev) => [...prev, newMessage]);
        setInputMessage('');
        setIsNewMessage(true);
    };

    const handleKeyPress = (e) => {
        if (e.key === 'Enter') {
            handleSendMessage();
        }
    };

    useEffect(() => {
        const messageRef = chatRef.current;
        if (isNewMessage) {
            if (prevScrollHeight - messageRef.scrollTop > messageRef.clientHeight + 10) {
                setIsShowDownBtn(true);
            } else {
                scrollBottom();
            }
        }
        prevScrollHeight = messageRef.scrollHeight;
        setIsNewMessage(false);
    }, [chatList, isNewMessage]);

    useEffect(() => {
        userFetch();
        chatFetch().then(() => {
            setTimeout(scrollBottom, 0);
        });
    }, [roomId]);

    return (
        <div className="chat-container" style={{
            width: "350px",
            border: "1px solid #d3d3d3",
            borderRadius: "10px",
            overflow: "hidden",
            backgroundColor: "#f0f0f0",
            display: "flex",
            flexDirection: "column",
            height: "500px",
            boxShadow: "0 4px 6px rgba(0,0,0,0.1)"
        }}>
            <div className="chat-header" style={{
                backgroundColor: "#e0e0e0",
                padding: "10px",
                textAlign: "center",
                borderBottom: "1px solid #d3d3d3",
                fontWeight: "bold",
                color: "#333"
            }}>
                채팅방
            </div>

            <div
                ref={chatRef}
                onScroll={onScroll}
                className="chat-messages"
                style={{
                    flexGrow: 1,
                    overflowY: "auto",
                    padding: "10px",
                    display: "flex",
                    flexDirection: "column",
                    gap: "10px",
                    backgroundColor: "#f5f5f5"
                }}
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
                    <div style={{
                        textAlign: "center",
                        color: "#999",
                        marginTop: "50%"
                    }}>
                        채팅이 없습니다.
                    </div>
                )}

                {isShowDownBtn && (
                    <button
                        onClick={scrollBottom}
                        style={{
                            position: "sticky",
                            bottom: "10px",
                            alignSelf: "flex-end",
                            backgroundColor: "#007bff",
                            color: "white",
                            border: "none",
                            borderRadius: "20px",
                            padding: "5px 10px",
                            cursor: "pointer"
                        }}
                    >
                        최신 메시지 보기
                    </button>
                )}
            </div>

            <div className="chat-input-container" style={{
                display: "flex",
                padding: "10px",
                backgroundColor: "#e0e0e0",
                borderTop: "1px solid #d3d3d3"
            }}>
                <input
                    type="text"
                    value={inputMessage}
                    onChange={(e) => setInputMessage(e.target.value)}
                    onKeyPress={handleKeyPress}
                    placeholder="메시지를 입력하세요"
                    style={{
                        flexGrow: 1,
                        padding: "10px",
                        borderRadius: "20px",
                        border: "1px solid #d3d3d3",
                        marginRight: "10px",
                        backgroundColor: "white",
                        color: "#333"
                    }}
                />
                <button
                    onClick={handleSendMessage}
                    style={{
                        backgroundColor: "#007bff",
                        color: "white",
                        border: "none",
                        borderRadius: "20px",
                        padding: "10px 15px",
                        cursor: "pointer"
                    }}
                >
                    전송
                </button>
            </div>
        </div>
    );
}

function ChatMessage({ message, isMine }) {
    return (
        <div style={{
            display: "flex",
            justifyContent: isMine ? "flex-end" : "flex-start",
            alignItems: "center"
        }}>
            {!isMine && (
                <div style={{
                    marginRight: "10px",
                    fontWeight: "bold",
                    fontSize: "0.8em",
                    color: "#666"
                }}>
                    {message.nickName}
                </div>
            )}
            <div style={{
                backgroundColor: isMine ? "#007bff" : "#ffffff",
                color: isMine ? "white" : "#333",
                padding: "10px",
                borderRadius: "15px",
                maxWidth: "70%",
                wordBreak: "break-word",
                boxShadow: "0 2px 4px rgba(0,0,0,0.1)"
            }}>
                {message.content}
            </div>
        </div>
    );
}

export default ChatRoom;