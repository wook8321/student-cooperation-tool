import React, { createContext, useContext, useRef, useState, useEffect } from 'react';
import {useLocation} from 'react-router-dom';// 방 id를 받아오기 위해 선언한 import
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { domain } from './domain';

const WebSocketContext = createContext();

export const useWebSocket = () => {
    return useContext(WebSocketContext);
};

export const WebSocketProvider = ({ children }) => {
    const [isConnected, setIsConnected] = useState(false);
    const stompClient = useRef(null);
    const location = useLocation();
    const { roomId, subUrl, userId, leaderId, presentationId } = location.state || {};
    const onStompError = (error) => {
        //2-2 연결 실패의 경우
        alert("방 입장에 실패하였습니다.");
        console.error("STOMP Error", error);
        window.location.href = "/";
    }

    useEffect(() => {
        stompClient.current = new Client({
            webSocketFactory: () => new SockJS(`${domain}/ws-stomp`),
            connectHeaders: {
                SubscribeUrl : subUrl // 어디에 구독할 지 헤더에 담아서 보냄
            },
            reconnectDelay: 5000,
            onConnect: () => setIsConnected(true),
            onStompError,
        });

        stompClient.current.activate();

        return () => {
            if (stompClient.current) {
                stompClient.current.deactivate();
            }
        };
    }, []);

    return (
        <WebSocketContext.Provider value={{ stompClient, isConnected, roomId, userId, leaderId, presentationId}}>
            {children}
        </WebSocketContext.Provider>
    );
};