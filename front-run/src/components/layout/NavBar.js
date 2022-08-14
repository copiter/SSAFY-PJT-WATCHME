import React, { useContext, useEffect, useState } from "react";
import { NavLink, Link } from "react-router-dom";
import AuthContext from "../../store/auth-context";
import { getCookie } from "../../Cookie";

import "./NavBar.css";
import logo from "../../img/logo.png";
import tmp_picture from "../../img/tmp_picture.PNG";

const NavBar = () => {
  const authCtx = useContext(AuthContext);

  // const [isLoggedIn, setIsLoggedIn] = useState(authCtx.isLoggedIn);
  const isLoggedIn = !!getCookie("accessToken");

  const logoutHandler = () => {
    authCtx.logout();
  };

  if (!isLoggedIn) {
    logoutHandler();
  }

  return (
    <header className="navbar">
      <div className="navbar__left">
        <Link to="/">
          <div className="nav-logo">
            <div>
              <img src={logo} alt="로고" height="30px" />
            </div>
            <h2>WATCH ME</h2>
          </div>
        </Link>
        <nav className="nav-list">
          <ul>
            <li>
              <NavLink to="RoomRecruit">공개룸 모집</NavLink>
            </li>
            <li>
              <NavLink to="GroupRecruit">스터디 그룹 모집</NavLink>
            </li>
            <li>
              <NavLink to="About">소식</NavLink>
            </li>
          </ul>
        </nav>
      </div>
      <div className="navbar__right">
        {/* 로그인 되면 */}
        {isLoggedIn && (
          <div className="dropdown__login">
            {/* 프로필사진, 이름 */}
            <div className="user-info">
              <img
                src={authCtx.userData.profileImage}
                alt="profile"
                id="nav-profile-picture"
              />
              <span className="nav-username">{authCtx.userData.nickName}</span>
            </div>

            {/* dropdown */}
            <div className="dropdowncontent__login">
              <a onClick={logoutHandler}>로그아웃</a>
              <Link to="/MyPage">마이페이지</Link>
              <Link to="/MyGroups">내그룹</Link>
            </div>
          </div>
        )}
        {/* 로그아웃되면 */}
        {!isLoggedIn && <Link to="/login">로그인</Link>}
      </div>
    </header>
  );
};

export default NavBar;
