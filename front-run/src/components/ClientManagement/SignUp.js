import React, { useState, Fragment, useRef, useContext } from "react";
import { useNavigate } from "react-router-dom";
import { FetchUrl } from "../../store/communication";

import "./SignUp.css";

function SignUp() {
  const FETCH_URL = useContext(FetchUrl);

  const [selectSex, setSelectSex] = useState("ND");
  const navigate = useNavigate();

  const handleSelectSex = (e) => {
    setSelectSex(e.target.value);
  };

  const emailInputRef = useRef();
  const passwordInputRef = useRef();
  const usernameInputRef = useRef();
  const nicknameInputRef = useRef();
  const sexInputRef = useRef();
  const birthdayInputRef = useRef();

  const submitHandler = (event) => {
    event.preventDefault();

    const enteredEmail = emailInputRef.current.value;
    const enteredPassword = passwordInputRef.current.value;
    const enteredUsername = usernameInputRef.current.value;
    const enteredNickname = nicknameInputRef.current.value;
    const enteredSex = sexInputRef.current.value;
    const enteredBirthday = birthdayInputRef.current.value;

    const url = `${FETCH_URL}/signup`;
    // Interacting with server
    fetch(url, {
      method: "POST",
      body: JSON.stringify({
        email: enteredEmail,
        password: enteredPassword,
        userName: enteredUsername,
        nickname: enteredNickname,
        gender: enteredSex,
        birth: enteredBirthday,
      }),
      headers: {
        "content-type": "application/json",
      },
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else {
          response.json().then((data) => {
            let errorMessage = "Authentication failed!";
            throw new Error(errorMessage);
          });
        }
      })
      .then((result) => {
        alert("회원가입 되었습니다");
        navigate("/login"); //로그인 페이지로
      })
      .catch((err) => {
        alert(err.message);
      });
  };

  return (
    <Fragment>
      <div className="signup">
        <form onSubmit={submitHandler}>
          <div className="signup-top">
            <div className="signup-top__word">SIGN UP</div>
          </div>
          <div className="signup-form">
            <div className="signup-left">
              <div className="signup-left-image"></div>
              <button className="signup-left-addimage">프로필 사진 추가</button>
            </div>
            <div className="signup-right">
              <div className="line">
                <input
                  className="short"
                  type="email"
                  placeholder="이메일을 입력하세요"
                  required
                  ref={emailInputRef}
                />
                <button className="dup">중복확인</button>
              </div>
              <div className="line">
                <input
                  className="half"
                  type="password"
                  placeholder="비밀번호를 입력하세요"
                  required
                  ref={passwordInputRef}
                />
                <input
                  className="half"
                  type="password"
                  placeholder="비밀번호를 다시한번 입력하세요"
                />
              </div>
              <div className="line">
                <input
                  type="text"
                  placeholder="이름을 입력하세요"
                  required
                  ref={usernameInputRef}
                />
              </div>
              <div className="line">
                <input
                  className="short"
                  type="text"
                  placeholder="닉네임을 입력하세요"
                  required
                  ref={nicknameInputRef}
                />
                <button className="dup">중복확인</button>
              </div>
              <div className="line">
                <select
                  name="sex"
                  placeholder="성별을 입력하세요"
                  onChange={handleSelectSex}
                  value={selectSex}
                  required
                  ref={sexInputRef}
                >
                  <option value="M">남</option>
                  <option value="F">녀</option>
                  <option value="ND">공개안함</option>
                </select>
              </div>
              <div className="line">
                <input
                  type="date"
                  placeholder="생년월일을 입력하세요"
                  required
                  min="1900-01-01"
                  max="2022-12-31"
                  ref={birthdayInputRef}
                />
              </div>
              <button className="submitting" type="submit">
                회원가입
              </button>
            </div>
          </div>
        </form>
      </div>
    </Fragment>
  );
}

export default SignUp;
