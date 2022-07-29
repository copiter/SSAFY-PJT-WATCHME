import React from "react";
import "./MyPage.css";
import userInfor from "../json/userInfor"
import { Link } from "react-router-dom";

function MyPage() {

  
  let userInformation=userInfor[0]["myUserInfor"][0];

  return (
  <div className="">
    <div className="Left">
      <div>
        <img src="#"/>
        <div>userName</div>
        <div>나에대한 간단한 설명을 적어주세요</div>
      </div>
      <div>
        <div>105000 포인트</div>
        <div>⚡충전하기</div>
        <div>💰전환하기</div>
        <div>⚡사용내역</div>
      </div>
      <div>
        <div>Category</div>
        <div>✏ 공무원 준비</div>
        <div>🥂 [group_name] 소속</div>
      </div>
    </div>
    <div className="Right">
      <div className="Up">
        <div className="subTitle">나의 공부시간</div>
        <div className="studyLeft">그래프</div>
        <div className="studyCenter">
          오늘 공부시간<br/>
          이번 주 공부시간<br/>
          이번 달 공부시간<br/>
          총 공부시간
        </div>
        <div className="studyRight"> 
          {parseInt(userInformation["studyTimeToday"]/60)?parseInt(userInformation["studyTimeToday"]/60)+"시간":""} {userInformation["studyTimeToday"]%60?userInformation["studyTimeToday"]%60+"분":""}<br></br>
          {parseInt(userInformation["studyTimeWeek"]/60)?parseInt(userInformation["studyTimeWeek"]/60)+"시간":""} {userInformation["studyTimeWeek"]%60?userInformation["studyTimeWeek"]%60+"분":""}<br></br>
          {parseInt(userInformation["studyTimeMonth"]/60)?parseInt(userInformation["studyTimeMonth"]/60)+"시간":""} {userInformation["studyTimeMonth"]%60?userInformation["studyTimeMonth"]%60+"분":""}<br></br>
          {parseInt(userInformation["studyTimeTotal"]/60)?parseInt(userInformation["studyTimeTotal"]/60)+"시간":""} {userInformation["studyTimeTotal"]%60?userInformation["studyTimeTotal"]%60+"분":""}
        </div>
      </div>
      <div className="Down">
        <div className="subTitle">나의 패널티</div>
        
        <div className="studyLeft">
          스마트폰<br/>
          자리이탈<br/>
          캠 OFF<br/>
          스터디 불참<br/>
          화면 OFF<br/>
          기타
        </div>
        <div  className="studyCenter">
          그래프
        </div>
        <div className="studyRight">
          <div>MyPage</div>
          <div>포인트 충전하기</div>
          <div>회원정보 수정</div>
          <div>나의 문의내역</div>
          <div>FAQ</div>
          <div>회원탈퇴</div>
        </div>
        
      </div>
    </div>
  </div>
  );
}

export default MyPage;
