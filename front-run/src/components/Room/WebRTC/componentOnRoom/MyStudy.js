import {  now } from "jquery";
import React from "react";
import {useContext,useState,useEffect } from "react";
import { getCookie } from "../../../../Cookie";
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
  
  const id = window.location.pathname.split("/")[2].substring(0);
  console.log("URL:"+url+"/"+id);

  useEffect(() => {
  
  fetch(url+"/"+id, {
      headers: {
        accessToken: getCookie("accessToken"),
      },
    })
   .then((response) => {
    console.log(response);
        if (response.ok) {
          return response.json(); //ok떨어지면 바로 종료.
        } else {
          response.json().then((data) => {
            let errorMessage = "";
            throw new Error(errorMessage);
          });
        }
      })
      .then((result) => {
        if (result != null) {
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
      <div className="borders">
        <div className="stduyTime">
          공부시간
          <div className="studyTimeTime">{studyTimes/1000/60/60>1?parseInt(studyTimes/1000/60/60)+"시간:":""}{parseInt(studyTimes/1000/60)%60?parseInt(studyTimes/1000/60)%60+"분":""}</div>
        </div>
        <div className="stduypanelty">
          내 패널티
          <div className="studyTimeTime">{study.penalty}</div>
        </div>
        <div className="rule">
          사용중인 규칙
          <div className="studyTimeTime">{study.mode==="MODE1"?"자율":study.mode==="MODE2"?"졸림 감지":study.mode==="MODE3"?"스마트폰 감시":"화면공유 필수"}
          </div>
        </div>
        
      </div>
    </div>
  );
}

export default MyStudy;
