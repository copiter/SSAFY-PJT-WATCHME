import React, { useState, Fragment, useRef, useContext } from "react";
import { FetchUrl } from "../../store/communication";
import AuthContext from "../../store/auth-context";
import { useNavigate } from "react-router-dom";
import { getCookie } from "../../Cookie";

import accept from "../../img/Icons/accept.png";
import cancel from "../../img/Icons/cancel.png";

import "./SocialLogin.css"; // 동일한 CSS 파일 사용
import ErrorCode from "../../Error/ErrorCode";

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

  // 닉네임 중복확인
  const nickNameDupUrl = `${FETCH_URL}/members/nickName-check`;
  const [isNickNameDup, setIsNickNameDup] = useState(null);
  function checkDuplicateNickName() {
    const nickName = nickNameInputRef.current.value;
    //API
    const config = {
      method: "POST",
      body: JSON.stringify({ nickName: nickName }),
      headers: {
        "Content-Type": "application/json",
      },
    };
    const getDatas = async () => {
      try {
        const response = await fetch(nickNameDupUrl, config);
        const result = await response.json();
        if (result.code === 200) {
          setIsNickNameDup(false);
        } else {
          ErrorCode(result);
          setIsNickNameDup(true);
        }
      } catch (e) {
        alert("통신 실패 " + e);
      }
    };
    getDatas();
  }

  const submitHandler = (event) => {
    event.preventDefault();

    if (isNickNameDup) {
      alert("닉네임 중복확인이 필요합니다");
      return;
    } else if (isNickNameDup === null) {
      alert("닉네임 중복확인이 필요합니다");
      return;
    }

    const enteredName = nameInputRef.current.value;
    const enteredNickname = nickNameInputRef.current.value;
    const enteredSex = sexInputRef.current.value;
    const enteredBirthday = birthdayInputRef.current.value;

    const url = `${FETCH_URL}/members/auth/social-signup`;
    // Interacting with server
    fetch(url, {
      method: "POST",
      body: JSON.stringify({
        name: enteredName,
        nickName: enteredNickname,
        gender: enteredSex,
        birth: enteredBirthday,
      }),
      headers: {
        accessToken: getCookie("accessToken"),
        "content-type": "application/json",
      },
    })
      .then((response) => response.json())
      .then((result) => {
        if (result.code === 200) {
          alert("회원가입 되었습니다");
          navigate("/");
        } else {
          ErrorCode(result);
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
            <div className="line dup-check">
              <input
                className="short"
                type="text"
                placeholder="닉네임을 입력하세요"
                required
                ref={nickNameInputRef}
              />
              {isNickNameDup === false && (
                <img className="dup-icon" src={accept} alt="중복아님" />
              )}
              {isNickNameDup === true && (
                <img className="dup-icon" src={cancel} alt="중복" />
              )}
              <button className="dup" onClick={checkDuplicateNickName}>
                중복확인
              </button>
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
                max="9999-12-31"
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
