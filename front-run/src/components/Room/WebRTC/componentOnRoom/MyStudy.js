import {  now } from "jquery";
import React from "react";
import {useContext,useState,useEffect } from "react";
import { FetchUrl } from "../../../../store/communication";
import "./MyStudy.css";
function MyStudy() {

  const FETCH_URL = useContext(FetchUrl);
  const url = `${FETCH_URL}/rooms`;

	const [study, setStudy] = useState({
    "name" :"123" ,        
    "startTime" : "2022-08-10 14:42",
    "mode" : "MODE1",
    "penalty" : 0
  })
  
  let id=4;
  console.log("URL:"+url+"/"+id);

  useEffect(() => {
  
  fetch(url+"/"+id, {
      headers: {
        //accessToken: getCookie("accessToken"),
      },
    })
   .then((response) => {
    console.log(response);
        if (response.ok) {
          return response.json(); //ok떨어지면 바로 종료.
        } else {
          response.json().then((data) => {
            console.log("ERR");
            let errorMessage = "";
            throw new Error(errorMessage);
          });
        }
      })
      .then((result) => {
        if (result != null) {
          console.log(result);
          console.log(result["responseData"]["room"]);
        setStudy(result["responseData"]["room"])
        }
      })
      .catch((err) => {
        console.log("ERR");
      });
    }, []);
      
let DN=new Date(now()).getTime();
let D1=new Date((study.startTime)).getTime();
let studyTimes=DN-D1;
return (
    <div className="backDiv">
      <div>
        <div>공부시간</div>
        <div>
          {studyTimes/1000/60/60>1?parseInt(studyTimes/1000/60/60)+"시간:":""}
          {parseInt(studyTimes/1000/60)%60?parseInt(studyTimes/1000/60)%60+"분":""}</div>
        <div>내 패널티</div>
        <div>{study.penalty}</div>
        <div>mm분후 쉬는시간입니다,</div>
      </div>
      <div>규칙</div>
      <div>chat</div>
    </div>
  );
}

export default MyStudy;
