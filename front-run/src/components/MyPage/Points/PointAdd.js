import React from "react";
import { useContext,useState } from "react";
import { FetchUrl } from "../../../store/communication";

import "./PointAdd.css";
function PointAdd() {
  const FETCH_URL = useContext(FetchUrl);
  const url = `${FETCH_URL}/points/kakao?`;
  const [inputs, setInputs] = useState({
    valueSelect: "1000",
    valueInputs:""
  });
  function getCookie(name) {
    const cookie = document.cookie
      .split(";")
      .map((cookie) => cookie.split("="))
      .filter((cookie) => cookie[0] === name);
    return cookie[0][1];
  }

  const handleSubmit = (event) => {
    event.preventDefault();
    console.log(url+"value="+(inputs.valueSelect==="Free"?inputs.valueInputs:inputs.valueSelect));
    fetch(url+"value="+(inputs.valueSelect==="Free"?inputs.valueInputs:inputs.valueSelect), 
    {
      method: "POST",
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
          window.location.href =result.responseData.REDIRECT_URL;
          localStorage.setItem("tid",result.responseData.tid);
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
      <form className="kakaopay-form-frame" onSubmit={handleSubmit}>
        
        <div className="return-btn"><input type="button" value='돌아가기' /> </div>
        <div className='selection'>💎결제수단을 선택하세요</div>
        <div className='kakaopay-pay1'>
        <input type="button" className='kakaopay-pay-item' value='카카오페이' />
        <input type="button" className='kakaopay-pay-item' value='신용카드' />
          <input type="button" className='kakaopay-pay-item' value='휴대폰' />
          </div>
        <div className='kakaopay-pay1'>
        <input type="button" className='kakaopay-pay-item' value='계좌이체' />      
        <input type="button" className='kakaopay-pay-item' value='기타' />
        <input type="button" className='kakaopay-pay-item' value='네이버페이' />
        </div>
        <div className='kakaopay-money'>💰 결제 금액<input placeholder='금액을 입력해주세요.'/></div>
        <div className='money-selection'>충전할 금액을 선택하세요</div>        
        <div className='kakaopay-money-btn'>
          <input value='+ 1만원' type="button" className='kakaopay-money-btn-item'  />
        <input type="button" className='kakaopay-money-btn-item' value='+ 3만원'></input>
        <input type="button" className='kakaopay-money-btn-item' value='+ 5만원'></input>
          <input type="button" className='kakaopay-money-btn-item' value='+ 10만원'></input>
          </div>
        <input className='kakaopay-charge-btn' type="submit" value='충전'></input>
      </form>
    </>
  );
}

export default PointAdd;
