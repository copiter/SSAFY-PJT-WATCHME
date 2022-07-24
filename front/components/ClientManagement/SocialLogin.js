import React, { useState, Fragment, useRef, useContext } from "react";
import AuthContext from "../../store/auth-context";
import { useNavigate } from "react-router-dom";

import "./SignUp.css"; // 동일한 CSS 파일 사용

function SocialLogin() {
  const [selectSex, setSelectSex] = useState("ND");
  const navigate = useNavigate();

  const handleSelectSex = (e) => {
    setSelectSex(e.target.value);
  };

  // const emailInputRef = useRef();
  // const passwordInputRef = useRef();
  const usernameInputRef = useRef();
  const nicknameInputRef = useRef();
  const sexInputRef = useRef();
  const birthdayInputRef = useRef();
  // const phoneNumberInputRef = useRef();

  const authCtx = useContext(AuthContext);

  const submitHandler = (event) => {
    event.preventDefault();

    // const enteredEmail = emailInputRef.current.value;
    // const enteredPassword = passwordInputRef.current.value;
    const enteredUsername = usernameInputRef.current.value;
    const enteredNickname = nicknameInputRef.current.value;
    const enteredSex = sexInputRef.current.value;
    const enteredBirthday = birthdayInputRef.current.value;
    // const enteredPhoneNumber = phoneNumberInputRef.current.value;

    const url =
      "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=AIzaSyA7OgmwziXipTd23RnFtyt6SZ17gqW_V48";
    // Interacting with server
    fetch(url, {
      method: "POST",
      // withCredentials: true,
      // credentials: "include",
      body: JSON.stringify({
        // email: enteredEmail,
        // password: enteredPassword,
        username: enteredUsername,
        nickname: enteredNickname,
        sex: enteredSex,
        birthday: enteredBirthday,
        // phoneNumber: enteredPhoneNumber,
      }),
      headers: {
        "content-type": "application/json",
        // authorization: `Bearer ${authCtx.accessToken}`,
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
        authCtx.accessToken = result.accessToken;
        authCtx.login(authCtx.accessToken);
        alert("로그인 되었습니다");
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
            <button id="signup-left-addimage" disabled>
              프로필 사진 추가
            </button>
          </div>
          <div id="signup-right">
            <div id="signup-right-1">
            
            </div>
            <div id="signup-right-2">
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

export default SocialLogin;
