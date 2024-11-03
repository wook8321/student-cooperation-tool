import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import axios from "axios";
import friendImage from './images/friends.png';
import projectImage from './images/project.png';
import userImage from './images/user.png';
import './friend.css';

const domain = "http://localhost:8080"

const Friend = () => {
    const [searchText, setSearchText] = useState('');
    const [modalOpen, setModalOpen] = useState(false);
    const [friendData, setFriendData] = useState(null);

    const handleSearchClick = () => {
        axios.get(domain + `/api/v1/friends/search?relation=true&name=${searchText}`)
            .then((res) => {
                setFriendData(res.data);
                setModalOpen(true);  
            })
            .catch(() => {
                console.log('failed to search friend.');
                setModalOpen(true);  
            });
    };

    const handleCloseModal = () => {
        setModalOpen(false);
        setFriendData(null);  // 모달창 닫을 때 데이터 초기화
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
                    className='friend_search_txt'
                    type="text"
                    placeholder="친구 이름을 입력하세요."
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
            
            {modalOpen && (
                <div className="modal">
                    <div className="modal_overlay">
                        <div className="modal_content">
                            <h3>검색 결과</h3> <button className="close_button" onClick={handleCloseModal}>X</button>
                            {friendData ? (
                                <p>
                                    {friendData.name}
                                    
                                    <button className='add_friend' type="post" onClick={() => {
                                        axios.post(domain + '/api/v1/friends')
                                            .then((res) => {
                                                console.log(res.data)
                                            })
                                            .catch(() => {
                                                console.log('failed to add friend.')
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
        </div>
    </>
    )
}

export default Friend;