import React, { Component } from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Login from './login.js';
import Main from './main.js';
import Friend from './friend.js';
import Project from './project.js';
import Auth from "./auth";

const App = () => {
  return (
    <div>
      <BrowserRouter>
        <Routes>
          <Route path = "/" element = {<Main />}></Route>
          <Route path = "/login" element = {<Login />}></Route>
          <Route path = "/friend" element = {<Auth><Friend /></Auth>}></Route>
          <Route path = "/project" element = {<Auth><Project /></Auth>}></Route>
          {/*일치하는 라우트가 없는경우 처리 
					<Route path="/*" element={<NotFound />}></Route> */}
        </Routes>
      </BrowserRouter>
    </div>

  );
};

export default App;