import React, { useState, Fragment, useRef, useContext } from "react";
import { FetchUrl } from "../../store/communication";
import AuthContext from "../../store/auth-context";
import { useNavigate } from "react-router-dom";

import "./SocialLogin.css"; // 동일한 CSS 파일 사용

function SocialLogin() {
  const FETCH_URL = useContext(FetchUrl);

  const [selectSex, setSelectSex] = useState("ND");
  const navigate = useNavigate();

  const handleSelectSex = (e) => {
    setSelectSex(e.target.value);
  };

  const nameInputRef = useRef();
  const nickNameInputRef = useRef();
  const sexInputRef = useRef();
  const birthdayInputRef = useRef();

  const authCtx = useContext(AuthContext);

  const submitHandler = (event) => {
    event.preventDefault();

    const enteredName = nameInputRef.current.value;
    const enteredNickname = nickNameInputRef.current.value;
    const enteredSex = sexInputRef.current.value;
    const enteredBirthday = birthdayInputRef.current.value;

    //쿠키 가져오기
    //쿠키 가져오기
    let cookie;
    function getCookie() {
      const cookie_array = document.cookie
        .split(";")
       console.log(cookie_array);
    }

    const url = "http://localhost:8080/social-signup";
    // Interacting with server
    fetch(url, {
      method: "POST",
      withCredentials: true,
      body: JSON.stringify({
        name: enteredName,
        nickName: enteredNickname,
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
            let errorMessage = "인증 실패";
            throw new Error(errorMessage);
          });
        }
      })
      .then((result) => {
        if (result != null) {
          authCtx.login();
          alert("로그인 되었습니다");
          navigate("/");
          window.location.reload();
        }
        
      })
      .catch((err) => {
        alert(err.message);
      });
  };

  return (
    <Fragment>
      <div className="social-login">
        <form onSubmit={submitHandler}>
          <div className="social-login-top">
            <div className="social-login-top__word">
              소셜 로그인 추가 입력 정보
            </div>
          </div>
          <div className="social-login-form">
            <div className="line">
              <input
                type="text"
                placeholder="이름을 입력하세요"
                required
                ref={nameInputRef}
              />
            </div>
            <div className="line">
              <input
                className="short"
                type="text"
                placeholder="닉네임을 입력하세요"
                required
                ref={nickNameInputRef}
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
        </form>
      </div>
    </Fragment>
  );
}

export default SocialLogin;
