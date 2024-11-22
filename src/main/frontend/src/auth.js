import React, { useEffect, useState } from "react";
import { Navigate } from "react-router-dom";

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
        return <div>Loading...</div>;
    }

    return isAuthenticated ? children : <Navigate to="/login" />;
}

export default Auth;
