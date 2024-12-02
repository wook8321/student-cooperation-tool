import React, { useEffect, useState } from "react";
import axios from "axios";
import searchIcon from "./images/search.svg";
import personHeart from "./images/PersonHearts.svg"
import friendship from "./images/friendship.svg"
import {domain} from "./domain";
import "./friend.css";
import "./scrollbar.css"
import "./bar.css"
import "./juaFont.css"
import Footer from "./footer";
import mainlogo from "./images/mainlogo.png";


const FriendsList = () => {
    const [friends, setFriends] = useState({num: 0, members: []});
    useEffect(() => {
        axios
            .get(domain + "/api/v1/friends")
            .then((res) => {
                console.log(res.data);
                setFriends(res.data.data);
            })
            .catch(() => {
                console.log("failed to load friends");
            });
    }, []);

    return (
            <div className="friend_list">
                <div id="newFriendDiv" className="newFriend-container"></div>
                <div id="barDiv"></div>
                <h2 id="friendsListH"></h2>
                {friends.num > 0 ? (
                    <ul>
                        <div className="friends-li">
                        <h2>친구 목록</h2>
                        <div className="friends-card">
                        {friends.members.map(friend => (
                            <li key={friend.email}>
                                <div className="profile-icon">
                                    <img src={friend.profile} alt="프로필"/>
                                </div>
                                <span className="friend-name">{friend.nickname}</span>
                            </li>)
                        )}
                        </div>
                        </div>
                    </ul>
                ) : <h1 style={{textAlign : "center", width: "1000px"}} id="notExistH">
                    <div>
                        <img src={friendship} height="300" width="300"/>
                    </div>
                    아직 등록된 친구가 없네요. 친구들을 찾아 볼까요?
                </h1>}
        </div>
    );
};

const Friend = () => {
    const [searchText, setSearchText] = useState("");
    const [modalOpen, setModalOpen] = useState(false);
    const [friendData, setFriendData] = useState({num:0, members:[]})
    const handleSearchClick = () => {
        axios
            .get(domain + `/api/v1/friends/search?relation=false&name=${searchText}`)
            .then((res) => {
                console.log(res.data.data);
                setFriendData(res.data.data);
                setModalOpen(true);
            })
            .catch(() => {
                console.log("Failed to search friend.");
                setModalOpen(true);
            });
    };

    const handleAddFriend = (email, profile, nickname) => {
        axios
            .post(`${domain}/api/v1/friends`, {
                email
            },  { "Content-Type": "application/json"},)
            .then((res) => {
                console.log("Friend added:", res.data);
                createFriendDiv(email,profile, nickname);

                setFriendData((prevData) => ({
                    ...prevData,
                    members: prevData.members.filter(friend => friend.email !== email),
                    num: prevData.num - 1,
                }));
            })
            .catch(() => {
                console.log("Failed to add friend.");
            });
    };

    function createFriendDiv(email,profile, nickname){
        const newFriendDiv = document.getElementById('newFriendDiv')
        if(newFriendDiv.querySelector('h2') === null){
            newFriendDiv.innerHTML += `<h2>새로운 친구</h2>`
        }
        const barDiv = document.querySelector('#barDiv');
        const notExistH = document.querySelector('#notExistH');

        if (notExistH) {
            //친구가 없는 상태일 때 친구를 사겼을 경우
            let friendsListH = document.querySelector('#friendsListH')
            friendsListH.parentNode.removeChild(friendsListH)
            notExistH.parentNode.removeChild(notExistH)
        } else{
            barDiv.setAttribute("class","divider-bar")
        }
        // 새롭게 생긴 친구를 삽입
        newFriendDiv.innerHTML +=
            `
            <li key=${email}>
              <div class="profile-icon">
                <img src = ${profile} alt="프로필"/>
              </div>
              <span class="friend-name">${nickname}</span>
            </li>
          `
    }

    const handleCloseModal = () => {
        setModalOpen(false);
        setFriendData(null); // 모달창 닫을 때 데이터 초기화
    };

    return (
        <div className="container">
            <div className="friend-main">
                <img src={mainlogo} className="under-logo"/>
                <form className="search_box" onSubmit={(e) => e.preventDefault()}>
                    <input className="friend_search_txt" type="text" placeholder="친구 이름을 입력하세요." value={searchText}
                           onChange={(e) => setSearchText(e.target.value)}/>
                    <button
                        className="search_button"
                        type="submit"
                        onClick={handleSearchClick}
                    >
                        <img src={searchIcon} alt="검색"/>
                    </button>
                </form>
                <div className="friends-list-wrapper">
                    <FriendsList/> {/* 친구 목록 표시 */}
                </div>
            </div>
            <Footer/>

            {modalOpen && (

                <div className="modal_overlay" onClick={handleCloseModal}>
                    <div className="modal_content" onClick={(e) => e.stopPropagation()} >

                        <h3>검색 결과</h3>
                        <button className="close_button" onClick={handleCloseModal}>
                            X
                        </button>
                        <div className="friend_result scrollbar">
                            {friendData.num > 0 ? (
                                    <ul>
                                        {friendData.members.map(friend => (
                                            <li key={friend.email}>
                                                <div className = "profile">
                                                    <div className="profile-icon">
                                                        <img src={friend.profile} alt="프로필"/>
                                                    </div>
                                                    <span className="friend-name">{friend.nickname}</span>
                                                    <button className="add_friend_button" onClick={() => handleAddFriend(friend.email,friend.profile,friend.nickname)}>
                                                        친구 추가
                                                    </button>
                                                </div>
                                            </li>)
                                        )}
                                    </ul>)
                                : (
                                    <p > 해당하는 친구가 없어요. 다시 확인하고 입력해주세요.</p>)}
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default Friend;