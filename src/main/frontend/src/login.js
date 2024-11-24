import React, { useEffect, useState } from "react";
import axios from "axios";
import { Link } from "react-router-dom";
import "./login.css";
import "./scrollbar.css";
import {domain} from "./domain";

function Login() {

  return (
    <div className="full">
      <div className="login">
        <h1>로그인</h1>
        <div className="Social-Login">
          <a
            className="btn btn-primary w-100 py-2 mb-1"
            href="/oauth2/authorization/google"
            type="button"
          >
            Google로 로그인
          </a>
        </div>
      </div>
    </div>
  );
}

export default Login;
