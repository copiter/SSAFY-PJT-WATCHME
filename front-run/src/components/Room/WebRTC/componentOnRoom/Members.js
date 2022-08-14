import React from "react";
import {useContext,useState ,useEffect} from "react";
import { getCookie } from "../../../../Cookie";
import { FetchUrl } from "../../../../store/communication";
import "./Members.css";
function Members() {

  const FETCH_URL = useContext(FetchUrl);
  const id=window.location.pathname.split("/")[2].substring(0 );
  const url = `${FETCH_URL}/rooms/`+id+`/members`;



	const [mstudy, setStudy] = useState(
    [{
    "nickName" : "1번후보",
    "penalty" : 10,
    "images" : "img/cat.png"
   
  },
  {
    "nickName" : "2번후보",
    "penalty" : 20,
    "images" : "222"
   
  },{
    "nickName" : "2번후보",
    "penalty" : 20,
    "images" : "222"
   
  },{
    "nickName" : "2번후보",
    "penalty" : 20,
    "images" : "222"
   
  },{
    "nickName" : "2번후보",
    "penalty" : 20,
    "images" : "222"
   
  }])

  
let leader=localStorage.getItem("L"); 
const ban = (event,key) => {
  if(leader){ 


    
  }
};

  
	useEffect(() => { 
  fetch(url, {
      headers: {
        accessToken: getCookie("accessToken"),
      },
    })
   .then((response) => {
      console.log("MemberResTest");
        if (response.ok) {
          console.log("Res OK");
          return response.json(); //ok떨어지면 바로 종료.
        } else {
          response.json().then((data) => {
            console.log("ERROn_ResMem");
            let errorMessage = "";
            throw new Error(errorMessage);
          });
        }
      })
    .then((result) => {
      console.log("ResultOK");
      setStudy(result.responseData.logs);
      console.log(result.responseData.logs);

    })
    .catch((err) => {
      console.log("ERR_Member");
    });
	}, [])





const LST=mstudy.map((name, memNo)=>(
<div key={memNo} className="comps" onClick={event=>ban(event, memNo)}> 

    <div className="images clickDisable"> <img src={mstudy[memNo].images} className="imagesImg" alt="#"/></div>
    <div className="images-infor clickDisable">  
      <div className="nick">{mstudy[memNo].nickName}</div>
      <div className="pen">패널티 : {mstudy[memNo].penalty}</div>
    </div>
</div>));
  return (
    <div className="backDiv">
      <div className="borders">
        <div className="listBoarder">
          <div className="lists">
              {LST}
            </div>
        </div>
          
      </div>
    </div>
  );  
}

export default Members;
