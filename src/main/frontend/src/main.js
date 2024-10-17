import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import axios from "axios";

const domain = "http://localhost:8080"

const Main = () => {
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
        <div className="friend">
            <Link to = "/friend">
                <button className="friends image" onClick={() => {
                    axios.get(domain + '/friend/*')
                    .then((res) => {
                        console.log(res.data)
                    })
                    .catch(() => {
                        console.log('failed to load friends')
                    })
                }}>
                    <img src="../images/friends.png"/>
                </button>
            </Link>
        </div>
        <h1>STOOL</h1>
    </>
    )
}

export default Main;