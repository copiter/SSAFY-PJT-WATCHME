import React, { useState, Fragment, useRef, useContext } from "react";
import AuthContext from "../../store/auth-context";
import { useNavigate } from "react-router-dom";

import "./SignUp.css";

function SignUp() {
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

  const authCtx = useContext(AuthContext);

  const submitHandler = (event) => {
    event.preventDefault();

    const enteredEmail = emailInputRef.current.value;
    const enteredPassword = passwordInputRef.current.value;
    const enteredUsername = usernameInputRef.current.value;
    const enteredNickname = nicknameInputRef.current.value;
    const enteredSex = sexInputRef.current.value;
    const enteredBirthday = birthdayInputRef.current.value;

    const url =
      "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=AIzaSyA7OgmwziXipTd23RnFtyt6SZ17gqW_V48";
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
        //가지고 오는 토큰 처리 어떻게 할지 넣어야 됩니다.
        authCtx.login(result.refreshToken); //회원가입 시 로그인도 함께 처리
        alert("회원가입 되었습니다");
        navigate("/"); //메인페이지로
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
            <div className="signup-top-title">SIGN UP</div>
          </div>
          <div id="signup-left">
            <div id="signup-left-image"></div>
            <button id="signup-left-addimage">프로필 사진 추가</button>
          </div>
          <div id="signup-right">
            <div id="signup-right-1">
              <input
                className="width70 input"
                type="email"
                placeholder="이메일을 입력하세요"
                required
                ref={emailInputRef}
              />
              <button className="dup">중복확인</button>
            </div>
            <div id="signup-right-2">
              <input
                className="width45 input"
                type="password"
                placeholder="비밀번호를 입력하세요"
                required
                ref={passwordInputRef}
              />
              <input
                className="width45 left10 input"
                type="password"
                placeholder="비밀번호를 다시한번 입력하세요"
              />
            </div>
            <div id="signup-right-3">
              <input
                className="width100 input"
                type="text"
                placeholder="이름을 입력하세요"
                required
                ref={usernameInputRef}
              />
            </div>
            <div id="signup-right-4">
              <input
                className="width70 input"
                type="text"
                placeholder="닉네임을 입력하세요"
                required
                ref={nicknameInputRef}
              />
              <button className="dup">중복확인</button>
            </div>
            <div id="signup-right-5">
              <select
                className="width100 input"
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
            <div id="signup-right-6">
              <input
                className="width100 input"
                type="date"
                placeholder="생년월일을 입력하세요"
                required
                ref={birthdayInputRef}
              />
            </div>
            {/* <div id="signup-right-7">
              <input
                className="width100 input"
                type="tel"
                pattern="[0-9]{3}-[0-9]{4}-[0-9]{4}"
                placeholder="전화번호를 입력하세요"
                required
                ref={phoneNumberInputRef}
              />
            </div> */}
            <div id="signup-right-8">
              <button className="width100 input submitting" type="submit">
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
