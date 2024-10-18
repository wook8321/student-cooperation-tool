import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import axios from "axios";

const domain = "http://localhost:8080"

const Friend = () => {
    const [hello, setHello] = useState('');
    
    useEffect(() => {
        axios.get(domain + '/friend')
            .then(res => {
            setHello(JSON.stringify(res.data));
            })
            .catch(error =>{
            alert(JSON.stringify(error))
            })
    }, []);

    return (
    <> 
        <div className="images">
            <Link to = "/friend">
                <button className="friend_image" onClick={() => {
                    axios.get(domain + '/friend/*')
                    .then((res) => {
                        console.log(res.data)
                    })
                    .catch(() => {
                        console.log('failed to load friends')
                    })
                }}>
                    <img src="./friends.png"/>
                </button>
            </Link>
            <br></br>
            <Link to = "/project">
                <button className="project_image" onClick={() => {
                    axios.get(domain + '/friend/*')
                    .then((res) => {
                        console.log(res.data)
                    })
                    .catch(() => {
                        console.log('failed to load projects')
                    })
                }}>
                    <img src="../images/project.png"/>
                </button>
            </Link>
        </div>
        
        <div className='container'>
            <form className='search_box' action="" method="get">
                <input className='friend_search_txt' type="text" placeholder="친구 이름을 입력하세요."></input>
                <button className='search_button' type="submit"></button>
            </form>          
            <h1 className="main_title">STOOL</h1>
        </div>
    </>
    )
}

export default Friend;