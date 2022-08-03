import React from "react"; 
import { useState, useContext,useEffect } from "react";
import { Link } from "react-router-dom";

import "./MyPage.css";
import { FetchUrl } from "../../store/communication";

import userInfor from "../json/member.json"



  

function MyPage() {
  const FETCH_URL = useContext(FetchUrl);
  
	const [userInformation, setUserInforMation] = useState(userInfor.responseData.member)
  

  const url = `${FETCH_URL}/members`;


	useEffect(() => {

    function getCookie(name) {
      const cookie = document.cookie
        .split(";")
        .map((cookie) => cookie.split("="))
        .filter((cookie) => cookie[0] === name);
      return cookie[0][1];
    }
    fetch(url,{
      headers: {
        accessToken: getCookie("accessToken"),
      },

    })
   .then((response) => {
     if(response.bodyUsed)
     {
       console.log("재사용됨");

     }
     else if(response.ok)
     {
      return response.json();
     }
     else{

       console.log("ELSE");
     }
   })
   .then((result)=>{
    setUserInforMation(result.responseData.member)
   })
   .catch((err) => {
     console.log("ERROR");
   });
 }, [])




  return (
  <div className="">
    <div className="Left">
      <div>
        <img src={userInformation.profileImage===""||userInformation.profileImage==="none"?"#":userInformation.profileImage} alt="#"/>
        <ul>
          <li>{userInformation.nickName}</li>
          <li>{userInformation.description}</li>
        </ul>
      </div>
      <ul>
        <li>{userInformation.point} 포인트</li>
        <li>⚡충전하기</li>
        <li>💰전환하기</li>
        <li>⚡사용내역</li>
      </ul>
      <ul>
        <li>Category</li>
        <li>✏ 공무원 준비</li>
        <li>🥂 [group_name] 소속</li>
      </ul>
    </div>
    <div className="Right">
      <div className="Up">
        <div className="subTitle">나의 공부시간</div>
        <div className="studyLeft">그래프</div>
        <div className="studyCenter">
          <ul>
            <li>오늘 공부시간</li>
            <li>이번 주 공부시간</li>
            <li>이번 달 공부시간</li>
            <li>총 공부시간</li>
          </ul>
          <ul>{console.log(userInformation)}
            <li>{parseInt(userInformation.studyTimeToday/60)?parseInt(userInformation.studyTimeToday/60)+"시간":""} {userInformation.studyTimeToday%60?userInformation.studyTimeToday%60+"분":""}{userInformation.studyTimeToday%60===""||userInformation.studyTimeToday%60===0?"이제 시작해보죠":""}</li>
            <li>{parseInt(userInformation.studyTimeWeek/60)?parseInt(userInformation.studyTimeWeek/60)+"시간":""} {userInformation.studyTimeWeek%60?userInformation.studyTimeWeek%60+"분":""}</li>
            <li>{parseInt(userInformation.studyTimeMonth/60)?parseInt(userInformation.studyTimeMonth/60)+"시간":""} {userInformation.studyTimeMonth%60?userInformation.studyTimeMonth%60+"분":""}</li>
            <li>{parseInt(userInformation.studyTimeTotal/60)?parseInt(userInformation.studyTimeTotal/60)+"시간":""} {userInformation.studyTimeTotal%60?userInformation.studyTimeTotal%60+"분":""}</li>   
          </ul>
         </div>
        </div>
        <div className="studyRight"> 
          
      </div>
      <div className="Down">
        <div className="subTitle">나의 패널티</div>
        
        <div className="studyLeft">
        <ul>
            <li>스마트폰</li>
            <li>자리이탈</li>
            <li>캠 OFF</li>
            <li>스터디 불참</li>
            <li>화면 OFF</li>
            <li>기타</li>
          </ul>
          <ul>
            <li></li>
            <li></li>
            <li></li>
            <li></li>
            <li></li>
            <li></li>
          </ul>
        </div>
        <div  className="studyCenter">
          그래프
        </div>
        <div className="studyRight">
        <ul>
            <li>MyPage</li>
            <li>포인트 충전하기</li>
            <li>회원정보 수정</li>
            <li>나의 문의내역</li>
            <li>FAQ</li>
            <li>회원탈퇴</li>
          </ul>
        </div>
        
      </div>
    </div>
  </div>
  );
}

export default MyPage;
