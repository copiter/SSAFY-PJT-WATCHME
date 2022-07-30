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
  const nameInputRef = useRef();
  const nickNameInputRef = useRef();
  const sexInputRef = useRef();
  const birthdayInputRef = useRef();
  const imageInputRef=useRef();

  const submitHandler = (event) => {
    event.preventDefault();


    const data={
      email: emailInputRef.current.value,
      password: passwordInputRef.current.value,
      name: nameInputRef.current.value,
      nickName: nickNameInputRef.current.value,
      gender: sexInputRef.current.value,
      birth: birthdayInputRef.current.value,
    };

    const files=imageInputRef.current.files[0];


    const formData = new FormData();
    formData.append('files', files);
    formData.append("data", new Blob([JSON.stringify(data)], {type: "application/json"}))
    console.log(formData);
    //myjsons->application.josn
    

    const url = `${FETCH_URL}/signup`;
    // Interacting with server
    fetch(url, {
      method: "POST",
      body: formData
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


  const [fileImage, setFileImage] = useState("");
  const saveFileImage = (event) =>{
    setFileImage(URL.createObjectURL(event.target.files[0]));

  };
  return (
    <Fragment>
      <div className="signup">
        <form onSubmit={submitHandler} method="post" >
          <div className="signup-top">
            <div className="signup-top__word">SIGN UP</div>
          </div>
          <div className="signup-form">
            <div className="signup-left">
              <div className="signup-left-image"> 
                {fileImage && ( <img alt="sample" src={fileImage}style={{ margin: "auto" ,width:"100%",height:"100%",borderRadius:"50%"
              }} /> )}
              </div>
              <input  name="imggeUpload" type="file" accept="image/*" onChange={saveFileImage}  ref={imageInputRef}/>
              chceck
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
          </div>
        </form>
      </div>
    </Fragment>
  );
}

export default SignUp;
