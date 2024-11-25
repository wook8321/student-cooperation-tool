import React, { useEffect, useState } from "react";
import { Navigate } from "react-router-dom";
import "./loading.css"
import Footer from "./footer";

function Auth({ children }) {
    const [isAuthenticated, setIsAuthenticated] = useState(null); // 초기 상태는 null (확인 전)

    useEffect(() => {
        fetch("/api/auth/check", { credentials: "include" })
            .then((response) => {
                if (response.ok) {
                    setIsAuthenticated(true);
                } else {
                    setIsAuthenticated(false);
                }
            })
            .catch(() => setIsAuthenticated(false));
    }, []);

    if (isAuthenticated === null) {
        // 인증 상태를 확인 중일 때 로딩 상태 표시
        return   <div className="loading">
                    <div className="loading-container">
                        <div className="spinner"></div>
                        <p>로딩 중...</p>
                    </div>
                 </div>;
    }

    if(isAuthenticated === false){
        alert("로그인을 하지않아 메인화면으로 행합니다")
        return <Navigate to="/" />
    } else if(isAuthenticated === true){
        return children;
    }
}

export default Auth;
