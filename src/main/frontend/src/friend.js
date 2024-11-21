import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import axios from "axios";
import friendImage from "./images/friends.svg";
import projectImage from "./images/archive.svg";
import userImage from "./images/user.svg";
import searchIcon from "./images/search.svg";
import homeImage from "./images/home.png";
import "./friend.css";

const domain = "http://localhost:8080";

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
        <h3>친구 목록</h3>
        {friends.num > 0 ? (
          <ul>
            {friends.members.map(friend => (
                <li key={friend.email}>
                  <div className="profile-icon">
                    <img src={friend.profile || userImage} alt="프로필" />
                  </div>
                  <span className="friend-name">{friend.nickname}</span>
                </li>)
            )}
          </ul>
        ) : <h2>아직 친구가 없습니다.</h2>}
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

  const handleAddFriend = (email) => {
      axios
        .post(`${domain}/api/v1/friends`, {
          email
        },  { "Content-Type": "application/json"},)
        .then((res) => {
          console.log("Friend added:", res.data);
          handleCloseModal(); // 친구 추가 후 모달 닫기
        })
        .catch(() => {
          console.log("Failed to add friend.");
        });
  };

  const handleCloseModal = () => {
    setModalOpen(false);
    setFriendData(null); // 모달창 닫을 때 데이터 초기화
  };

  return (
    <div className="container">
      <main>
        <form className="search_box" onSubmit={(e) => e.preventDefault()}>
          <input
            className="friend_search_txt"
            type="text"
            placeholder="친구 이름을 입력하세요."
            value={searchText}
            onChange={(e) => setSearchText(e.target.value)}
          />
          <button
            className="search_button"
            type="submit"
            onClick={handleSearchClick}
          >
            <img src={searchIcon} alt="검색" />
          </button>
        </form>
        <FriendsList /> {/* 친구 목록 표시 */}
      </main>

      <footer>
        <Link to="/">
          <img src={homeImage} />
        </Link>
        <br></br>
        <Link to="/friend">
          <img src={friendImage} alt="친구 이미지" />
        </Link>
        <br />
        <Link to="/project">
          <img src={projectImage} alt="프로젝트 이미지" />
        </Link>
      </footer>

      {modalOpen && (
        <div className="modal">
          <div className="modal_overlay" onClick={handleCloseModal}></div>
          <div className="modal_content">
            <h3>검색 결과</h3>
            <button className="close_button" onClick={handleCloseModal}>
              X
            </button>
            <div className="friend_result">
                {friendData.num > 0 ? (
                    <ul>
                      {friendData.members.map(friend => (
                          <li key={friend.email}>
                            <div className="profile-icon">
                              <img src={friend.profile || userImage} alt="프로필"/>
                            </div>
                            <span className="friend-name">{friend.nickname}</span>
                            <button className="add_friend_button" onClick={() => handleAddFriend(friend.email)}>
                              친구 추가
                            </button>
                          </li>)
                      )}
                    </ul>)
                    : (
                        <p>검색 결과가 없습니다.</p>)}
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Friend;