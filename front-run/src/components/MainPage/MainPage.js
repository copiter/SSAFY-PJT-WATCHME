import React from "react";
import { useContext, useState, useEffect } from "react";

import "./MainPage.css";
import { Link, useNavigate } from "react-router-dom";
import jsons from "../json/jsons";
import { FetchUrl } from "../../store/communication";
import AuthContext from "../../store/auth-context";
import mystudy__infor__tmp from "../../img/Icons/mystudy__infor__tmp.png";

//유일한 쿠키 조회
function getCookie(name) {
  if (!document.cookie) {
    return;
  }
  const cookie = document.cookie
    .split("; ")
    .map((cookie) => cookie.split("="))
    .filter((cookie) => cookie[0] === name);
  return cookie[0][1];
}

function MainPage() {
  const authCtx = useContext(AuthContext);
  const navigate = useNavigate();
  //URL
  const FETCH_URL = useContext(FetchUrl);
  const url = `${FETCH_URL}/main`;

  let rooms, groups, myGroups, userInformation;

  let groupNo = 0;
  let roomNo = 0;
  let myGroupNo = 0;

  const [isLoggedIn, setIsLoggedIn] = useState(authCtx.isLoggedIn);

  const [responseData, setResponseData] = useState(jsons["responseData"]);

  useEffect(() => {
    const getDatas = async () => {
      const response = await fetch(url, {
        credentials: "include",
        headers: {
          accessToken: getCookie("accessToken"),
        },
      });
      const data = await response.json();
      setResponseData(data.responseData);
      if (data.responseData.member !== undefined) {
        authCtx.userDataGetter({
          profileImage: data.responseData.member.profileImage,
          nickName: data.responseData.member.nickName,
        });
      }
    };
    getDatas();
  }, []);

  rooms = responseData["rooms"];
  groups = responseData["groups"];

  if (isLoggedIn) {
    userInformation = responseData["member"];
    myGroups = responseData["myGroups"];
  }

  function enteringRoom(id) {
    const urls = `${FETCH_URL}/room/join/`;

    function getCookie(name) {
      const cookie = document.cookie
        .split(";")
        .map((cookie) => cookie.split("="))
        .filter((cookie) => cookie[0] === name);
      return cookie[0][1];
    }
    console.log(urls + 15);
    fetch(urls + id, {
      method: "POST",
      headers: {
        accessToken: getCookie("accessToken"),
      },
    })
      .then((response) => {
        console.log("T1");
        if (response.bodyUsed) {
          console.log("재사용됨");
        } else if (response.ok) {
          console.log("Case2");
          return response.json();
        } else {
          console.log("C4");
        }
      })
      .then((result) => {
        console.log(result);
        navigate(`/RoomDetail/:${id}`);
      })
      .catch((err) => {
        alert("로그인후 이용부탁드립니다.");
      });
  }

  return (
    <>
      <div id="outer">
        {/* 로그인 됐을때만 표시 */}
        {isLoggedIn && (
          <section id="mainpage__myinfor">
            {/*개인과 관련된 섹션. 임시링크들 있음 수정예정 */}
            <div id="mypage__myinfor__width">
              <div id="mypage__myinfor__title">
                오늘도 화이팅, {userInformation["nickName"]}님
              </div>
              <div id="mypage__myinfor__contents">
                <div id="mypage__myinfor__create-room">
                  {/*방생성관련 */}
                  <span className="mypage__myinfor__sub-title">방만들기</span>
                  <Link to="/RoomCreate">
                    <div id="mypage__myinfor__create-room__create"></div>
                  </Link>
                </div>
                <div id="mypage__myinfor__mystudy-group">
                  {/*내가 가입한 스터디그룹 관련*/}
                  <span className="mypage__myinfor__sub-title">
                    내 스터디그룹
                  </span>

                  {!myGroups.length && (
                    <>
                      {/*그룹 아무것도 가입안한경우 */}
                      <div>
                        가입한 그룹이 없습니다. 새로 가입해보시는건 어떠신가요
                      </div>
                    </>
                  )}
                  {myGroups.length && (
                    <div id="mypage__myinfor__groups">
                      <Link
                        id="mypage__myinfor__mystudy-group1"
                        className="mypage__myinfor__mystudy-group-image"
                        to="/GroupDetail"
                      >
                        {/* <div className="mypage__myinfor__mystudy-group__Title"></div> */}
                        <span>{myGroups[myGroupNo]["groupName"]}</span>
                        {/* <div className="mypage__myinfor__mystudy-group__img"></div> */}
                        <img
                          className="mypage__myinfor__mystudy-group__img"
                          src={
                            myGroups[myGroupNo]["groupImage"] === "none" ||
                            myGroups[myGroupNo]["groupImage"] === ""
                              ? "#"
                              : myGroups[myGroupNo]["groupImage"]
                          }
                          alt="그룹이미지"
                        />
                      </Link>
                      {myGroups.length > ++myGroupNo && (
                        <Link
                          id="mypage__myinfor__mystudy-group2"
                          className="mypage__myinfor__mystudy-group-image"
                          to="/GroupDetail"
                        >
                          <div className="mypage__myinfor__mystudy-group__Title">
                            {myGroups[myGroupNo]["groupName"]}
                          </div>
                          <img
                            className="mypage__myinfor__mystudy-group__img"
                            src={
                              myGroups[myGroupNo]["groupImage"] === "none" ||
                              myGroups[myGroupNo]["groupImage"] === ""
                                ? "#"
                                : myGroups[myGroupNo]["groupImage"]
                            }
                            alt="그룹이미지"
                          />
                        </Link>
                      )}
                    </div>
                  )}
                </div>
                <div id="mypage__myinfor__mystudy">
                  {/*내가 공부한 총 공부시간 관련. */}
                  <div className="mypage__myinfor__sub-title">
                    나의 공부시간
                  </div>
                  <Link to="MyPage">
                    <div id="mypage__myinfor__mystudy__infor">
                      <div id="mypage__myinfor__mystudy__infor__chart">
                        <img src={mystudy__infor__tmp} width="128px" />
                      </div>
                      <ul id="mypage__myinfor__mystudy__infor__text">
                        <li>
                          <span className="mystudy__infor__text-left">
                            오늘 공부시간
                          </span>
                          <span>
                            {`${parseInt(
                              userInformation["studyTimeToday"] / 60
                            )}시간 ${userInformation["studyTimeToday"] % 60}분`}
                          </span>
                        </li>
                        <li>
                          <span className="mystudy__infor__text-left">
                            이번주 공부시간
                          </span>
                          <span>
                            {`${parseInt(
                              userInformation["studyTimeWeek"] / 60
                            )}시간 ${userInformation["studyTimeWeek"] % 60}분`}
                          </span>
                        </li>
                        <li>
                          <span className="mystudy__infor__text-left">
                            이번달 공부시간
                          </span>
                          <span>
                            {`${parseInt(
                              userInformation["studyTimeMonth"] / 60
                            )}시간 ${userInformation["studyTimeMonth"] % 60}분`}
                          </span>
                        </li>
                        <li>
                          <span className="mystudy__infor__text-left">
                            총 공부시간
                          </span>
                          <span>
                            {`${parseInt(
                              userInformation["studyTimeTotal"] / 60
                            )}시간 ${userInformation["studyTimeTotal"] % 60}분`}
                          </span>
                        </li>
                      </ul>
                    </div>
                  </Link>
                </div>
              </div>
            </div>
          </section>
        )}

        {/* 로그인 유무와 관계 없는 부분 */}
        <section id="mainpage_study-groups">
          {/* 스터디 그룹탐색 관련 섹션 연결하는 임시링크들 있음. 수정예정. */}
          <div className="section__top">
            <div className="section__top__text">
              인기있는
              <br /> 모집한 스터디그룹🥇{" "}
            </div>
            <Link to="/GroupRecruit">
              <button className="section__top__btn">더보기 &gt;</button>
            </Link>
            {/* <div className="section__top__rec"></div> */}
          </div>
          <div id="mainpage__study-groups__groups">
            {/*실제 글부의 정보를 가져와 연결해줌 조 수 높은 그룹이 우선순으로 표기됨. */}
            {!groups.length /*오류나 초기상태로 그룹이 DB에 존재하지 않는경우 */ && (
              <></>
            )}
            {groups.length && (
              <ul>
                <li>
                  <Link
                    to="/GroupDetail"
                    className="groups__img"
                    style={{
                      position: "relative",
                      backgroundImage: `url(${groups[groupNo]["groupImage"]})`,
                      backgroundSize: "cover",
                    }}
                  >
                    {/* <img
                      className="groups__img"
                      src={
                        groups[groupNo]["groupImage"] === "none"
                          ? "기본이미지"
                          : groups[groupNo]["groupImage"]
                      }
                    /> */}
                    <div className="groups__group-info">
                      <div className="group-info__left">
                        <span className="groups__group-name">
                          {groups[groupNo]["groupName"]}
                        </span>
                        <p className="groups__group-descript">
                          {groups[groupNo]["groupDescription"]}
                        </p>
                      </div>
                      <div className="groups__mem-no-rec">
                        {`${groups[groupNo]["groupMemberNo"]}/${groups[groupNo]["groupMemberMaxNo"]}`}
                      </div>
                    </div>
                  </Link>
                </li>

                {groups.length > ++groupNo && (
                  <li>
                    <Link
                      to="/GroupDetail"
                      className="groups__img"
                      style={{
                        position: "relative",
                        backgroundImage: `url(${groups[groupNo]["groupImage"]})`,
                        backgroundSize: "cover",
                      }}
                    >
                      {/* <img
                      className="groups__img"
                      src={
                        groups[groupNo]["groupImage"] === "none"
                          ? "기본이미지"
                          : groups[groupNo]["groupImage"]
                      }
                    /> */}
                      <div className="groups__group-info">
                        <div className="group-info__left">
                          <span className="groups__group-name">
                            {groups[groupNo]["groupName"]}
                          </span>
                          <p className="groups__group-descript">
                            {groups[groupNo]["groupDescription"]}
                          </p>
                        </div>
                        <div className="groups__mem-no-rec">
                          {`${groups[groupNo]["groupMemberNo"]}/${groups[groupNo]["groupMemberMaxNo"]}`}
                        </div>
                      </div>
                    </Link>
                  </li>
                )}
                {groups.length > ++groupNo && (
                  <li>
                    <Link
                      to="/GroupDetail"
                      className="groups__img"
                      style={{
                        position: "relative",
                        backgroundImage: `url(${groups[groupNo]["groupImage"]})`,
                        backgroundSize: "cover",
                      }}
                    >
                      {/* <img
                      className="groups__img"
                      src={
                        groups[groupNo]["groupImage"] === "none"
                          ? "기본이미지"
                          : groups[groupNo]["groupImage"]
                      }
                    /> */}
                      <div className="groups__group-info">
                        <div className="group-info__left">
                          <span className="groups__group-name">
                            {groups[groupNo]["groupName"]}
                          </span>
                          <p className="groups__group-descript">
                            {groups[groupNo]["groupDescription"]}
                          </p>
                        </div>
                        <div className="groups__mem-no-rec">
                          {`${groups[groupNo]["groupMemberNo"]}/${groups[groupNo]["groupMemberMaxNo"]}`}
                        </div>
                      </div>
                    </Link>
                  </li>
                )}
                {groups.length > ++groupNo && (
                  <li>
                    <Link
                      to="/GroupDetail"
                      className="groups__img"
                      style={{
                        position: "relative",
                        backgroundImage: `url(${groups[groupNo]["groupImage"]})`,
                        backgroundSize: "cover",
                      }}
                    >
                      {/* <img
                      className="groups__img"
                      src={
                        groups[groupNo]["groupImage"] === "none"
                          ? "기본이미지"
                          : groups[groupNo]["groupImage"]
                      }
                    /> */}
                      <div className="groups__group-info">
                        <div className="group-info__left">
                          <span className="groups__group-name">
                            {groups[groupNo]["groupName"]}
                          </span>
                          <p className="groups__group-descript">
                            {groups[groupNo]["groupDescription"]}
                          </p>
                        </div>
                        <div className="groups__mem-no-rec">
                          {`${groups[groupNo]["groupMemberNo"]}/${groups[groupNo]["groupMemberMaxNo"]}`}
                        </div>
                      </div>
                    </Link>
                  </li>
                )}
              </ul>
            )}
          </div>
        </section>
        <section id="mainpage__banner">
          {/* 배너섹션 수정사항 없을예정. */}
          <div id="banner__left">
            <div id="mainpage__banner__title">
              WATCH ME는
              <br />
              혼자여도 같이합니다.
            </div>
            <div id="mainpage__banner__description">
              공부만 시작하면 스마트폰에 눈이 팔린다구요?
              <br />
              누가 나좀 감시해줬으면 좋겠다구요?
              <br />
              <br />
              Watch Me와 함께라면 걱정마세요!
              <br />
              혼자만의 시간을 보낼 수도,
              <br /> 그룹을 만들어서 스프린트를 만들수도,
              <br />
              아무와나 함께할 수 있습니다.
            </div>
          </div>
          <div id="mainpage__banner__main">
            의지박약자여,
            <br />
            이리로 모여라.
            <br />
            같이 집중하자👊
          </div>
        </section>
        <section id="mainpage__meeting-rooms">
          {/* 공개미팅룸관련 섹션 연결하는 임시링크잇음. 수정예정. */}
          <div className="section__top">
            <div className="section__top__text">
              활활타오르는
              <br /> 진행중인 공개룸🔥
            </div>
            <Link to="/RoomRecruit">
              <button className="section__top__btn">더보기 &gt;</button>
            </Link>
            {/* <div className="section__top__rec"></div> */}
          </div>
          <div id="mainpage__meeting-rooms__rooms">
            {/*실제 방의 정보를 가져와 연결해줌 조회수가 높은 방이 우선순으로 표기됨*/}
            {!rooms.length && /* 오류나 DB에 룸이 존재하지 않는 경우*/ <></>}
            {rooms.length && (
              <ul className="rooms__whole">
                <li>
                  <div onClick={() => enteringRoom(rooms[0]["id"])}>
                    <article>
                      <div
                        className="group-specs"
                        style={{
                          backgroundImage: `url(${rooms[roomNo]["roomImage"]})`,
                          backgroundSize: "cover",
                        }}
                      >
                        <div className="group-specs__rules">
                          <span>📝 규칙</span>
                          <ul>
                            <li>✔ 휴대폰 인식</li>
                            <li>✔ 얼굴 인식</li>
                            <li>✔ 캠 켜기</li>
                          </ul>
                        </div>
                      </div>

                      <dl className="group-info">
                        <div className="category member-no">
                          <dt className="sr-only">카테고리</dt>
                          <dl>{rooms[roomNo]["roomCategory"]}</dl>
                          <dt className="sr-only">인원수</dt>
                          <dl>
                            &#128509;
                            {rooms[roomNo]["roomMemberNo"]}/
                            {rooms[roomNo]["roomMemberMaxNo"]}
                          </dl>
                        </div>
                        <div>
                          <dt className="sr-only">이름</dt>
                          <dl>{rooms[roomNo]["roomName"]}</dl>
                        </div>
                        <div className="info-content">
                          <dt className="sr-only">세부설명</dt>
                          <dl>{rooms[roomNo]["roomDescription"]}</dl>
                        </div>
                      </dl>
                    </article>
                  </div>
                </li>
                {rooms.length > ++roomNo && (
                  <li>
                    <div onClick={() => enteringRoom(rooms[1]["id"])}>
                      <article>
                        <div
                          className="group-specs"
                          style={{
                            backgroundImage: `url(${rooms[roomNo]["roomImage"]})`,
                            backgroundSize: "cover",
                          }}
                        >
                          <div className="group-specs__rules">
                            <span>📝 규칙</span>
                            <ul>
                              <li>✔ 휴대폰 인식</li>
                              <li>✔ 얼굴 인식</li>
                              <li>✔ 캠 켜기</li>
                            </ul>
                          </div>
                        </div>

                        <dl className="group-info">
                          <div className="category member-no">
                            <dt className="sr-only">카테고리</dt>
                            <dl>{rooms[roomNo]["roomCategory"]}</dl>
                            <dt className="sr-only">인원수</dt>
                            <dl>
                              &#128509;
                              {rooms[roomNo]["roomMemberNo"]}/
                              {rooms[roomNo]["roomMemberMaxNo"]}
                            </dl>
                          </div>
                          <div>
                            <dt className="sr-only">이름</dt>
                            <dl>{rooms[roomNo]["roomName"]}</dl>
                          </div>
                          <div className="info-content">
                            <dt className="sr-only">세부설명</dt>
                            <dl>{rooms[roomNo]["roomDescription"]}</dl>
                          </div>
                        </dl>
                      </article>
                    </div>
                  </li>
                )}
                {rooms.length > ++roomNo && (
                  <li>
                    <div onClick={() => enteringRoom(rooms[2]["id"])}>
                      <article>
                        <div
                          className="group-specs"
                          style={{
                            backgroundImage: `url(${rooms[roomNo]["roomImage"]})`,
                            backgroundSize: "cover",
                          }}
                        >
                          <div className="group-specs__rules">
                            <span>📝 규칙</span>
                            <ul>
                              <li>✔ 휴대폰 인식</li>
                              <li>✔ 얼굴 인식</li>
                              <li>✔ 캠 켜기</li>
                            </ul>
                          </div>
                        </div>

                        <dl className="group-info">
                          <div className="category member-no">
                            <dt className="sr-only">카테고리</dt>
                            <dl>{rooms[roomNo]["roomCategory"]}</dl>
                            <dt className="sr-only">인원수</dt>
                            <dl>
                              &#128509;
                              {rooms[roomNo]["roomMemberNo"]}/
                              {rooms[roomNo]["roomMemberMaxNo"]}
                            </dl>
                          </div>
                          <div>
                            <dt className="sr-only">이름</dt>
                            <dl>{rooms[roomNo]["roomName"]}</dl>
                          </div>
                          <div className="info-content">
                            <dt className="sr-only">세부설명</dt>
                            <dl>{rooms[roomNo]["roomDescription"]}</dl>
                          </div>
                        </dl>
                      </article>
                    </div>
                  </li>
                )}
                {rooms.length > ++roomNo && (
                  <li>
                    <div onClick={() => enteringRoom(rooms[3]["id"])}>
                      <article>
                        <div
                          className="group-specs"
                          style={{
                            backgroundImage: `url(${rooms[roomNo]["roomImage"]})`,
                            backgroundSize: "cover",
                          }}
                        >
                          <div className="group-specs__rules">
                            <span>📝 규칙</span>
                            <ul>
                              <li>✔ 휴대폰 인식</li>
                              <li>✔ 얼굴 인식</li>
                              <li>✔ 캠 켜기</li>
                            </ul>
                          </div>
                        </div>

                        <dl className="group-info">
                          <div className="category member-no">
                            <dt className="sr-only">카테고리</dt>
                            <dl>{rooms[roomNo]["roomCategory"]}</dl>
                            <dt className="sr-only">인원수</dt>
                            <dl>
                              &#128509;
                              {rooms[roomNo]["roomMemberNo"]}/
                              {rooms[roomNo]["roomMemberMaxNo"]}
                            </dl>
                          </div>
                          <div>
                            <dt className="sr-only">이름</dt>
                            <dl>{rooms[roomNo]["roomName"]}</dl>
                          </div>
                          <div className="info-content">
                            <dt className="sr-only">세부설명</dt>
                            <dl>{rooms[roomNo]["roomDescription"]}</dl>
                          </div>
                        </dl>
                      </article>
                    </div>
                  </li>
                )}
                {rooms.length > ++roomNo && (
                  <li>
                    <div onClick={() => enteringRoom(rooms[4]["id"])}>
                      <article>
                        <div
                          className="group-specs"
                          style={{
                            backgroundImage: `url(${rooms[roomNo]["roomImage"]})`,
                            backgroundSize: "cover",
                          }}
                        >
                          <div className="group-specs__rules">
                            <span>📝 규칙</span>
                            <ul>
                              <li>✔ 휴대폰 인식</li>
                              <li>✔ 얼굴 인식</li>
                              <li>✔ 캠 켜기</li>
                            </ul>
                          </div>
                        </div>

                        <dl className="group-info">
                          <div className="category member-no">
                            <dt className="sr-only">카테고리</dt>
                            <dl>{rooms[roomNo]["roomCategory"]}</dl>
                            <dt className="sr-only">인원수</dt>
                            <dl>
                              &#128509;
                              {rooms[roomNo]["roomMemberNo"]}/
                              {rooms[roomNo]["roomMemberMaxNo"]}
                            </dl>
                          </div>
                          <div>
                            <dt className="sr-only">이름</dt>
                            <dl>{rooms[roomNo]["roomName"]}</dl>
                          </div>
                          <div className="info-content">
                            <dt className="sr-only">세부설명</dt>
                            <dl>{rooms[roomNo]["roomDescription"]}</dl>
                          </div>
                        </dl>
                      </article>
                    </div>
                  </li>
                )}
                {rooms.length > ++roomNo && (
                  <li>
                    <div onClick={() => enteringRoom(rooms[5]["id"])}>
                      <article>
                        <div
                          className="group-specs"
                          style={{
                            backgroundImage: `url(${rooms[roomNo]["roomImage"]})`,
                            backgroundSize: "cover",
                          }}
                        >
                          <div className="group-specs__rules">
                            <span>📝 규칙</span>
                            <ul>
                              <li>✔ 휴대폰 인식</li>
                              <li>✔ 얼굴 인식</li>
                              <li>✔ 캠 켜기</li>
                            </ul>
                          </div>
                        </div>

                        <dl className="group-info">
                          <div className="category member-no">
                            <dt className="sr-only">카테고리</dt>
                            <dl>{rooms[roomNo]["roomCategory"]}</dl>
                            <dt className="sr-only">인원수</dt>
                            <dl>
                              &#128509;
                              {rooms[roomNo]["roomMemberNo"]}/
                              {rooms[roomNo]["roomMemberMaxNo"]}
                            </dl>
                          </div>
                          <div>
                            <dt className="sr-only">이름</dt>
                            <dl>{rooms[roomNo]["roomName"]}</dl>
                          </div>
                          <div className="info-content">
                            <dt className="sr-only">세부설명</dt>
                            <dl>{rooms[roomNo]["roomDescription"]}</dl>
                          </div>
                        </dl>
                      </article>
                    </div>
                  </li>
                )}
                {rooms.length > ++roomNo && (
                  <li>
                    <div onClick={() => enteringRoom(rooms[5]["id"])}>
                      <article>
                        <div
                          className="group-specs"
                          style={{
                            backgroundImage: `url(${rooms[roomNo]["roomImage"]})`,
                            backgroundSize: "cover",
                          }}
                        >
                          <div className="group-specs__rules">
                            <span>📝 규칙</span>
                            <ul>
                              <li>✔ 휴대폰 인식</li>
                              <li>✔ 얼굴 인식</li>
                              <li>✔ 캠 켜기</li>
                            </ul>
                          </div>
                        </div>

                        <dl className="group-info">
                          <div className="category member-no">
                            <dt className="sr-only">카테고리</dt>
                            <dl>{rooms[roomNo]["roomCategory"]}</dl>
                            <dt className="sr-only">인원수</dt>
                            <dl>
                              &#128509;
                              {rooms[roomNo]["roomMemberNo"]}/
                              {rooms[roomNo]["roomMemberMaxNo"]}
                            </dl>
                          </div>
                          <div>
                            <dt className="sr-only">이름</dt>
                            <dl>{rooms[roomNo]["roomName"]}</dl>
                          </div>
                          <div className="info-content">
                            <dt className="sr-only">세부설명</dt>
                            <dl>{rooms[roomNo]["roomDescription"]}</dl>
                          </div>
                        </dl>
                      </article>
                    </div>
                  </li>
                )}
              </ul>
            )}
          </div>
        </section>
      </div>
    </>
  );
}

export default MainPage;
