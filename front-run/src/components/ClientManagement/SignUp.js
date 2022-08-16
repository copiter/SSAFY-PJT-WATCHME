import React, { useState, Fragment, useRef, useContext } from "react";
import { useNavigate } from "react-router-dom";
import { FetchUrl } from "../../store/communication";
import GetToday from "../ETC/GetToday";
import ErrorCode from "../../Error/ErrorCode";

import accept from "../../img/Icons/accept.png";
import cancel from "../../img/Icons/cancel.png";

import "./SignUp.css";

function SignUp() {
  const FETCH_URL = useContext(FetchUrl);

  const url = `${FETCH_URL}/members/auth/signup`;
  const emailDupUrl = `${FETCH_URL}/members/emails-check`;
  const nickNameDupUrl = `${FETCH_URL}/members/nickName-check`;

  const [selectSex, setSelectSex] = useState("ND");
  const navigate = useNavigate();

  // 오늘 고르기
  const today = GetToday(0);

  const handleSelectSex = (e) => {
    setSelectSex(e.target.value);
  };

  const emailInputRef = useRef();
  const passwordInputRef = useRef();
  const pwdSameRef = useRef();
  const nameInputRef = useRef();
  const nickNameInputRef = useRef();
  const sexInputRef = useRef();
  const birthdayInputRef = useRef();
  const imageInputRef = useRef();

  const submitHandler = (event) => {
    event.preventDefault();

    //중복확인 여부
    if (isEmailDup || isNickNameDup) {
      alert("이메일 혹은 닉네임 중복확인이 필요합니다");
      return;
    } else if (isEmailDup === null || isNickNameDup === null) {
      alert("이메일 혹은 닉네임 중복확인이 필요합니다");
      return;
    }

    if (!isPwdSame) {
      alert("비밀번호를 확인해주세요");
      return;
    }

    const data = {
      email: emailInputRef.current.value,
      password: passwordInputRef.current.value,
      name: nameInputRef.current.value,
      nickName: nickNameInputRef.current.value,
      gender: sexInputRef.current.value,
      birth: birthdayInputRef.current.value,
    };

    const images = imageInputRef.current.files[0];
    const formData = new FormData();
    formData.append("images", images);
    formData.append(
      "data",
      new Blob([JSON.stringify(data)], { type: "application/json" })
    );

    console.log(data);

    //myjsons->application.josn

    // Interacting with server
    fetch(url, {
      method: "POST",
      body: formData,
    })
      .then((response) => response.json())
      .then((result) => {
        if (result.code === 200) {
          alert("회원가입 되었습니다");
          navigate("/login"); //로그인 페이지로
        } else {
          ErrorCode(result);
        }
      })
      .catch((err) => {
        console.log(err);
        alert("오류가 발생하였습니다");
      });
  };

  const [isPwdSame, setIsPwdSame] = useState(null);
  function checkPwdSame() {
    if (passwordInputRef.current.value === pwdSameRef.current.value) {
      setIsPwdSame(true);
    } else {
      setIsPwdSame(false);
    }
  }

  const [isEmailDup, setIsEmailDup] = useState(null);
  function checkDuplicateEmail() {
    const email = emailInputRef.current.value;
    //API
    const config = {
      method: "POST",
      body: JSON.stringify({ email: email }),
      headers: {
        "Content-Type": "application/json",
      },
    };
    const getDatas = async () => {
      try {
        const response = await fetch(emailDupUrl, config);
        const result = await response.json();
        if (result.code === 200) {
          setIsEmailDup(false);
        } else {
          ErrorCode(result);
          setIsEmailDup(true);
        }
      } catch (e) {
        alert("통신 실패 " + e);
      }
    };
    getDatas();
  }

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

  const [fileImage, setFileImage] = useState("");
  const saveFileImage = (event) => {
    setFileImage(URL.createObjectURL(event.target.files[0]));
  };
  return (
    <Fragment>
      <div className="signup">
        <form>
          <div className="signup-top">
            <div className="signup-top__word">SIGN UP</div>
          </div>
          <div className="signup-form">
            <div className="signup-left">
              <input
                className="signup-left-image"
                type="file"
                accept="image/*"
                onChange={saveFileImage}
                ref={imageInputRef}
              />
              {fileImage && (
                <img
                  className="signup-uploaded-image"
                  alt="sample"
                  src={fileImage}
                />
              )}
              <div id="blank-layer-signup"></div>
              <span className="signup-left-addimage">
                프로필 사진을 올려주세요
              </span>
            </div>
            <div className="signup-right">
              <div className="line dup-check">
                <input
                  className="short"
                  type="email"
                  placeholder="이메일을 입력하세요"
                  required
                  ref={emailInputRef}
                />
                {isEmailDup === false && (
                  <img className="dup-icon" src={accept} alt="중복아님" />
                )}
                {isEmailDup === true && (
                  <img className="dup-icon" src={cancel} alt="중복" />
                )}

                <button className="dup" onClick={checkDuplicateEmail}>
                  중복확인
                </button>
              </div>
              <div className="line password-check">
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
                  ref={pwdSameRef}
                  onKeyUp={checkPwdSame}
                />
                {isPwdSame === false && (
                  <img className="dup-icon" src={cancel} alt="다름" />
                )}
                {isPwdSame === true && (
                  <img className="dup-icon" src={accept} alt="같음" />
                )}
              </div>
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
                  max={today}
                  ref={birthdayInputRef}
                />
              </div>
              <button
                className="submitting"
                type="submit"
                onClick={submitHandler}
              >
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
