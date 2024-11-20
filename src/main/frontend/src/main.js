import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import axios from "axios";
import friendImage from "./images/friends.svg";
import projectImage from "./images/archive.svg";
import homeImage from "./images/home.png";
import "./main.css";

const domain = "http://localhost:8080";

const Main = () => {
  return (
    <div className="container">
      <main>
        <h1 className="main_title">STOOL</h1>
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
    </div>
  );
};

export default Main;
