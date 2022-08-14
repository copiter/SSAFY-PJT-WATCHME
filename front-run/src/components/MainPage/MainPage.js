import React from "react";
import { useContext, useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import { FetchUrl } from "../../store/communication";
import { getCookie } from "../../Cookie";
import ErrorCode from "../../Error/ErrorCode";

import GroupItem from "../Group/GroupItem";
import Banner from "./Banner";
import RoomItem from "../Room/RoomItem";
import MyStudyInfo from "./MyStudyInfo";

import "./MainPage.css";
import jsons from "../json/main";
import AuthContext from "../../store/auth-context";
import mystudy__infor__tmp from "../../img/Icons/mystudy__infor__tmp.png";

function MainPage() {
  const authCtx = useContext(AuthContext);
  const navigate = useNavigate();
  //URL
  const FETCH_URL = useContext(FetchUrl);
  const url = `${FETCH_URL}/main`;

  let rooms, groups, myGroups, userInformation;

  // const [isLoggedIn, setIsLoggedIn] = useState();
  const isLoggedIn = !!getCookie("accessToken");

  // const [datas, setDatas] = useState(jsons.responseData);
  const [datas, setDatas] = useState({
    myGroups: [],
    member: {},
    rooms: [],
    groups: [],
  });

  useEffect(() => {
    const getDatas = async () => {
      const response = await fetch(url, {
        // credentials: "include",
        headers: {
          accessToken: getCookie("accessToken"),
        },
      });
      const data = await response.json();
      if (data.code === 200) {
        setDatas(data.responseData);
        if (data.responseData.member !== undefined) {
          authCtx.userDataGetter({
            profileImage: data.responseData.member.profileImage,
            nickName: data.responseData.member.nickName,
          });
        }
      }
    };
    getDatas();
  }, []);
  rooms = datas["rooms"];
  groups = datas["groups"];

  if (isLoggedIn) {
    userInformation = datas["member"];
    myGroups = datas["myGroups"];
    // let myNickName = localStorage.setItem(
    //   "nickName",
    //   userInformation["nickName"]
    // );
  }

  console.log(datas);

  function toGroupRecruit() {
    navigate("/GroupRecruit");
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

                  {/*그룹 아무것도 가입안한경우 */}
                  {myGroups.length === 0 && (
                    <>
                      <div id="myGroup-title" onClick={toGroupRecruit}>
                        <span>가입한 그룹이 없습니다. 둘러보세요!</span>
                      </div>
                    </>
                  )}
                  {myGroups.length > 0 && (
                    <ul id="mypage-mygroup">
                      {myGroups.map((group, index) => {
                        return (
                          <li key={index}>
                            <GroupItem
                              group={group}
                              width="160"
                              height="160"
                              myGroup={true}
                            />
                          </li>
                        );
                      })}
                    </ul>
                  )}
                </div>
                <div id="mypage__myinfor__mystudy">
                  {/*내가 공부한 총 공부시간 관련. */}
                  <div className="mypage__myinfor__sub-title">
                    나의 공부시간
                  </div>
                  <Link to="MyPage">
                    <MyStudyInfo
                      userInformation={userInformation}
                      isMainPage={true}
                    />
                  </Link>
                </div>
              </div>
            </div>
          </section>
        )}

        {/* 로그인 유무와 관계 없는 부분 */}
        <section id="mainpage_study-groups">
          {/* 스터디 그룹 */}
          <div className="section__top">
            <div className="section__top__text">
              인기있는
              <br /> 모집한 스터디그룹🥇
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
              <ul id="groups__whole" value={0}>
                {groups.map((group, index) => {
                  return (
                    <li key={index}>
                      <GroupItem group={group} width="278" height="278" />
                    </li>
                  );
                })}
              </ul>
            )}
          </div>
        </section>

        {/* 배너 */}
        <Banner />

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
                {rooms.map((room, index) => {
                  return (
                    <li key={index}>
                      <RoomItem room={room} />
                    </li>
                  );
                })}
              </ul>
            )}
          </div>
        </section>
      </div>
    </>
  );
}

export default MainPage;
