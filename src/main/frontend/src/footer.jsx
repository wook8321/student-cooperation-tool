import React from 'react';
import { Link } from 'react-router-dom';

// 이미지 경로들 import
import homeImage from './images/home.png';  // 예시 이미지 경로
import friendImage from './images/friends.svg'; // 예시 이미지 경로
import projectImage from './images/project-footer.svg'; // 예시 이미지 경로
import exitDoor from './images/exit.png'; // 예시 이미지 경로
import mainImage from './images/main.svg'

// handleClick 함수 정의
const handleClick = () => {
    alert("로그아웃 했습니다. 홈화면으로 돌아갑니다.");
    window.location.href = '/logout'; // /logout URL로 리디렉션
};

// Footer 컴포넌트 정의
const Footer = () => {
    return (
        <footer>
            <h2>Stool</h2>
            <Link to="/" title="메인">
                <img src={mainImage} alt="홈 화면" />
            </Link>
            <br />
            <Link to="/friend" title="친구">
                <img src={friendImage} alt="친구 이미지" />
            </Link>
            <br />
            <Link to="/project" title="프로젝트">
                <img src={projectImage} alt="프로젝트 이미지" />
            </Link>
            <Link className="exit-link" title="로그아웃">
                <img onClick={handleClick} src={exitDoor}/>
            </Link>
        </footer>
    );
};

export default Footer;