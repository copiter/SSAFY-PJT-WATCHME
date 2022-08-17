import React from "react";
import { useState, useContext, useEffect } from "react";
import { Link } from "react-router-dom";
import { getCookie } from "../../Cookie";
import { FetchUrl } from "../../store/communication";
import { Chart } from "react-google-charts";
import ErrorCode from "../../Error/ErrorCode";

import "./MyPage.css";
import userInfor from "../json/member.json";

import MyStudyInfo from "../MainPage/MyStudyInfo";

function MyPage() {
  const FETCH_URL = useContext(FetchUrl);

  //로컬 테스트 시
  const [info, setInfo] = useState(userInfor.responseData);
  //실제 통신시
  // const [info, setInfo] = useState({
  //   studyByDay: [],
  //   penalty: {},
  //   member: {},
  //   penaltyByDay: [],
  //   rules: [],
  // });

  let studydata = [["Day", "공부시간"]];
  let studydataPen = [["Day", "공부패널티"]];
  let timeChartdata = [["Task", "stutytimes"]];

  const options_studydataPen = {
    title: "나의 공부패널티 그래프",
    curveType: "function",
    legend: { position: "none" },
    width: "650",
    height: "300",
    positon: "absolue",
  };
  const options = {
    title: "나의 공부시간 그래프",
    curveType: "function",
    legend: { position: "none" },
    width: "700",
    height: "220",
    positon: "absolue",
  };
  const options_timeChartdata = {
    curveType: "function",
    pieHole: 0.4,
  };

  for (let i = 0; i < info.studyByDay.length; i++) {
    studydata[i + 1] = [i + 1 + "일", info.studyByDay[i]];
  }

  for (let i = 0; i < info.penaltyByDay.length; i++) {
    studydataPen[i + 1] = [i + 1 + "일", info.penaltyByDay[i]];
  }

  timeChartdata[1] = ["오늘공부량시간", info.member.studyTimeToday];
  timeChartdata[2] = ["이번주 공부시간", info.member.studyTimeWeek];

  const url = `${FETCH_URL}/members`;
  // useEffect(() => {
  //   fetch(url, {
  //     headers: {
  //       accessToken: getCookie("accessToken"),
  //     },
  //   })
  //     .then((response) => response.json())
  //     .then((result) => {
  //       if (result.code === 200) {
  //         setInfo(result.responseData);
  //       } else {
  //         ErrorCode(result);
  //       }
  //     })
  //     .catch((err) => {
  //       console.log(err);
  //     });
  // }, []);

  console.log(info);

  return (
    <div id="mypage">
      <div id="mypage__left">
        <div id="mypage__profile">
          <img
            src={info.member.profileImage}
            alt="프로필 사진"
            id="mypage__profile__img"
          />
          <span id="mypage__profile__name">{info.member.nickName}</span>
        </div>
        <div id="mypage__point">
          <span>
            <b>보유 포인트</b>
            {` : ${info.member.point}`}
          </span>
          <ul id="point-tabs">
            <li>
              <Link to="/PointAdd">⚡ 충전하기</Link>
            </li>
            <li>
              <Link to="./PointRefund">💰 전환하기</Link>
            </li>
            <li>
              <Link to="./PointUsed">⚡ 사용내역</Link>
            </li>
          </ul>
        </div>
      </div>
      <div id="mypage__right">
        <div id="mypage__top">
          <div id="my-study-time__number">
            {/* <Chart
              chartType="PieChart"
              data={timeChartdata}
              options={options_timeChartdata}
              width={"100%"}
              height={"400px"}
            /> */}
            <strong>나의 공부량</strong>
            <MyStudyInfo userInformation={info.member} isMainPage={false} />
          </div>
          <div id="my-study-time__graph">
            <strong>내 공부시간 그래프</strong>
            <Chart
              chartType="LineChart"
              width="100%"
              height="400px"
              data={studydata}
              options={options}
            />
          </div>
        </div>
        <div id="mypage__bottom">
          <div id="mypage__penalty-wrap">
            <div id="mypage__penalty">
              <strong>내 패널티 그래프</strong>
              <ul id="penalty__specific">
                <li>
                  <small>😴 졸림 감지</small>
                  <span>{!!info.penalty.MODE2 ? info.penalty.MODE2 : 0}회</span>
                </li>
                <li>
                  <small>📱 스마트폰 감시</small>
                  <span>{!!info.penalty.MODE3 ? info.penalty.MODE3 : 0}회</span>
                </li>
                <li>
                  <small>🖥 화면공유</small>
                  <span>{!!info.penalty.MODE4 ? info.penalty.MODE4 : 0}회</span>
                </li>
              </ul>
            </div>
            <Chart
              chartType="LineChart"
              data={studydataPen}
              options={options_studydataPen}
            />
          </div>
          <div id="mypage__setting">
            <strong>MyPage</strong>
            <ul id="Links">
              <li>
                <Link to="./PointAdd">포인트 충전하기</Link>
              </li>
              <li>
                <a href="#">회원정보 수정</a>
              </li>
              <li>
                <a href="#">나의 문의내역</a>
              </li>
              <li>
                <a href="#">회원탈퇴</a>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
}

export default MyPage;
