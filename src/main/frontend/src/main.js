import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import axios from "axios";
import friendImage from './images/friends.png';
import projectImage from './images/project.png';
import './main.css'

const domain = "http://localhost:8080"

const Main = () => {

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
            <h1 className="main_title">STOOL</h1>
        </div>
    </>
    )
}

export default Main;