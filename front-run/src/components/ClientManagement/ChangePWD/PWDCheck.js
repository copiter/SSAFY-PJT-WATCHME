import React from "react";
import "./PWDCheck.css";
import { useContext, useState } from "react";
import { FetchUrl } from "../../../store/communication";
import { Link, useNavigate } from "react-router-dom";

function PWDCheck() {
  const url = `${useContext(FetchUrl)}/reset-ddd`;
  const navigate = useNavigate();

  const [inputs, setInputs] = useState({
    pwd: "",
  });

  // Function handling submit button
  const submitHandler = (event) => {
    event.preventDefault();
    if (inputs.pwd === inputs.check) {
      fetch(url, {
        method: "POST",
        body: JSON.stringify({
          password: inputs.pwd,
        }),
        headers: {
          "content-type": "application/json",
        },
      })
        .then((response) => {
          return response.json();
        })
        .then((result) => {
          console.log(result);
          if (result.message === "RESET PASSWORD FAIL") {
            alert("잘못된 정보입니다");
          } else if(result.message==="FIND EMAIL SUCCESS") {
            ////////////////////////////성공시 여기입니다.
          }
          else{
            alert("오류입니다.");
          }
        })
        .catch((err) => {
          console.log("ERRROR");
        });
    }else
    {
      alert("비밀번호가 서로 다릅니다.");
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
          비밀번호 변경페이지입니다
        </div>
      </div>
      <form className="login-inputs" onSubmit={submitHandler}>
        <input
          className="input-box"
          type="password"
          onChange={handleChange}
          name="pwd"
          placeholder="비밀번호를 입력하세요"
        />
        <input
          className="input-box"
          type="password"
          name="check"
          onChange={handleChange}
          placeholder="비밀번호를 다시 입력하세요"
        />
        <button className="submit-btn">확인</button>
      </form>

      <div className="signup-btns">
        <Link to="/login">
          <div className="email"> 메인 페이지로</div>
        </Link>
      </div>
    </div>
  );
}

export default PWDCheck;
