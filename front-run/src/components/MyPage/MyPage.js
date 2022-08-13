import React from "react";
import { useState, useContext, useEffect } from "react";
import { Link } from "react-router-dom";

import "./MyPage.css";
import { FetchUrl } from "../../store/communication";
import { Chart } from "react-google-charts";

import userInfor from "../json/member.json";

function MyPage() {
  const FETCH_URL = useContext(FetchUrl);
  let data = [
    ["Day", "공부시간"],
    ["2004", 1000],
    ["2005", 1170],
    ["2006", 660],
    ["2007", 1030],
  ];

  const [userInformation, setUserInforMation] = useState(
    userInfor.responseData
  );

  let studydata = [["Day", "공부시간"]];

  let studydataPen = [["Day", "공부패널티"]];

  let timeChartdata = [["Task", "stutytimes"]];

  const options_studydataPen = {
    title: "나의 공부패널티 그래프",
    curveType: "function",
    legend: { position: "none" },
    width: "500",
    height: "250",
    positon: "absoulte",
  };
  const options = {
    title: "나의 공부시간 그래프",
    curveType: "function",
    legend: { position: "none" },
    width: "500",
    height: "250",
    positon: "absoulte",
  };
  const options_timeChartdata = {
    curveType: "function",
    pieHole: 0.4,
  };
  for (let i = 0; i < userInformation.studyByDay.length; i++) {
    studydata[i + 1] = [(i+1) + "일", userInformation.studyByDay[i]];
  }

  for (let i = 0; i < userInformation.penaltyByDay.length; i++) {
    studydataPen[i + 1] = [(i+1) + "일", userInformation.penaltyByDay[i]];
  }

  timeChartdata[1] = ["오늘공부량시간", userInformation.member.studyTimeToday];
  timeChartdata[2] = ["이번주 공부시간", userInformation.member.studyTimeWeek];
  console.log(timeChartdata[1]);
  console.log("TST");
  const url = `${FETCH_URL}/members`;

  function getCookie(name) {
    const cookie = document.cookie
      .split(";")
      .map((cookie) => cookie.split("="))
      .filter((cookie) => cookie[0] === name);
    return cookie[0][1];
  }
  console.log("START");
  console.log(getCookie("accessToken"));
  useEffect(() => {
    console.log("EFFECT");
    fetch(url, {
      headers: {
        accessToken: getCookie("accessToken"),
      },
    })
      .then((response) => {
        console.log(response);
        if (response.bodyUsed) {
          console.log("재사용됨");
        } else if (response.ok) {
          return response.json();
        } else {
          console.log("ELSE");
        }
      })
      .then((result) => {
        setUserInforMation(result.responseData);
      })
      .catch((err) => {
        console.log("ERROR");
      });
  }, []);

  console.log(userInformation.member);
  console.log(userInformation.penalty);
  console.log(userInformation.penaltyByDay); //이번달 패널티(일단위)
  console.log(userInformation.rules);
  console.log(userInformation.studyByDay); //이번달 공부시간(일단위)
  return (
    <div className="out">
      <div className="Left">
        <div className="Left_Inner">
          <div className="profileImg">
            <img
              src={
                userInformation.member.profileImage === "" ||
                userInformation.member.profileImage === "none"
                  ? "#"
                  : userInformation.member.profileImage
              }
              alt="#"
              className="profileImg_Img"
            />
            <div className="profileDisc">
              <div className="nickName">{userInformation.member.nickName}</div>
              <div className="description">
                {userInformation.member.description}
              </div>
            </div>
          </div>
          <div className="LeftBottom">
            <div className="Point">
              <div className="RealPoint">
                {userInformation.member.point} 포인트
                <br />
              </div>
              <Link to="/PointAdd">
                ⚡충전하기
                <br />
              </Link>
              <Link to="./PointRefund">
                💰전환하기
                <br />
              </Link>
              <Link to="./PointUsed">
                ⚡사용내역
                <br />
              </Link>
            </div>
            <div className="Category">
              Category
              <div className="CategoryHover">
                ✏ 공무원 준비
                <br />
                🥂 [group_name] <br />
                소속
              </div>
            </div>
          </div>
        </div>
      </div>
      <div className="Right">
        <div className="Up">
          <div className="Title">나의 공부시간</div>
          <div className="inner_Down">
            <div className="studyLeft">
              <div className="donutchart">
                <Chart
                  chartType="PieChart"
                  data={timeChartdata}
                  options={options_timeChartdata}
                  width={"100%"}
                  height={"400px"}
                />
              </div>
            </div>
            <div className="studyCenter">
              <div className="subtitle">이번주 나의 공부량</div>
                <div className="MyStduy">
                  <div className="MyStduy1">
                    <div>오늘 공부시간</div>
                    <div>이번 주 공부시간</div>
                    <div>이번 달 공부시간</div>
                    <div>총 공부시간</div>
                  </div>
                  <div className="MyStduy2">
                    <div>
                      {parseInt(userInformation.member.studyTimeToday / 60)
                        ? parseInt(userInformation.member.studyTimeToday / 60) +
                          "시간"
                        : ""}{" "}
                      {userInformation.member.studyTimeToday % 60
                        ? (userInformation.member.studyTimeToday % 60) + "분"
                        : ""}
                      {userInformation.member.studyTimeToday % 60 === "" ||
                      userInformation.member.studyTimeToday % 60 === 0
                        ? "0분"
                        : ""}
                    </div>
                    <div>
                      {parseInt(userInformation.member.studyTimeWeek / 60)
                        ? parseInt(userInformation.member.studyTimeWeek / 60) +
                          "시간"
                        : ""}{" "}
                      {userInformation.member.studyTimeWeek % 60
                        ? (userInformation.member.studyTimeWeek % 60) + "분"
                        : ""}
                    </div>
                    <div>
                      {parseInt(userInformation.member.studyTimeMonth / 60)
                        ? parseInt(userInformation.member.studyTimeMonth / 60) +
                          "시간"
                        : ""}{" "}
                      {userInformation.member.studyTimeMonth % 60
                        ? (userInformation.member.studyTimeMonth % 60) + "분"
                        : ""}
                    </div>
                    <div>
                      {parseInt(userInformation.member.studyTimeTotal / 60)
                        ? parseInt(userInformation.member.studyTimeTotal / 60) +
                          "시간"
                        : ""}{" "}
                      {userInformation.member.studyTimeTotal % 60
                        ? (userInformation.member.studyTimeTotal % 60) + "분"
                        : ""}
                    </div>
                  </div>
              </div>
            </div>
            <div className="studyRight">
              <div className="char_out">
                <div className="subtitle">내 공부시간 그래프</div>
              </div>
              <div className="chart">
                <Chart
                  chartType="LineChart"
                  width="100%"
                  height="400px"
                  data={studydata}
                  options={options}
                />
              </div>
            </div>
          </div>
        </div>
        <div className="Down">
          <div className="Title">나의 패널티</div>
          <div className="inner_Down">
            <div className="studyLeft">
              <div className="pens">
                <ul className="pen1">
                  <li>스마트폰감지</li>
                  <li>졸음감지</li>
                  <li>화면공유</li>
                </ul>
                <ul className="pen1">
                  <li> {userInformation.penalty.MODE1}</li>
                  <li> {userInformation.penalty.MODE2}</li>
                  <li> {userInformation.penalty.MODE3}</li>
                </ul>
              </div>
            </div>
            <div className="studyCenter">
              <div className="subtitle">그래프</div>
              <div className="chart">
                <Chart
                  chartType="LineChart"
                  data={studydataPen}
                  options={options_studydataPen}
                />
              </div>
            </div>
            <div className="studyRight">
              <div className="subtitle">MyPage</div>
              <ul className="Links">
                <li>
                  <Link to="./PointAdd">포인트 충전하기</Link>
                </li>
                <li>회원정보 수정</li>
                <li>나의 문의내역</li>
                {/*<li>FAQ</li>*/}
                <li>회원탈퇴</li>
              </ul>
            </div>
          </div>
        </div>
        <div className="inner_Down"></div>
      </div>
    </div>
  );
}

export default MyPage;
