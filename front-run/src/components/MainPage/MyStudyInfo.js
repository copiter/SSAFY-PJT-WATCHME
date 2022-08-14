import React from "react";

import "./MyStudyInfo.css";

function MyStudyInfo(props) {
  const userInformation = props.userInformation;

  return (
    <div id="mypage__myinfor__mystudy__infor">
      {props.isMainPage && <div id="fighting-text">오늘도 파이팅!</div>}
      <ul id="mypage__myinfor__mystudy__infor__text">
        <li>
          <span className="mystudy__infor__text-left">오늘 공부시간</span>
          <span>
            {`${parseInt(userInformation["studyTimeToday"] / 60)}시간 ${
              userInformation["studyTimeToday"] % 60
            }분`}
          </span>
        </li>
        <li>
          <span className="mystudy__infor__text-left">이번주 공부시간</span>
          <span>
            {`${parseInt(userInformation["studyTimeWeek"] / 60)}시간 ${
              userInformation["studyTimeWeek"] % 60
            }분`}
          </span>
        </li>
        <li>
          <span className="mystudy__infor__text-left">이번달 공부시간</span>
          <span>
            {`${parseInt(userInformation["studyTimeMonth"] / 60)}시간 ${
              userInformation["studyTimeMonth"] % 60
            }분`}
          </span>
        </li>
        <li>
          <span className="mystudy__infor__text-left">총 공부시간</span>
          <span>
            {`${parseInt(userInformation["studyTimeTotal"] / 60)}시간 ${
              userInformation["studyTimeTotal"] % 60
            }분`}
          </span>
        </li>
      </ul>
    </div>
  );
}

export default MyStudyInfo;
