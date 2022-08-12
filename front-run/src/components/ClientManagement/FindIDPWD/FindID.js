import React from "react";
import "./FindID.css";
import { useContext, useState } from "react";
import { FetchUrl } from "../../../store/communication";
import { Link, useNavigate } from "react-router-dom";

function FindID() {
  const url = `${useContext(FetchUrl)}/find-email`;
  const navigate = useNavigate();

  const [inputs, setInputs] = useState({
    name: "",
    nickName: "",
  });

  // Function handling submit button
  const submitHandler = (event) => {
    console.log(
      JSON.stringify({
        name: inputs.name,
        nickName: inputs.nickName,
      })
    );
    event.preventDefault();
    fetch(url, {
      method: "POST",
      body: JSON.stringify({
        name: inputs.name,
        nickName: inputs.nickName,
      }),
      headers: {
        "content-type": "application/json",
      },
    })
      .then((response) => {
        console.log(response);
        return response.json();
      })
      .then((result) => {
        console.log(result);
        if (result.message === "FIND EMAIL FAIL") {
          alert("잘못된 정보입니다");
        } else if (result.message === "FIND EMAIL SUCCESS") {
          alert("아이디 : " + result.responseData.email);
          navigate("/login");
          ////////////////////////////성공시 여기입니다.
        } else {
          alert("오류입니다.");
        }
      })
      .catch((err) => {
        console.log("ERRROR");
      });
  };
  const handleChange = (event) => {
    const name = event.target.name;
    const value = event.target.value;
    setInputs((values) => ({ ...values, [name]: value }));
  };

  return (
    <div className="login">
      <div className="login-title">
        아이디찾기
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
          placeholder="이름을 입력하세요"
        />
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
  );
}

export default FindID;
