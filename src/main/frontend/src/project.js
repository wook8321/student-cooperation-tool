import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import axios from "axios";
import friendImage from "./images/friends.svg";
import projectImage from "./images/archive.svg";
import homeImage from "./images/home.png";
import userImage from "./images/user.svg"
import searchImage from "./images/search.svg";
import "./project.css";

const domain = "http://localhost:8080";

const RoomList = () => {
  const [rooms, setRooms] = useState({num: 0, rooms: []});
  const handleDeleteRoom = () => {
    axios
        .delete(`${domain}/api/v1/rooms`)
        .then(() => {
          console.log("Successed to delete room");
        })
        .catch(() => {
          console.log("Failed to delete room");
        });
  };
  useEffect(() => {
    axios
        .get(domain + "/api/v1/rooms?page=0")
        .then((res) => {
          setRooms(res.data.data);
          console.log(rooms);
        })
        .catch(() => {
          console.log("failed to load rooms");
        });
  }, []);

  return (
      <div className="room_list">
          <div id="newRoomDiv"></div>
        <h3>방 목록</h3>
        {rooms.num > 0 ? (
            rooms.rooms.map((room) => (
                <div key={room.roomId} className="room_card">
                  <h4>{room.title}</h4>
                  <button onClick={() => handleDeleteRoom(room.roomId)}>X</button>
                  <div className="process_flow">
                    <div className="process_step">주제선정</div>
                    <div className="arrow">→</div>
                    <div className="process_step">자료 조사</div>
                    <div className="arrow">→</div>
                    <div className="process_step">발표 자료</div>
                    <div className="arrow">→</div>
                    <div className="process_step">발표 준비</div>
                  </div>
                </div>
            ))
        ) : <h2>프로젝트가 없습니다.</h2>}
      </div>
  );
};

