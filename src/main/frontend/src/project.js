import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import axios from "axios";
import userImage from "./images/user.svg"
import "./project.css";
import Footer from "./footer";
import {domain} from "./domain";
import "./scrollbar.css"
import "./card.css"
import "./paginationButton.css"


const RoomList = () => {
  const [enterModal, setEnterModal] = useState(false);
  const [enterRoomId, setEnterRoomId] = useState(0)
  const [rooms, setRooms] = useState({num: 0, roomList: []});
  const [currentPage, setCurrentPage] = useState(1); // 현재 페이지 상태 추가

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

    // 방 목록 가져오기 (페이지에 따라 호출)
    const fetchRooms = (page) => {
        axios
            .get(domain + `/api/v1/rooms?page=${page}`)
            .then((res) => {
                console.log(res.data);
                setRooms(res.data.data);
                setCurrentPage(page); // 현재 페이지 업데이트
            })
            .catch(() => {
                console.log("Failed to load rooms");
            });
    };

    // 초기 로드 시 첫 페이지 데이터 가져오기
    useEffect(() => {
        fetchRooms(0); // 첫 페이지로 초기화
    }, []);

    const enterRoom = (roomId) =>{
        setEnterRoomId(roomId);
        setEnterModal(true);
    }

  return (
      <div className="room_list">
        <h3> 참여한 프로젝트 : {rooms.num}</h3>
          <div>
              {rooms.num > 0 ? (
                  <>
                      <div className="card-container">
                      {rooms.rooms.map((room) => {
                          const capColors = ["pink-cap", "green-cap", "orange-cap"];
                          const randomCapClass = capColors[Math.round(Math.random() * capColors.length)];
                          return (
                          <div key={room.roomId} className="card">
                              <div className={`image-cap ${randomCapClass}`}>
                                  {room.title}
                              </div>
                              <div className="card-body">
                            <span style={{ fontSize: "small", justifyContent: "end", color: "white" }}>
                                참가자 : {room.participationNum}
                            </span>
                                  <h3 className="card-title">주제 : {room.topic}</h3>
                                  <div className="button-group">
                                      <button className="card-button" onClick={() => enterRoom(room.roomId)}>
                                          입장하기
                                      </button>
                                      <button className="card-red-button" onClick={() => handleDeleteRoom(room.roomId)}>
                                          삭제하기
                                      </button>
                                  </div>
                              </div>
                          </div>
                      );
                      })}
                      </div>
                      <div id="paginationButtonGroup" className="pagination-container">
                          <button className="pagination-button" onClick={() => fetchRooms(rooms.firstPage - 1)}
                                  disabled={currentPage === rooms.firstPage - 1}>
                              맨 처음
                          </button>
                          <div id="paginationButtonGroup" className="pagination-container">
                              {/* 여기서 firstPage부터 lastPage까지 버튼 생성 */}
                              {Array.from(
                                  { length: rooms.lastPage - rooms.firstPage + 1 },
                                  (_, i) => rooms.firstPage + i
                              ).map((page) => (
                                  <button className="pagination-button" onClick={() => fetchRooms(page - 1)}
                                  disabled={currentPage === page - 1}>
                                      {page}
                                  </button>
                              ))}
                          </div>
                          <button onClick={() => fetchRooms(rooms.lastPage - 1)} className="pagination-button"
                                  disabled={currentPage === rooms.lastPage - 1}>
                              마지막
                          </button>
                      </div>
                  </>
              ) : <h2>프로젝트가 없습니다.</h2>}
        </div>
      </div>
  );
};

