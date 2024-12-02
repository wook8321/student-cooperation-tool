import React, { useEffect, useState } from "react";
import "./main.css";
import Footer from "./footer";
import "./juaFont.css"
import mainlogo from "./images/mainlogo.png"


const Main = () => {
    const goGoogleLoginScreen = () => {
        window.location.href = "/oauth2/authorization/google"; // /logout URL로 리디렉션
    }

    return (
        <div className="container">
            <main className="main-page">
                <img src={mainlogo} className="under-logo"/>
                <img src={mainlogo} className="main-logo"/>
                <div className="text-wrapper">
                <h1 className="main-title jua-regular">STOOL에 오신 것을 환영합니다!</h1>
                <p className="main-description jua-regular">
                    저희는 보다 간편한 협업툴을 지향합니다.<br />
                    여러분들에게 보다 간편하고 즐거운 협업을 제공하는 것이 저희의 목적입니다.
                </p>
                    <button onClick={() => goGoogleLoginScreen()} className="gradient-button">Let's Go!</button>
                </div>
            </main>
           <Footer/>
        </div>
    );
};

export default Main;
