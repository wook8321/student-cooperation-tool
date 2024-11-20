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
  const [friends, setFriends] = useState([]);

  useEffect(() => {
    axios
      .get(domain + "/api/v1/friends")
      .then((res) => {
        setFriends(res.data);
      })
      .catch(() => {
        console.log("failed to load friends");
      });
  }, []);

  const exFriend = [
    {
      id: "test1",
      name: "person1",
      profileImage: "https://example.com/image1.jpg",
    },
    {
      id: "test2",
      name: "person2",
      profileImage: "https://example.com/image2.jpg",
    },
    {
      id: "test3",
      name: "person3",
      profileImage: "https://example.com/image3.jpg",
    },
    {
      id: "test4",
      name: "person4",
      profileImage: "https://example.com/image4.jpg",
    },
  ];
  return (
    <div>
      <h3>친구 목록</h3>
      <ul className="friend_list">
        {exFriend.map((friend) => (
          <li key={friend.id} className="friend_item">
            <div className="profile-icon">
              <img
                src={friend.profileImage}
                alt="프로필"
                onError={(e) => (e.target.src = userImage)}
              />
            </div>
            <span className="friend-name">{friend.name}</span>
          </li>
        ))}
      </ul>
    </div>
  );
};

const Friend = () => {
  const [searchText, setSearchText] = useState("");
  const [modalOpen, setModalOpen] = useState(false);
  const [friendData, setFriendData] = useState(null);

  const handleSearchClick = () => {
    axios
      .get(domain + `/api/v1/friends/search?relation=false&name=${searchText}`)
      .then((res) => {
        setFriendData(res.data);
        setModalOpen(true);
      })
      .catch(() => {
        console.log("Failed to search friend.");
        setModalOpen(true);
      });
  };

  const handleAddFriend = () => {
    if (friendData && friendData.id) {
      axios
        .post(`${domain}/api/v1/friends/add`, {
          /* memberId: 현재 사용자의 memberId */
          friendId: friendData.id,
        })
        .then((res) => {
          console.log("Friend added:", res.data);
          handleCloseModal(); // 친구 추가 후 모달 닫기
        })
        .catch(() => {
          console.log("Failed to add friend.");
        });
    }
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
            {friendData ? (
              <div className="friend_result">
                <p>{friendData.name}</p>
                <button className="add_friend_button" onClick={handleAddFriend}>
                  친구 추가 +
                </button>
              </div>
            ) : (
              <p>검색 결과가 없습니다.</p>
            )}
          </div>
        </div>
      )}
    </div>
  );
};

export default Friend;
