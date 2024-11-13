import React from 'react';
import axios from "axios";
import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { Client, IMessage } from "@stomp/stompjs";

const domain = "http://localhost:8080"

interface ChatMessageReqeust {
    from: string;
    text: string;
    roomId: number;
  }
  interface ChatMessageResponse{
    id: number;
    content: string;
    writer: string;
  }
  
  function ChatPage() {
    const { roomId } = useParams();
    const [stompClient, setStompClient] = useState<Client | null>(null);
    const [messages, setMessages] = useState<ChatMessageResponse[]>([]);
    const [writer, setWriter] = useState<string>("");
    const [newMessage, setNewMessage] = useState<string>("");
  
    useEffect(() => {
      const loadChatHistory = async () => {
        try {
          const response = await axios.get(
            `{$domain}/api/v1/rooms/${roomId}/chats`
          );
          const messages = response.data.data.messageList as ChatMessageResponse[];
          setMessages(messages);
        } catch (error) {
          console.error("채팅 내역 로드 실패", error);
        }
      };
  
      loadChatHistory();
      const client = new Client({
        brokerURL: "ws://localhost:8080/chat", // 서버 WebSocket URL
        reconnectDelay: 5000,
        onConnect: () => {
          client.subscribe(`/sub/rooms/${roomId}/chat`, (message: IMessage) => {
            const msg: ChatMessageResponse = JSON.parse(message.body);
            setMessages((prevMessages) => [...prevMessages, msg]);
          });
        },
      });
      client.activate();
      setStompClient(client);
      return () => {
        client.deactivate();
      };
    }, [roomId]);
  
    const sendMessage = () => {
      if (stompClient && newMessage) {
        const chatMessage: ChatMessageReqeust = {
          from: writer,
          text: newMessage,
          roomId: parseInt(roomId || ""),
        };
        stompClient.publish({
          destination: `/pub/chats/add`,
          body: JSON.stringify(chatMessage),
        });
        console.log(messages);
        setNewMessage("");
      }
    };
  
    const deleteMessage = () => {
      if (stompClient && newMessage) {
        const chatMessage: ChatMessageReqeust = {
          from: writer,
          text: newMessage,
          roomId: parseInt(roomId || ""),
        };
        stompClient.publish({
          destination: `/pub/chats/delete`,
          body: JSON.stringify(chatMessage),
        });
        console.log(messages);
      }
    };

    return (
      <div className="chat-container">
        <div>
          <Link to={"/api/v1/rooms/topics"} className="back-link">
            뒤로 가기
          </Link>
        </div>
        <div className="chat-messages">
          {messages.map((msg, idx) => (
            <div key={idx}>
              {msg.writer}: {msg.content}
            </div>
          ))}
        </div>
        <div className="input-group">
          <label>작성자</label>
          <input
            type="text"
            value={writer}
            onChange={(e) => setWriter(e.target.value)}
          />
        </div>
        <div className="input-group">
          <input
            type="text"
            value={newMessage}
            onChange={(e) => setNewMessage(e.target.value)}
          />
          <button className="send-button" onClick={sendMessage}>
            전송
          </button>

          <button className="delete-button" onClick={deleteMessage}>
            삭제
          </button>

        </div>
      </div>
    );
  }
  
  export default ChatPage;