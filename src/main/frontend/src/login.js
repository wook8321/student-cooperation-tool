import React, {useEffect, useState} from "react";
import axios from "axios";
import { Link } from 'react-router-dom';

const domain = "http://localhost:8080"

function Login() {
  const [hello, setHello] = useState('');

  useEffect(() => {
    axios.get(domain + '/api/loginTest')
        .then(res => {
          setHello(JSON.stringify(res.data));
        })
        .catch(error =>{
            alert(JSON.stringify(error))
        })
  }, []);

  return (
      <div>
        <div className="Social-Login">
          <a className="btn btn-primary w-100 py-2 mb-1" href="/oauth2/authorization/google" type="button">Google로 로그인</a>
        </div>
        <div className="Social-Logout">
          <form action="/logout" method="post">
            <button type={"submit"}> 로그아웃</button>
          </form>
        </div>
          {/*로그아웃은 post 방식으로 <form>테그로 csrf 토큰과 함께 요청하면 됨*/}
      </div>
  );
}

export default Login;