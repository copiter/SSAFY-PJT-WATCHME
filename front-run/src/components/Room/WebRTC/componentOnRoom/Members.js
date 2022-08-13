import { CompareSharp } from "@material-ui/icons";
import React from "react";
import {useContext,useState ,useEffect} from "react";
import { useParams } from "react-router-dom";
import { FetchUrl } from "../../../../store/communication";
import "./Members.css";
function Members() {

  const FETCH_URL = useContext(FetchUrl);
  const id=window.location.pathname.split("/")[2].substring(0 );
  console.log("members");
  console.log(id);
  const url = `${FETCH_URL}/rooms/`+id+`/members`;



	const [mstudy, setStudy] = useState(
    [{
    "nickName" : "1234",
    "penalty" : 0,
    "images" : "1234"
   
  },
  {
    "nickName" : "222",
    "penalty" : 0,
    "images" : "222"
   
  }])
  


  
	useEffect(() => { 
  fetch(url, {
      headers: {
        //accessToken: getCookie("accessToken"),
      },
    })
   .then((response) => {
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
      setStudy(result.responseData.log);
    })
    .catch((err) => {
      console.log("ERR");
    });
	}, [])

  let memNo=1;



const LST=mstudy.map((name, memNo)=>(
<div key={memNo} className="comps"> 

  <img src={mstudy[memNo].images} alt="#"/>
  <div>{mstudy[memNo].nickName}</div>
  <div>{mstudy[memNo].penalty}</div>
</div>));
  return (
    <div className="backDiv">
      <div>
        {LST}

      </div>
    </div>
  );  
}

export default Members;
