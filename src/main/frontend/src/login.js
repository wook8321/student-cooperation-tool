import React, { useEffect } from 'react'
import "./App.css";
import { useNavigate, Link } from 'react-router-dom';
import { GoogleLogin, GoogleOAuthProvider } from '@react-oauth/google';

const GoogleLogIn = () => {

  const clientID = '937048003352-71pacbp3j1p6gp5lv5mv51mh0dbnvh0h.apps.googleusercontent.com';
  const navigate = useNavigate();

  // 로그인 성공했을 때 처리 함수
  const LoginSuccess = (response) => {
    console.log(response);
    navigate('/home');
  }

  //로그인 실패했을 때 처리 함수
  const LoginFail = (response) => {
    console.log(response);
  }

  // 로그아웃 성공했을 때 처리 함수
  const LogoutSuccess = () => {
    console.log('SUCCESS LOG OUT');
  };

  return (
    <>
      <div className='login_title'>
        STOOL
      </div>
      <React.Fragment>
        <GoogleOAuthProvider clientId={clientID}>
          <GoogleLogin
            clientId={clientID}
            className='Login'
            onSuccess={LoginSuccess}
            onFailure={LoginFail}
          />
        </GoogleOAuthProvider>
      </React.Fragment>
    </>
  )
}

export default GoogleLogIn