const Project = () => {
  const [createmodal, setCreateModal] = useState(false);
  const [roomData, setRoomData] = useState({num:0, rooms:[]});
  const [result, setResult] = useState({num:0, members:[]}); // 초대할 친구 정보
  const [participant, setParticipant] = useState({num:0, members:[]}); // 이미 초대된 친구들 정보
  
  const [searchTitle, setSearchTitle] = useState("");
  const [searchFriend, setSearchFriend] = useState("");
  const [roomTitle, setRoomTitle] = useState("");

  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);

  const [searchModal, setSearchModal] = useState(false);
  const [enterModal, setEnterModal] = useState(false);
  const [friendModal, setFriendModal] = useState(false);
  const [password, setPassword] = useState("");
  const [inputPassword, setInputPassword] = useState("");
  const [error, setError] = useState(false);
  const [searchFriendModal, setSearchFriendModal] = useState(false);
  const handleSearch = ({ searchTitle }) => {
    axios
      .get(`${domain}/api/v1/rooms/search?title=${searchTitle}&page=0`)
      .then((res) => {
        setRoomData(res.data.data);
        setSearchModal(true);
      })
      .catch(() => {
        console.log("Failed to search project.");
      });
  };

  const handleCreateClick = () => {
    axios
      .post(`${domain}/api/v1/rooms`,  {
          title : roomTitle,
          password,
          participation : participant.members.map((member) => member.id)
      })
      .then((res) => {
          const updatedRoom = res.data.data;
        console.log("Successed to create project.")
        closeCreateModal();
          createRoomDiv(updatedRoom);
      })
      .catch(() => {
        console.log("Failed to create project.");
      });
  };

    function createRoomDiv(updatedRoom){
        const newRoomDiv = document.getElementById('newRoomDiv')
        if(newRoomDiv.querySelector('h2') === null){
            newRoomDiv.innerHTML += `<h2>새로운 프로젝트</h2>`
        }
        const roomCard = document.createElement('li');
        roomCard.setAttribute('key', updatedRoom.roomId);
        roomCard.className = 'room_card';
        roomCard.innerHTML = `<h4>${updatedRoom.title}</h4>
        <button>X</button> `;

        const deleteButton = roomCard.querySelector('button');
        deleteButton.addEventListener('click', () => handleDeleteRoom(updatedRoom.roomId));

        newRoomDiv.appendChild(roomCard);
    }


    const closeCreateModal = () => {
    setCreateModal(false);
    setRoomData(null);
  };

  const closeSearchModal = () => {
    setSearchModal(false);
    setRoomData(null);
  }

  const closeSearchFriendModal = () => {
      setSearchFriendModal(false);
      setSearchFriend(null);
  }

  const handleDeleteRoom = (roomId) => {
    axios
      .delete(`${domain}/api/v1/rooms`, {
          data: {
              roomId,
          },
      })
      .then(() => {
        console.log("Successed to delete room");
      })
      .catch(() => {
        console.log("Failed to delete room");
      });
  };

  const handlePasswordCheck = () => {
    if (inputPassword === password) {
      <Link to="/subject"></Link>;
    } else {
      setError(true);
    }
  };

  const handleFriendList = () => {
    axios.get(`${domain}/api/v1/friends`)
        .then((res) => {
          setResult(res.data.data);
          setFriendModal(true);
        })
        .catch(() => {
          console.log("Failed to list friend")
        })

        {
          result.num > 0 ? (
              result.members.map((result) => (
                  <div key={result.email} className="friend_card">
                    <img src={result.profile || userImage} alt="프로필"/>
                    <h2>{result.nickname}</h2>
                    <button onClick={() => addResult(result.id, result.email, result.nickname, result.profile)}> 초대</button>
                    <button onClick={() => setFriendModal(false)}>X</button>
                  </div>
              ))
          ) : <h2>친구가 없습니다.</h2>
        }
  }
  /* 참여할 유저 ( 친구 상태 ) 검색 */
  const handleFriend = ({name}) => {
    axios.get(`${domain}/api/v1/friends/search?relation=true&name=${name}`)
        .then((res) => {
          setResult(res.data.data);
          setSearchFriendModal(true);
        })
        .catch(() => {
          console.log("Failed to search friend");
        });

    return (
        <div className="participant_grid">
          {result.num > 0 ? (
              result.members.map((result) => (
                  <div key={result.email} className="participant_card">
                    <img src={result.profile || userImage} alt="프로필"/>
                    <h2>{result.nickname}</h2>
                    <button onClick={() => addResult(result.id, result.email, result.nickname, result.profile)}> 초대 </button>
                <button onClick={() => setFriendModal(false)}>X</button>
              </div>
            ))
          ) : <h2>검색 한 친구가 없습니다.</h2>}
        </div>
      );
  };

  const addResult = (result) => {
    setParticipant(prev => ({ num: prev.num + 1, members: [...prev.members, result ]})); // 참가자들 리스트 추가
      setResult((prev) => ({
          num: prev.num - 1,
          members: prev.members.filter((member) => member !== result)
      }));

  };

  const ParticipantList = () => {
      
    return (
      <div className="participant_list">
        <h3>팀원 목록</h3>
        {participant.num > 0 ? (
            participant.members.map((participant) => (
                <div key={participant.email} className="room_card">
                  <img src={participant.profile || userImage} alt="프로필" />
                  <h2>{participant.nickname}</h2>
                </div>
            ))
        ) : <h2>선택한 팀원이 없습니다.</h2>}
      </div>
    );
  }


  return (
    <div className="container">
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

      <main>
        <form className="search_box" onSubmit={(e) => e.preventDefault()}>
          <input
            className="project_search_txt"
            type="text"
            placeholder="프로젝트 이름을 입력하세요."
            value={searchTitle}
            onChange={(e) => setSearchTitle(e.target.value)}
          />
          <button
            className="search_button"
            type="submit"
            onClick={() => handleSearch(searchTitle)}
          >
            검색
          </button>
        </form>
        <RoomList />

        <form className="create_box" onSubmit={(e) => e.preventDefault()}>
          <button
            className="create_button"
            type="submit"
            onClick={()=>setCreateModal(true)}
          >
            프로젝트 생성
          </button>
        </form>

        {searchModal && (
            <div className="modal_overlay">
                <div onClick={(e) => e.stopPropagation()} className="add_project_container">
                <div className="modal_content">
                  <button className="close_button" onClick={() => closeSearchModal()}>
                    X
                  </button>
                  <div className="room_grid">
                    {roomData.num > 0 ? (
                        roomData.rooms.map((room) => (
                            <div key={room.roomId} className="room_card">
                              <h4>{room.title}</h4>
                              <button onClick={() => handleDeleteRoom(room.roomId)}>X</button>
                              <div className="process_flow">
                                <div className="process_step">주제선정</div>
                                <div className="arrow">→</div>
                                <div className="process_step">자료 조사</div>
                                <div className="arrow">→</div>
                                <div className="process_step">발표 자료</div>
                                <div className="arrow">→</div>
                                <div className="process_step">발표 준비</div>
                              </div>
                            </div>
                        ))
                    ) : <h2>검색한 프로젝트가 없습니다.</h2>}
                  </div>
                </div>
              </div>
            </div>
        )}
            {/*
            
            <div className="pagination">
                <button onClick={() => {
                    const newPage = Math.max(page - 1, 1);
                    setPage(newPage);
                }} disabled={page === 1}>이전</button>

                <span>{page} / {totalPages}</span>

                <button onClick={() => {
                    const newPage = Math.min(page + 1, totalPages);
                    setPage(newPage);
                }} disabled={page === totalPages}>다음</button>
            </div>
            
            */}

        {createmodal && (
            <div className="modal_overlay">
                <div onClick={(e) => e.stopPropagation()} className="add_project_container">
                    <div className="modal_content">
                <button className="close_button" onClick={() => closeCreateModal()}>
                  X
                </button>

                <div className="modal_body">
                  <div className="modal_section">
                    <label className="modal_label">방 제목</label>

                    <input className="modal_input" type="text"
                      value={roomTitle}
                      onChange={(e) => setRoomTitle(e.target.value)}
                    />
                  </div>

                  <div className="modal_section">
                    <label className="modal_label">비밀번호</label>
                      <input className="modal_input" type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                      />
                  </div>

                  <div className="modal_section">
                    <label className="modal_label">프로세스</label>

                    <div className="process_flow">
                      <div className="process_step">주제선정</div>
                      <div className="arrow">→</div>
                      <div className="process_step">자료 조사</div>
                      <div className="arrow">→</div>
                      <div className="process_step">발표 자료</div>
                      <div className="arrow">→</div>
                      <div className="process_step">발표 준비</div>
                    </div>
                  </div>

                  <div className="add_friend">
                    <button className="add_friend_button" onClick={() => {handleFriendList()}}>
                      +
                    </button>



                  <ParticipantList /> {/* 참가할 친구 리스트 */}

                  </div>
                  <button className="create_complete_btn" onClick={() => handleCreateClick()}>생성</button>
                </div>
              </div>
            </div>
          </div>
        )}
      </main>

        {enterModal && (
            <div className="modal_overlay">
                <div onClick={(e) => e.stopPropagation()} className="modal_section">
                    <label className="modal_label">비밀번호</label>

                            <input
                                className="modal_input"
                                type="password"
                                value={inputPassword}
                                onChange={(e) => setInputPassword(e.target.value)}
                            />
                        </div>
            </div>
        )}

        {error && (
            <div>
                <p className="error_message"> 비밀번호가 틀렸습니다. 다시 시도해 주세요. </p>
                <button className="check_password_button" onClick={() => handlePasswordCheck}>
                    확인
                </button>
            </div>
        )}

        {friendModal && (
            <div className="friend_overlay">
                <div onClick={(e)=> e.stopPropagation()} className="friend_modal">
                <input
                    className="friend_search_txt"
                    type="text"
                    placeholder="참여시킬 친구 이름을 입력하세요."
                    value={searchFriend}
                    onChange={(e) => setSearchFriend(e.target.value)}
                />
                <button className="search_icon" onClick={() => {handleFriend(searchFriend)}}>검색</button>
                <button onClick={() => setFriendModal(false)}> X</button>
                <div className="friend_list">
                    {result.num > 0 ? (
                        result.members.map((member) => (
                            <div key={member.email} className="friend_card">
                                <img src={member.profile || userImage} alt="프로필"/>
                                <h2>{member.nickname}</h2>
                                <button className="add_result_button"
                                    onClick={() => addResult(member)}> 초대
                                </button>
                            </div>
                        ))
                    ) : <h2>친구가 없습니다.</h2>}
                </div>
                </div>
            </div>
        )}

        /*{searchFriendModal && (
            <div className="search_friend_modal">
                <button onClick={() => setSearchFriendModal(false)}> X</button>
                {result.num > 0 ? (
                    result.members.map((result) => (
                        <div key={result.email} className="friend_card">
                            <img src={result.profile || userImage} alt="프로필"/>
                            <h2>{result.nickname}</h2>
                            <button
                                onClick={() => addResult(result.id, result.email, result.nickname, result.profile)}> 초대
                            </button>
                        </div>
                    ))
                ) : <h2>검색한 친구가 없습니다.</h2>}

            </div>

        )}*/
    </div>
  );
};

export default Project;