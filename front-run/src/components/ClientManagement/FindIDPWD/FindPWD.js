import React from "react";
import "./FindPWD.css";
import { useContext, useState } from "react";
import { FetchUrl } from "../../../store/communication";
import { Link, useNavigate } from "react-router-dom";

function FindPWD() {
  const url = `${useContext(FetchUrl)}/members/auth/find-pwd`;
  const navigate = useNavigate();

  const [inputs, setInputs] = useState({
    email: "",
    name: "",
  });

  // Function handling submit button
  const submitHandler = (event) => {
    if (inputs.name === "") {
      alert("이름을 입력하세요");
    } else if (inputs.email === "") {
      alert("이메일을 입력하세요");
    } else {
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
          if (result.message === "이메일 입력이 잘못되었습니다.") {
            alert("잘못된 정보입니다");
          } else if (result.message === "EMAIL SEND SUCCESS") {
            alert("이메일로 비빌번호가 전송되었습니다.");
            navigate("/login");
            ////////////////////////////성공시 여기입니다.
          } else {
            alert("ERROR");
          }
        })
        .catch((err) => {
          console.log("ERRROR");
        });
    }
  };
  const handleChange = (event) => {
    const name = event.target.name;
    const value = event.target.value;
    setInputs((values) => ({ ...values, [name]: value }));
  };

  return (
    <div className="login">
      <div className="login-title">
        <div>
          <Link to="/FindID">아이디 찾기</Link>비밀번호 찾기
        </div>
      </div>
      <form className="login-inputs" onSubmit={submitHandler}>
        <input
          className="input-box"
          type="email"
          onChange={handleChange}
          name="email"
          placeholder="이메일주소를 입력하세요"
        />
        <input
          className="input-box"
          type="text"
          name="name"
          onChange={handleChange}
          placeholder="이름을 입력하세요"
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

export default FindPWD;
