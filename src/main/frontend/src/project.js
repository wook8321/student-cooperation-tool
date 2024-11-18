import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import axios from "axios";
import friendImage from './images/friends.svg';
import projectImage from './images/archive.svg';
import "./project.css";

const domain = "http://localhost:8080"



const Project = () => {
    const [createmodal, setCreateModal] = useState(false);
    const [roomData, setRoomData] = useState([]);
    const [searchTitle, setSearchTitle] = useState('');
    const [page, setPage] = useState(1);
    const [totalPages, setTotalPages] = useState(1);
    const [enterModal, setEnterModal] = useState(false);
    const [password, setPassword] = useState('1234');
    const [inputPassword, setInputPassword] = useState('');
    const [error, setError] = useState(false);

    const ProjectsList = () => {

        const handleDeleteRoom = () => {
            axios.delete(`${domain}/api/v1/rooms`)
                .then(() => {
                    console.log('Successed to delete room');
                })
                .catch(() => {
                    console.log('Failed to delete room');
                });
        };

        useEffect(() => {
            axios.get('${domain}/api/v1/rooms?page=1')
                .then((res) => {
                    setRoomData(res.data);
                })
                .catch(() => {
                    console.log('failed to load rooms');
                });
        }, []);

        return (
            <>
                <div className="room_grid">
                    {roomData.map((room) => (
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
                            <button onClick={setEnterModal(true)}>입장</button>
                        </div>
                    ))}
                </div>

                
            </>
        );
    };

    const handleSearch = () => {
        axios.get(`${domain}/api/v1/rooms/search?title=${searchTitle}&page=1`)
            .then((res) => {
                setRoomData(res.data);
            })
            .catch(() => {
                console.log('Failed to search project.');
            });
    };

    const createClick = () => {
        setCreateModal(true);
    }

    const handleCreateClick = () => {
        axios.post(`${domain}/api/v1/rooms`)
            .then((res) => {
                setRoomData(res.data);
                setCreateModal(false);
            })
            .catch(() => {
                console.log('Failed to create project.');
                setCreateModal(false);
            });
    };

    const closeCreateModal = () => {
        setCreateModal(false);
        setRoomData([]);  
    };

    const handleEnterRoom = () => {
        axios.post(`${domain}/api/v1/rooms/enter-room`)
            .then((res) => {
                setPassword(res.password);
            })
            .catch(() => {
                console.log('Failed to enter room');
            });
    };

    const handlePasswordCheck = () => {
        if (inputPassword === password) {
            <Link to='/topic'>
                
            </Link> 
        } else {
            setError(true);
        }
    };

    return (
    <> 
       <div className="images">
                <Link to="/friend">
                    <button className="friend_image">
                        <img src={friendImage} alt="친구 이미지" />
                    </button>
                </Link>
                <br />
                <Link to="/project">
                    <button className="project_image">
                        <img src={projectImage} alt="프로젝트 이미지" />
                    </button>
                </Link>
        </div>
        
        <div className='container'>
            <form className='search_box' onSubmit={(e) => e.preventDefault()}>
                <input
                    className='project_search_txt'
                    type="text"
                    placeholder="프로젝트 이름을 입력하세요."
                    value={searchTitle}
                    onChange={(e) => setSearchTitle(e.target.value)}
                />
                <button
                    className='search_button'
                    type="submit"
                    onClick={handleSearch}
                >
                    검색
                </button>
            </form>

            <form className='create_box' onSubmit={(e) => e.preventDefault()}>
                <button
                    className='create_button'
                    type="submit"
                    onClick={createClick}
                >
                    프로젝트 생성
                </button>
            </form>

            <ProjectsList />
    
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
            {enterModal && (
                <div className="modal_section">
                    <label className="modal_label">비밀번호</label>
                        <input
                            className="modal_input"
                            type="password"
                            value={inputPassword}
                            onChange={(e) => setInputPassword(e.target.value)}
                        />
                        
                        <button className="room_enter" onClick={handlePasswordCheck}>
                            입장
                        </button>
                </div>                
            )}

            {createmodal && (
                <div className='add_project_container'>
                    <div className="modal_overlay">
                        <div className="modal_content">
                            <button className="close_button" onClick={closeCreateModal}>X</button>
                            <div className="modal_body">
                                <div className="modal_section">
                                    <label className="modal_label">방 제목</label>
                                    <input className="modal_input" type="text" />
                                </div>

                                <div className="modal_section">
                                    <label className="modal_label">비밀번호</label>
                                    <input className="modal_input" type="password" />
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
                                <button className='create_complete_btn' onClick={handleCreateClick}>생성</button> 
                            </div>     
                        </div> 
                    </div>   
                </div>
            )}
        </div>

        

        {error && (
            <div>
                <p className="error_message">비밀번호가 틀렸습니다. 다시 시도해 주세요.</p>
                    <button className='check_password_button' onClick={handlePasswordCheck}>
                        확인
                    </button>
            </div>
        )}
    </>
    )
}

export default Project;