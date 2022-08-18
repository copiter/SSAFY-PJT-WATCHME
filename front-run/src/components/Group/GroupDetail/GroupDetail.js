import React, { useState, useContext, useEffect } from "react";
import { FetchUrl } from "../../../store/communication";
import { getCookie } from "../../../Cookie";

import "./GroupDetail.css";
// import json from "../../json/groupdetail.json";

import GroupDetailHome from "./GroupDetailHome";
import GroupDetailSprint from "./GroupDetailSprint";
import GroupDetailMembers from "./GroupDetailMembers";
import ErrorCode from "../../../Error/ErrorCode";
import swal from "sweetalert";

function GroupDetail() {
  const [resData, setResData] = useState({
    group: {},
    members: [],
    myData: {},
    sprints: [],
    leader: {},
    groupData: {},
  });
  const [navBar, setNavBar] = useState(0);
  const [isSecret, setIsSecret] = useState(true);
  const [isJoinCheck, setIsJoinCheck] = useState(false);

  //groupId 구하기
  const pathnameArr = window.location.pathname.split("/");
  const groupId = +pathnameArr[pathnameArr.length - 1];

  //데이터 요청
  const FETCH_URL = useContext(FetchUrl);
  const url = `${FETCH_URL}/groups/${groupId}`;

  useEffect(() => {
    (async () => {
      const response = await fetch(url, {
        method: "POST",
        headers: {
          accessToken: getCookie("accessToken"),
        },
      });
      const data = await response.json();
      if (data.code === 200) {
        setResData(data.responseData);

        //비밀방 퇴출
        if (data.responseData.secret && data.responseData.myData.role !== 2) {
          setIsSecret(false);
        } else if (!data.responseData.secret) {
          setIsSecret(false);
        }
      } else if (data.code === 501) {
        window.history.back();
        swal("비공개 방입니다", "", "error");
      } else {
        window.history.back();
        ErrorCode(data);
      }
    })();
  }, [isJoinCheck]);

  console.log(resData);

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
          swal("그룹 가입 신청되었습니다", "", "success");
          setIsJoinCheck(true);
        } else {
          ErrorCode(data);
        }
      } catch (e) {
        swal("통신실패", "", "error");
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
          swal("신청 취소되었습니다", "", "success");
          setIsJoinCheck(false);
        } else {
          ErrorCode(data);
        }
      } catch (e) {
        swal("통신실패", "", "error");
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

  if (!isSecret) {
    return (
      <>
        {resData.myData.role === 2 && resData.myData.assign !== null && (
          <div id="group-detail__joinBtn">
            {resData.myData.assign === 0 && !isJoinCheck && (
              <button id="join_submit" onClick={joinHandler}>
                그룹 참가하기 🏹
              </button>
            )}
            {(isJoinCheck || resData.myData.assign === 1) && (
              <button id="join_cancel" onClick={joinCancelHandler}>
                그룹 참가 취소
              </button>
            )}
            {resData.myData.assign === 2 && (
              <button id="join_cancel" onClick={joinHandler}>
                반려되었습니다
              </button>
            )}
            {resData.myData.assign === 3 && (
              <button id="join_cancel">강퇴되었습니다</button>
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
                  {resData.group.hasOwnProperty("ctg") &&
                    resData.group.ctg.map((item, index) => (
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
                {!!resData.groupData.assignee ? (
                  <small>{resData.groupData.assignee}</small>
                ) : null}{" "}
                Members
              </li>
            </ul>
          </div>

          {/* Main Contents : home, sprint, members*/}
          <>
            {navBar === 0 && (
              <GroupDetailHome
                resData={resData}
                groupId={groupId}
                href={FETCH_URL}
              />
            )}
            {navBar === 1 && (
              <GroupDetailSprint
                href={FETCH_URL}
                groupId={groupId}
                role={resData.myData.role}
              />
            )}
            {navBar === 2 && (
              <GroupDetailMembers
                myData={resData.myData}
                url={url}
                href={FETCH_URL}
                groupId={groupId}
              />
            )}
          </>
        </div>
      </>
    );
  }
}

export default GroupDetail;
