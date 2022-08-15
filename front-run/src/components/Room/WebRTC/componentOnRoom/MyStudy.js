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
    "mode" : "MODE2",
    "penalty" : 13
  })
  
  const id = window.location.pathname.split("/")[2].substring(0);
  console.log("URL:"+url+"/"+id);

  useEffect(() => {
  
    console.log('TESTHERE');
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
      
    



const [studyTimes, setStudyTimes] = useState(
0)


useEffect(() => {
  const id = setInterval(() => {
    let DN=new Date(now()).getTime();
    setStudyTimes(DN-D1);
  }, 30000);
  return () => clearInterval(id);
}, 10000);

let D1=new Date((study.startTime)).getTime();
console.log(studyTimes);
let hours=studyTimes/1000/60/60,minutes=studyTimes/1000/60%60;
return (
  <div className="backDiv">
    <div className="borders"><div className="borders-inner">
      <div className="clock">
        <div className="stduyTime">
          공부시간
        </div>
        <div className="studyTimeTime">
        {hours>=10? parseInt(hours):
        hours>=1?("0"+parseInt(hours)):
        "00"}
        :{minutes>=10? parseInt(minutes):
        minutes>=1?("0"+parseInt(minutes)):
        "00"}
        </div>
        
      </div>
      <div className="otherInformaations">
        <div className="rule">
          📝 적용중인 규칙
          <div className="studyRuleRule">{study.mode==="MODE1"?"자율":study.mode==="MODE2"?"졸림 감지":study.mode==="MODE3"?"스마트폰":"화면공유"}
          <div className="studyPanemtyPanelty">{study.mode!=="MODE1"?study.penalty:""}</div>
        </div>
      
      </div>
      </div>
      
      
      
    </div></div>
  </div>
);
}

export default MyStudy;
