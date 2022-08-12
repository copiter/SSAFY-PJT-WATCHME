import React, { useState, useContext, useEffect } from "react";
import { FetchUrl } from "../../../store/communication";
import { getCookie } from "../../../Cookie";

import "./GroupDetail.css";
import json from "../../json/groupdetail.json";

import GroupDetailHome from "./GroupDetailHome";
import GroupDetailSprint from "./GroupDetailSprint";
import GroupDetailMembers from "./GroupDetailMembers";

// props 에 id, pwd 실려서 내려옴
function GroupDetail(props) {
  const [resData, setResData] = useState(json.responseData);
  const [navBar, setNavBar] = useState(0);
  const [isJoinCheck, setIsJoinCheck] = useState(false);

  //groupId 구하기
  const pathnameArr = window.location.pathname.split("/");
  const groupId = +pathnameArr[pathnameArr.length - 1];

  //데이터 요청
  const FETCH_URL = useContext(FetchUrl);
  const url = `${FETCH_URL}/groups/${groupId}`;
  useEffect(() => {
    const config = {
      method: "POST",
      headers: {
        accessToken: getCookie("accessToken"),
      },
    };
    const getDatas = async () => {
      const response = await fetch(url, config);
      const data = await response.json();
      if (data.code === 200) {
        setResData(data.responseData);
      } else {
        console.log(data.message);
      }
    };
    getDatas();
  }, []);

  function joinHandler() {
    const config = {
      method: "POST",
      headers: {
        accessToken: getCookie("accessToken"),
      },
    };
    const getDatas = async () => {
      try {
        const response = await fetch(url + "/applies", config);
        const data = await response.json();

        if (data.code === 200) {
          alert("그룹 가입 신청되었습니다");
          setIsJoinCheck(true);
        } else {
          alert(data.message);
        }
      } catch (e) {
        alert(`통신 실패 ` + e);
      }
    };
    getDatas();
  }

  function joinCancelHandler() {
    const config = {
      method: "POST",
      headers: {
        accessToken: getCookie("accessToken"),
      },
    };
    const getDatas = async () => {
      try {
        const response = await fetch(url + "/applies/cancel", config);
        const data = await response.json();

        if (data.code === 200) {
          alert("신청 취소되었습니다");
          setIsJoinCheck(false);
        } else {
          alert(data.message);
        }
      } catch (e) {
        alert(`통신 실패 ` + e);
      }
    };
    getDatas();
  }

  //Sidebar Nav 전환
  function navHandler(event) {
    const selectNum = event.target.value;
    const navs = document.querySelectorAll(".group-detail__nav-home");

    navs.forEach((nav, index) => {
      if (index === selectNum) {
        nav.classList.add("nav_active");
      } else {
        nav.classList.remove("nav_active");
      }
    });

    setNavBar(selectNum);
  }

  return (
    <>
      {resData.myData.role === 2 && (
        <div id="group-detail__joinBtn">
          {!isJoinCheck && (
            <button id="join_submit" onClick={joinHandler}>
              그룹 참가하기 🏹
            </button>
          )}
          {isJoinCheck && (
            <button id="join_cancel" onClick={joinCancelHandler}>
              그룹 참가 취소
            </button>
          )}
        </div>
      )}
      <div id="group-detail">
        <div id="group-detail__sidebar">
          <div id="group-detail__sidebar__info">
            <img
              src={resData.group.imgLink}
              alt="그룹 사진"
              id="group-detail__img"
            />
            <span id="group-detail__name">{resData.group.name}</span>
            <div id="group-detail__desc">
              <span>그룹소개</span>
              <p>{resData.group.description}</p>
            </div>
            <div id="group-detail__etc">
              <ul id="group-detail__etc__ctg">
                {resData.group.ctg.map((item, index) => (
                  <li key={index} className="group-detail__etc__ctg-item">
                    {item}
                  </li>
                ))}
              </ul>
              <div id="group-detail__etc__member">
                🗽 {resData.group.currMember}/{resData.group.maxMember}
              </div>
            </div>
          </div>
          <ul id="group-detail__sidebar__nav">
            <li
              className="group-detail__nav-home nav_active"
              onClick={navHandler}
              value="0"
            >
              Home
            </li>
            <li
              className="group-detail__nav-home"
              onClick={navHandler}
              value="1"
            >
              Sprint
            </li>
            <li
              className="group-detail__nav-home"
              onClick={navHandler}
              value="2"
            >
              Members
            </li>
          </ul>
        </div>

        {/* Main Contents : home, sprint, members*/}
        <>
          {navBar === 0 && (
            <GroupDetailHome resData={resData} groupId={groupId} />
          )}
          {navBar === 1 && (
            <GroupDetailSprint href={FETCH_URL} groupId={groupId} />
          )}
          {navBar === 2 && (
            <GroupDetailMembers myData={resData.myData} url={url} />
          )}
        </>
      </div>
    </>
  );
}

export default GroupDetail;
