import React, { useState, Fragment, useRef, useContext } from "react";
import { useNavigate } from "react-router-dom";
import { FetchUrl } from "../../store/communication";

import accept from "../../img/Icons/accept.png";
import cancel from "../../img/Icons/cancel.png";

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

    const data = {
      email: emailInputRef.current.value,
      password: passwordInputRef.current.value,
      name: nameInputRef.current.value,
      nickName: nickNameInputRef.current.value,
      gender: sexInputRef.current.value,
      birth: birthdayInputRef.current.value,
    };

    const files = imageInputRef.current.files[0];
    const formData = new FormData();
    formData.append("files", files);
    formData.append(
      "data",
      new Blob([JSON.stringify(data)], { type: "application/json" })
    );

    //myjsons->application.josn

    const url = `${FETCH_URL}/signup`;
    // Interacting with server
    fetch(url, {
      method: "POST",
      body: formData,
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
        alert("오류가 발생하였습니다");
      });
  };

  const [isEmailDup, setIsEmailDup] = useState(null);
  function checkDuplicateEmail() {
    const email = emailInputRef.current.value;
    //API
    const config = {
      method: "GET",
      body: JSON.stringify({ email: email }),
      headers: {
        "Content-Type": "application/json",
      },
    };
    const getDatas = async () => {
      try {
        const response = await fetch(`${FETCH_URL}/emails-check`, config);
        const result = await response.json();
        if (result.code === "200") {
          setIsEmailDup(false);
        } else {
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
      method: "GET",
      body: JSON.stringify({ nickName: nickName }),
      headers: {
        "Content-Type": "application/json",
      },
    };
    const getDatas = async () => {
      try {
        const response = await fetch(`${FETCH_URL}/nickName-check`, config);
        const result = await response.json();
        if (result.code === "200") {
          setIsNickNameDup(false);
        } else {
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
                  max="2022-12-31"
                  ref={birthdayInputRef}
                />
              </div>
              <button
                className="submitting"
                type="submit"
                onSubmit={submitHandler}
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
