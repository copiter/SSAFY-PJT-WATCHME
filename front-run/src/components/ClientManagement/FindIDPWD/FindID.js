import React from "react"; 
import "./FindID.css";
import {  useContext ,useState} from "react";
import { FetchUrl } from "../../../store/communication";
import { Link } from "react-router-dom";

  
function FindID() {

  const url = `${useContext(FetchUrl)}/find-email`;


  const [inputs, setInputs] = useState({
    name: "",
    nickName: "",
  });



  // Function handling submit button
  const submitHandler = (event) => {
    event.preventDefault();

    fetch(url+"?name="+inputs.name+"&nickName="+inputs.nickName)
    .then((response)=>{
      return response.json();

    })
    .then((result)=>{
      console.log(url+"?name="+inputs.name+"&nickName="+inputs.nickName);
      console.log(result);

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
            name="name"
            onChange={handleChange}
            placeholder="이름을 입력하세요" /> 
        <input
          className="input-box"
          type="text"
          onChange={handleChange}
          name="nickName"
          placeholder="닉네임을 입력하세요"
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
