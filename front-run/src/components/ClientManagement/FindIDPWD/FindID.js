import React from "react"; 
import "./FindID.css";
import {  useContext ,useState} from "react";
import { FetchUrl } from "../../../store/communication";
import { Link } from "react-router-dom";

  
function FindID() {

  const url = `${useContext(FetchUrl)}/findID`;


  const [inputs, setInputs] = useState({
    myName: "",
    phoneNumber: 1,
  });



  // Function handling submit button
  const submitHandler = (event) => {
    event.preventDefault();
    console.log(url);
    console.log([JSON.stringify(inputs)]);
    fetch(url,{
      method:"POST",
      body:[JSON.stringify(inputs)],
      headers:{type: "application/json"
      }
    })
    .then((response)=>{

    })
    .catch((err)=>{
      console.log("ERRROR");
    });
   
  };
  const handleChange = (event) => {
    const name = event.target.name;
    const value = event.target.value;
    setInputs((values) => ({ ...values, [name]: value }));
  };




  return(

    <div className="login">
        <div className="login-title">아이디찾기
          <div>
            <Link to="/FindPWD">비밀번호 찾기</Link>
          </div>
      </div>
      <form className="login-inputs" onSubmit={submitHandler}>
        <input
            className="input-box"
            type="text"
            name="myName"
            onChange={handleChange}
            placeholder="이름을 입력하세요" /> 
        <input
          className="input-box"
          type="number"
          onChange={handleChange}
          name="phoneNumber"
          placeholder="전화번호를 입력하세요"
          accept="number"
          />
        <button className="submit-btn">확인</button>
      </form>

      <div className="signup-btns">
        <Link to="/login">
          <div className="email"> 로그인 페이지로</div>
        </Link>
          
      </div>
    </div>
  )
}

export default FindID;
