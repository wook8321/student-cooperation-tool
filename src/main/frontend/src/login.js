import React, { useEffect, useState } from "react";
import axios from "axios";
import { Link } from "react-router-dom";
import "./login.css";
import "./scrollbar.css";

const domain = "http://localhost:8080";

function Login() {
  const [hello, setHello] = useState("");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  // useEffect(() => {
  //   axios.get(domain + '/api/loginTest')
  //       .then(res => {
  //         setHello(JSON.stringify(res.data));
  //       })
  //       .catch(error =>{
  //           alert(JSON.stringify(error))
  //       })
  // }, []);

  const inPlatformLogin = async (e) => {
    e.preventDefault();

    // id, pw확인 코드 (id는 test, pw는 1234임.)
    if (username === "test" && password === "1234") {
      // 로그인 성공시. 이후에 Auth 바꾸는 코드 필요함!!!
      console.log('로그인 성공. 이후에 Auth 바꾸는 코드 필요해요')
    }
  }

  return (
    <div className="full">
      <div className="login">
        <h1>로그인</h1>

        <form>
          <div>
            <label htmlFor="username">아이디</label>
            <input
              type="text"
              id="username"
              name="username"
              placeholder="아이디를 입력하세요"
              value={username}
              onChange={(e) => setUsername(e.target.value)} // 입력 값 업데이트
              required
            />
          </div>
          <div>
            <label htmlFor="password">비밀번호</label>
            <input
              type="password"
              id="password"
              name="password"
              placeholder="비밀번호를 입력하세요"
              value={password}
              onChange={(e) => setPassword(e.target.value)} // 입력 값 업데이트
              required
            />
          </div>
          <button onClick={inPlatformLogin} >로그인</button>
        </form>

        <div className="Social-Login">
          <a
            className="btn btn-primary w-100 py-2 mb-1"
            href="/oauth2/authorization/google"
            type="button"
          >
            Google로 로그인
          </a>
        </div>
        <div className="Social-Logout">
          <form action="/logout" method="post">
            <button type={"submit"}> 로그아웃</button>
          </form>
        </div>
      </div>
    </div>
  );
}

export default Login;
