import React from "react";
import { useContext,useState } from "react";
import { FetchUrl } from "../../../store/communication";

import "./PointRefund.css";
function PointRefund() {

  const FETCH_URL = useContext(FetchUrl);
  const url = `${FETCH_URL}/points/return?`;
  const [inputs, setInputs] = useState({
    valueSelect: "1000",
    valueInputs:""
  });

  const handleSubmit = (event) => {
    event.preventDefault();

    function getCookie(name) {
      const cookie = document.cookie
        .split(";")
        .map((cookie) => cookie.split("="))
        .filter((cookie) => cookie[0] === name);
      return cookie[0][1];
    }
    
    console.log(url+"value="+(inputs.valueSelect==="Free"?inputs.valueInputs:inputs.valueSelect));
    fetch(url+"value="+(inputs.valueSelect==="Free"?inputs.valueInputs:inputs.valueSelect)
      , {
      method: "POST",
      headers: {
        accessToken: getCookie("accessToken"),
      },
    })
      .then((response) => {
        if (response.ok) {
          console.log("C1");
          console.log(response);
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
          window.location.reload(); //리다이렉션관련
        }
      })
      .catch((err) => {
        console.log("ERR");
      });
  };


  const [isShown,setIsShown]=useState(false)
  const handleChange=(event)=>{
    const name = event.target.name;
    const value = event.target.value;
    setInputs((values) => ({ ...values, [name]: value }));
    if(value==="Free"&&name==="valueSelect")
    {
      //valueInputs비허용
      setIsShown(true);
    }
    else if(value!=="Free"&&name==="valueSelect"){

      setIsShown(false);
    }
  }




  return (
    <>
      <div className="Refund">
        <form onSubmit={handleSubmit}>
          <div>환불하실 금액을 입력하세요</div>
          <select onChange={handleChange} name="valueSelect">
            <option value="1000" >1,000</option>
            <option value="5000" >5,000</option>
            <option value="10000" >10,000</option>
            <option value="20000" >20,000</option>
            <option value="50000" >50,000</option>
            <option value="100000" >10,0000</option>
            <option value="Free" >직접입력</option>
          </select>
          {isShown&&<input type="number"  className="dirInput" name="valueInputs" onChange={handleChange}/>}
          <div><button type="submit">제출</button></div>
        </form>
      </div>
    </>
  );
}

export default PointRefund;
