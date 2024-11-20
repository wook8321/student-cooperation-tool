import React, { Component } from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Login from './login.js';
import Main from './main.js';
import Friend from './friend.js';
import Project from './project.js';

const App = () => {
  return (
    <div>
      <BrowserRouter>
        <Routes>
          {/* <Route path = "/" element = {<Login />}></Route>
          <Route path = "/home" element = {<Main />}></Route> */}
          <Route path = "/" element = {<Main />}></Route>
          <Route path = "/friend" element = {<Friend />}></Route>
          <Route path = "/project" element = {<Project />}></Route>
          {/*일치하는 라우트가 없는경우 처리 
					<Route path="/*" element={<NotFound />}></Route> */}
        </Routes>
      </BrowserRouter>
    </div>

  );
};

export default App;