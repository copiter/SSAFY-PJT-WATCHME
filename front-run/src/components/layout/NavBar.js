import React, { useContext } from "react";
import { NavLink, Link } from "react-router-dom";
import AuthContext from "../../store/auth-context";

import "./NavBar.css";

const NavBar = () => {
  const authCtx = useContext(AuthContext);

  const logoutHandler = () => {
    authCtx.logout();
    location.push(0);
  };

  const isLoggedIn = !localStorage.hasOwnProperty("isLoggedIn")
    ? false
    : localStorage.getItem("isLoggedIn")
    ? true
    : false;

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
          <>
            {/* 로그아웃 버튼은 임시 */}
            <button onClick={logoutHandler}>로그아웃</button>
            <div className="nav-profile-picture">
              <img src="#none" alt="profile" /> {/* 프로필사진 */}
            </div>
            <div className="nav-username">Username</div>
          </>
        )}
        {/* 로그아웃되면 */}
        {!isLoggedIn && <Link to="/login">로그인</Link>}
      </div>
    </header>
  );
};

export default NavBar;
