import React, { useState, useContext, useEffect } from "react";
import { FetchUrl } from "../../store/communication";
import getCookie from "../../Cookie";

import "./GroupDetail.css";
import json from "../json/groupdetail.json";

import GroupDetailHome from "./GroupDetail/GroupDetailHome";
import GroupDetailSprint from "./GroupDetail/GroupDetailSprint";
import GroupDetailMembers from "./GroupDetail/GroupDetailMembers";

// props 에 id, pwd 실려서 내려옴
function GroupDetail(props) {
  const [resData, setResData] = useState(json.responseData);
  const [navBar, setNavBar] = useState(0);
  const [isJoinCheck, setIsJoinCheck] = useState(false);

  //데이터 요청
  const FETCH_URL = useContext(FetchUrl);
  const url = `${FETCH_URL}/groups/1`; //임시로 넣어줌
  // const url = `${FETCH_URL}/groups/${props.id}`;
  // const url = "http://localhost:5000/responseData/";
  // useEffect(() => {
  //   const config = {
  //     method: "POST",
  //     credentials: "include",
  //     body: {
  //       pwd: props.pwd,
  //     },
  //     headers: {
  //       accessToken: getCookie("accessToken"),
  //     },
  //   };
  //   const getDatas = async () => {
  //     // const response = await fetch(url, config);
  //     const response = await fetch(url);
  //     const data = await response.json();
  //     setResData(data);
  //   };
  //   getDatas();
  // }, []);

  //role에 따라 변경
  if (resData.myData.role === "anonymous") {
    console.log(window.document.getElementById("group-detail__joinBtn"));
    document.getElementById("group-detail__joinBtn").style.visibility =
      "visible";
  } else if (resData.myData.role === "crew") {
    //그룹원인 경우
  } else if (resData.myData.role === "leader") {
    //리더인 경우
  }

  function joinHandler() {
    const config = {
      method: "POST",
      headers: {
        accessToken: getCookie("accessToken"),
      },
    };
    const getDatas = async () => {
      try {
        await fetch(url + "/applies", config);
        alert("그룹 가입 신청되었습니다");
        setIsJoinCheck(true);
      } catch (e) {
        alert(`가입 신청 실패 ` + e);
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
        await fetch(url + "/applies/cancel", config);
        alert("신청 취소되었습니다");
        setIsJoinCheck(false);
      } catch (e) {
        alert(`신청 취소 실패 ` + e);
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
      {/* 만일 anonymous인 경우 visibility: visible */}
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
          {navBar === 0 && <GroupDetailHome resData={resData} />}
          {navBar === 1 && <GroupDetailSprint />}
          {navBar === 2 && (
            <GroupDetailMembers myData={resData.myData} url={url} />
          )}
        </>
      </div>
    </>
  );
}

export default GroupDetail;
