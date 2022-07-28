import React, { useContext, useEffect, useState } from "react";
import { NavLink, Link } from "react-router-dom";
import AuthContext from "../../store/auth-context";

import "./NavBar.css";

const NavBar = () => {
  const authCtx = useContext(AuthContext);
  const [isLoggedIn, setIsLoggedIn] = useState(
    !sessionStorage.hasOwnProperty("isLoggedIn")
      ? false
      : sessionStorage.getItem("isLoggedIn")
      ? true
      : false
  );

  useEffect(() => {
    // window.location.reload();
  }, [setIsLoggedIn]);

  const logoutHandler = () => {
    setIsLoggedIn(false);
    authCtx.logout();
  };

  return (
    <header className="navbar">
      <div className="navbar__left">
        <Link to="/">
          <div className="nav-logo">
            <img src="#none" alt="로고" />
            WATCH ME
          </div>
        </Link>
        <nav className="nav-list">
          <ul>
            <li>
              <NavLink to="RoomRecruit">공개룸 모집</NavLink>
            </li>
            <li>
              <NavLink to="GroupRecruit">스터디 모집</NavLink>
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
              <img src="#none" alt="profile" className="nav-profile-picture" />
              <span className="nav-username">Username</span>
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
