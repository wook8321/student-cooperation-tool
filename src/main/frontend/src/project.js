import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import axios from "axios";
import friendImage from "./images/friends.svg";
import projectImage from "./images/archive.svg";
import homeImage from "./images/home.png";
import "./project.css";
import userImage from "./images/user.svg";

const domain = "http://localhost:8080";

const RoomList = () => {
  const [rooms, setRooms] = useState({num: 0, roomList: []});
  const handleDeleteRoom = (roomId) => {
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
          console.log(res.data);
          setRooms(res.data.data);
        })
        .catch(() => {
          console.log("failed to load rooms");
        });
  }, []);

  return (
      <div className="room_list">
        <h3>방 목록</h3>
        {rooms.num > 0 ? (
            rooms.roomList.map((room) => (
                <div key={room.id} className="room_card">
                  <h4>{room.title}</h4>
                  <button onClick={() => handleDeleteRoom(room.id)}>X</button>
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
  const [searchTitle, setSearchTitle] = useState("");
  const [roomTitle, setRoomTitle] = useState("");
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);

  const [searchModal, setSearchModal] = useState(false);
  const [enterModal, setEnterModal] = useState(false);
  const [password, setPassword] = useState("");
  const [inputPassword, setInputPassword] = useState("");
  const [error, setError] = useState(false);

  const handleSearch = () => {
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
      .post(`${domain}/api/v1/rooms`)
      .then((res) => {
        console.log("Successed to create project.")
        closeCreateModal();
      })
      .catch(() => {
        console.log("Failed to create project.");
      });
  };

  const closeCreateModal = () => {
    setCreateModal(false);
    setRoomData(null);
  };

  const closeSearchModal = () => {
    setSearchModal(false);
    setRoomData(null);
  }

  const handleDeleteRoom = (roomId) => {
    axios
      .delete(`${domain}/api/v1/rooms`)
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
            onClick={handleSearch}
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
            <div className="add_project_container">
              <div className="modal_overlay">
                <div className="modal_content">
                  <button className="close_button" onClick={closeSearchModal}>
                    X
                  </button>
                  <div className="room_grid">
                    {roomData.num > 0 ? (
                        roomData.rooms.map((room) => (
                            <div key={room.id} className="room_card">
                              <h4>{room.title}</h4>
                              <button onClick={() => handleDeleteRoom(room.id)}>X</button>
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
                      <div className="add_project_container">
                        <div className="modal_overlay">
                          <div className="modal_content">
                            <button className="close_button" onClick={closeCreateModal}>
                              X
                            </button>
                            <div className="modal_body">
                              <div className="modal_section">
                                <label className="modal_label">방 제목</label>
                                <input className="modal_input" type="text"
                                       value={roomTitle}
                                       onChange={(e) => setRoomTitle(e.target.value)}/>
                              </div>

                              <div className="modal_section">
                                <label className="modal_label">비밀번호</label>
                                <input className="modal_input" type="password"
                                       value={password}
                                       onChange={(e) => setPassword(e.target.value)}/>
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
                              <button className="create_complete_btn"
                                      onClick={() => handleCreateClick()}>생성</button>
                            </div>
                          </div>
                        </div>
                      </div>
                  )}
                </main>

                {enterModal && (
                    <div className="modal_section">
                      <label className="modal_label">비밀번호</label>
                      <input
                          className="modal_input"
                          type="password"
                          value={inputPassword}
                          onChange={(e) => setInputPassword(e.target.value)}
                      />
                    </div>
                )}

                {error && (
                    <div>
                      <p className="error_message">
                        비밀번호가 틀렸습니다. 다시 시도해 주세요.
                      </p>
                      <button
                          className="check_password_button"
                          onClick={handlePasswordCheck}
                      >
                        확인
                      </button>
                    </div>
                )}
              </div>
              );
              };

              export default Project;