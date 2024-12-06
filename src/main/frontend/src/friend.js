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
                setFriends(res.data.data);
            })
            .catch(() => {
                console.log("failed to load friends");
            });
    }, []);

    const handleDeleteFriend = (email) => {
        axios.delete(domain + "/api/v1/friends", {data: {email}})
            .then(()=>{
                setFriends(prev => (
                    {num: prev.num-1, members: prev.members.filter(member => member.email !== email)}))
            })
            .catch(()=>{
                console.log("failed to delete friend")
            })
    }

    return (
            <div className="friend_list">
                <div id="newFriendDiv" className="newFriend-container"></div>
                <div id="barDiv"></div>
                <h2 id="friendsListH"></h2>
                {friends.num > 0 ? (
                    <ul>
                        <div className="friends-li">
                        <h2>친구 목록 ({friends.num})</h2>
                        <div className="friends-card">
                        {friends.members.map(friend => (
                            <li key={friend.email}>
                                <div className="profile-icon">
                                    <img src={friend.profile} alt="프로필"/>
                                </div>
                                <span className="friend-name">{friend.nickname}</span>
                                <button className="delete-friend-button"
                                        onClick={() => handleDeleteFriend(friend.email)}>
                                    삭제
                                </button>
                            </li>)
                        )}
                        </div>
                        </div>
                    </ul>
                ) : <h1 style={{textAlign: "center", width: "1000px"}} id="notExistH">
                    <div>
                    <img src={friendship} height="300" width="300" style={{marginTop: "20px"}}/>
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
    const [excludeList, setExcludeList] = useState([]);
    const handleSearchClick = () => {
        axios
            .get(domain + `/api/v1/friends/search?relation=false&name=${searchText}`)
            .then((res) => {
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
        // 새 친구 요소 생성
        const listItem = document.createElement('li');
        listItem.setAttribute('key', email);

        // 프로필 div 생성
        const profileDiv = document.createElement('div');
        profileDiv.className = 'profile-icon';

        const profileImg = document.createElement('img');
        profileImg.src = profile;
        profileImg.alt = "프로필";
        profileDiv.appendChild(profileImg);

        // 닉네임 span 생성
        const nicknameSpan = document.createElement('span');
        nicknameSpan.className = 'friend-name';
        nicknameSpan.textContent = nickname;

        // 삭제 버튼 생성
        const deleteButton = document.createElement('button');
        deleteButton.className = 'delete-friend-button';
        deleteButton.textContent = "삭제";
        deleteButton.onclick = () => handleDeleteFriend(email); // 이벤트 리스너 연결

        // 요소 조립
        listItem.appendChild(profileDiv);
        listItem.appendChild(nicknameSpan);
        listItem.appendChild(deleteButton);

        // 새 친구 추가
        newFriendDiv.appendChild(listItem);
    }

    const handleDeleteFriend = (email) => {
        axios.delete(domain + "/api/v1/friends", {data: {email}})
            .then(()=>{
                removeFriendDiv(email);
            })
            .catch(()=>{
                console.log("failed to delete friend")
            })
    }

    function removeFriendDiv(email){
        const newFriendDiv = document.getElementById('newFriendDiv');
        const friendToRemove = newFriendDiv.querySelector(`li[key="${email}"]`)
        friendToRemove.remove();

        const remainingFriends = newFriendDiv.querySelectorAll(`li`);
        if(remainingFriends.length === 0){
            const title = newFriendDiv.querySelector(`h2`);
            if(title){
                newFriendDiv.removeChild(title);
            }
        }
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
                    <div className="friend_modal_content" onClick={(e) => e.stopPropagation()} >

                        <h3>검색 결과</h3>
                        <button className="close_button" onClick={handleCloseModal}>
                            X
                        </button>
                        <div className="friend_result scrollbar">
                            {friendData.num > 0 ? (
                                    <ul>
                                        {friendData.members.map(friend => (
                                            <li key={friend.email}>
                                                <div className = "friend_profile">
                                                    <div className="profile-icon">
                                                        <img src={friend.profile} alt="프로필"/>
                                                    </div>
                                                    <span className="friend-name">{friend.nickname}</span>
                                                    <button className="add_friend_button" onClick={() => handleAddFriend(friend.email,friend.profile,friend.nickname)}>
                                                        +
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