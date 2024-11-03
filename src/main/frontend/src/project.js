import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import axios from "axios";
import friendImage from './images/friends.png';
import projectImage from './images/project.png';
import "./project.css";

const domain = "http://localhost:8080"

const Project = () => {
    const [searchText, setSearchText] = useState('');
    const [searchmodal, setSearchModalOpen] = useState(false);
    const [createmodal, setCreateModal] = useState(false);
    const [roomData, setRoomData] = useState(null);

    const handleSearchClick = () => {
        axios.get(domain + `/api/v1/rooms/search?title=””&page=””&name=${searchText}`)
            .then((res) => {
                setRoomData(res.data);
                setSearchModalOpen(true);  
            })
            .catch(() => {
                console.log('failed to search project.');
                setSearchModalOpen(true);  
            });
    };

    const handleCreateClick = () => {
        axios.post(domain + `/api/v1/rooms`)
            .then((res) => {
                setRoomData(res.data);
                setCreateModal(true);  
            })
            .catch(() => {
                console.log('failed to search project.');
                setCreateModal(true);  
            });
    };

    const closeSearchModal = () => {
        setSearchModalOpen(false);
        setRoomData(null);  // 모달창 닫을 때 데이터 초기화
    };

    const closeCreateModal = () => {
        setCreateModal(false);
        setRoomData(null);  
    };

    return (
    <> 
        <div className="images">
            <Link to = "/friend">
                <button className="friend_image" onClick={() => {
                    axios.get(domain + '/api/v1/friends')
                    .then((res) => {
                        console.log(res.data)
                    })
                    .catch(() => {
                        console.log('failed to load friends')
                    })
                }}>
                    <img src={friendImage}/>
                </button>
            </Link>
            <br></br>
            <Link to = "/project">
                <button className="project_image" onClick={() => {
                    axios.get(domain + '/api/v1/rooms?page=1')
                    .then((res) => {
                        console.log(res.data)
                    })
                    .catch(() => {
                        console.log('failed to load projects')
                    })
                }}>
                    <img src={projectImage}/>
                </button>
            </Link>
        </div>
        
        <div className='container'>
            <form className='search_box' onSubmit={(e) => e.preventDefault()}>
                <input
                    className='project_search_txt'
                    type="text"
                    placeholder="프로젝트 이름을 입력하세요."
                    value={searchText}
                    onChange={(e) => setSearchText(e.target.value)}
                />
                <button
                    className='search_button'
                    type="submit"
                    onClick={handleSearchClick}
                >
                    검색
                </button>
            </form>

            <form className='create_box' onSubmit={(e) => e.preventDefault()}>
                <button
                    className='create_button'
                    type="submit"
                    onClick={handleCreateClick}
                >
                    프로젝트 생성
                </button>
            </form>
            
            {searchmodal && (
                <div className="modal">
                    <div className='modal_overlay'>
                        <div className="modal_content">
                            <h3>검색 결과</h3> <button className="close_button" onClick={closeSearchModal}>X</button>
                            {roomData ? (
                                <p>
                                    {roomData.name}
                                    
                                    <button className='add_room'onClick={() => {
                                        axios.get(domain + '/api/v1/rooms/search?title=””&page=””')
                                            .then((res) => {
                                                console.log(res.data)
                                            })
                                            .catch(() => {
                                                console.log('failed to add room.')
                                            })
                                            }}>
                                            친구 추가
                                    </button>
                                </p>
                            ) : (
                                <p>검색 결과가 없습니다.</p>
                            )}
                            
                        </div>
                    </div>
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
                                        <input
                                            className="modal_input"
                                            type="text"
                                        />
                                    </div>

                                    <div className="modal_section">
                                        <label className="modal_label">비밀번호</label>
                                            <input
                                                lassName="modal_input"
                                                type="password"
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
                                <button className='create_complete_btn'>생성</button> 
                            </div>     
                        </div> 
                    </div>   
                </div>
            )}
                        
        </div>
       </>
    )
}

export default Project;