const Project = () => {
  const [createmodal, setCreateModal] = useState(false);
  const [roomData, setRoomData] = useState({num:0, rooms:[]});
  const [result, setResult] = useState({num:0, members:[]}); // 초대할 친구 정보
  const [participant, setParticipant] = useState({num:0, members:[]}); // 이미 초대된 친구들 정보

  const [enterRoomId, setEnterRoomId] = useState(0)
  const [searchTitle, setSearchTitle] = useState("");
  const [searchFriend, setSearchFriend] = useState("");
  const [roomTitle, setRoomTitle] = useState("");

  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);

  const [searchModal, setSearchModal] = useState(false);
  const [enterModal, setEnterModal] = useState(false);
  const [friendModal, setFriendModal] = useState(false);
  const [password, setPassword] = useState("");
  const [inputPassword, setInputPassword] = useState("");
  const [error, setError] = useState(false);

  const handleSearch = ({page}) => {
      const searchTitle = document.getElementById("roomSearchInput").value;
    axios
      .get(`${domain}/api/v1/rooms/search?title=${searchTitle}&page=${page}`)
      .then((res) => {
          setRoomData(res.data.data);
          setSearchModal(true);  // 모달 열기
      })
      .catch(() => {
        console.log("Failed to search project.");
      });
  };

    const enterRoom = (roomId) =>{
        closeSearchModal()
        setEnterRoomId(roomId)
        setEnterModal(true)
    }

  const verifyPasswordAndEnterRoom = (roomId) =>{
      const password = document.getElementById("roomPasswordInput").value
      const data = {
          roomId,
          password
      }
      axios
          .post(`${domain}/api/v1/rooms/enter-room`,data,{ "Content-Type": "application/json"})
          .then((res) =>{
              const isCorrect = res.data.data
              if(isCorrect){
                  //비밀 번호가 맞다면, 방을 입장
                  <Link to="/topic" state={{roomId}}></Link>;
                  closeEnterModal()
              }
          })
          .catch(() =>{
              const passwordInvalidDiv = document.getElementById("passwordInvalidDiv");
              passwordInvalidDiv.innerHTML = '<span style="color:red;">해당 비밀번호는 틀렸습니다. 다시 입력해주세요.</span>'
          })
  }

  const handleLoadMore = () => {
        setPage((prevPage) => prevPage + 1);  // 페이지 번호 증가
        handleSearch({ page: page + 1 });     // 다음 페이지 데이터 요청
  };

    // 이전 페이지 버튼 클릭
    const handlePrevPage = () => {
        if (page > 0) {
            setPage((prevPage) => prevPage - 1);
            handleSearch({ page: page - 1 });
        }
    };

    // 다음 페이지 버튼 클릭
    const handleNextPage = () => {
        setPage((prevPage) => prevPage + 1);
        handleSearch({ page: page + 1 });
    };

  const handleCreateClick = () => {
    axios
      .post(`${domain}/api/v1/rooms`)
      .then(() => {
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
    setPage(0);
    setSearchModal(false);
    setRoomData(null);
  }

  const closeEnterModal = () => {
      setEnterRoomId(0)
      setEnterModal(false)
  }

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

  const handlePasswordCheck = () => {
    if (inputPassword === password) {
      <Link to="/subject"></Link>;
    } else {
      setError(true);
    }
  };

  const friendList = () => {
    axios.get(`${domain}/api/v1/friends`)
        .then((res) => {
          setResult(res.data.data);
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
                    <button onClick={() => addResult(result.email, result.nickname, result.profile)}> 초대</button>
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
                    <button onClick={() => addResult(result.email, result.nickname, result.profile)}> 초대 </button>
                <button onClick={() => setFriendModal(false)}>X</button>
              </div>
            ))
          ) : <h2>검색 한 친구가 없습니다.</h2>}
        </div>
      );
  };

  const addResult = (email, nickname, profile) => {
    setParticipant(prev => ({ num: prev.num + 1, members: { email, nickname, profile }, ...prev })); // 참가자들 리스트 추가
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
      <Footer/>

      <main>

        <form className="search_box" onSubmit={(e) => e.preventDefault()}>
          <input id="roomSearchInput" className="project_search_txt" type="text" placeholder="프로젝트 이름을 입력하세요."/>
          <button className="search_button" type="submit" onClick={() => handleSearch({page : 0})}>
            검색
          </button>
        </form>
          <form className="create_box" onSubmit={(e) => e.preventDefault()}>
              <button className="create_button" type="submit" onClick={()=>setCreateModal(true)}>
                  프로젝트 생성
              </button>
          </form>
        <RoomList />

        {searchModal && (
            <div className="add_project_container">
              <div className="modal_overlay">
                <div className="modal_content">
                  <button className="close_button" onClick={() => closeSearchModal()}>
                    X
                  </button>
                  <div className="room_grid scrollbar">
                    {roomData.num > 0 ? (
                        <>
                            <div className="card-container">
                                {roomData.rooms.map((room) => {
                                    // 랜덤 배경색 클래스 설정
                                    const capColors = ["pink-cap", "green-cap", "orange-cap"];
                                    const randomCapClass = capColors[Math.round(Math.random() * capColors.length)];

                                    return (
                                        <div key={room.roomId} className="card">
                                            {/* 동적으로 랜덤 클래스 추가 */}
                                            <div className={`image-cap ${randomCapClass}`}>{room.title}</div>
                                            <div className="card-body">
                                                <h3 className="card-title">{room.topic}</h3>
                                                <button className="card-button" onClick={() => enterRoom(room.roomId)}>
                                                    참여하기
                                                </button>
                                            </div>
                                        </div>
                                    );
                                })}
                            </div>
                            <div style={{ textAlign: 'center' }} className="pagination-container">
                                <button onClick={handlePrevPage} className="pagination-button" disabled={page === 0}>
                                        이전
                                </button>
                                <button onClick={handleNextPage} className="pagination-button" disabled={roomData.last}>
                                    다음
                                </button>
                            </div>
                        </>
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
          <div className="add_project_container ">
            <div className="modal_overlay">
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
                      <input className="modal_input" type="password" value={password}/>
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
                    <button className="add_friend_button" onClick={() => {setFriendModal(true); friendList()}}> {/* 참가할 친구 추가 */}
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
              <div className="modal_content"  style={{textAlign : "center"}}>
                  <button className="close_button" onClick={() => closeEnterModal()}>
                      X
                  </button>
                  <div id="passwordInvalidDiv"></div>
                  <label className="modal_label">비밀번호</label>
                  <input className="modal_input" id="roomPasswordInput" type="password"/>
                  <button onClick={() => verifyPasswordAndEnterRoom(enterRoomId)}> 입장 </button>
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
            <div className="friend_modal">
              <input
                  className="friend_search_txt"
                  type="text"
                  placeholder="참여시킬 친구 이름을 입력하세요."
                  value={searchFriend}
                  onChange={(e) => setSearchFriend(e.target.value)}
              />
              <button className="search_icon" onClick={() => handleFriend(searchFriend)}> 검색</button>
              <button className="close_button" onClick={() => setFriendModal(false)}> X</button>
              <div className="friend_list">
                {result.num > 0 ? (
                    result.members.map((result) => (
                        <div key={result.email} className="friend_card">
                          <img src={result.profile || userImage} alt="프로필"/>
                          <h2>{result.nickname}</h2>
                          <button onClick={() => addResult(result.email, result.nickname, result.profile)}> 초대</button>
                        </div>
                    ))
                ) : <h2>친구가 없습니다.</h2>}
              </div>
            </div>
        )}
    </div>
  );
};

export default Project;