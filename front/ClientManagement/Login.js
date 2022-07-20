import React from "react";

import "./Login.css";
import kakao from "../SignUpLogos/kakao.png";
import naver from "../SignUpLogos/naver.png";
import google from "../SignUpLogos/google.png";

const Login = () => {
  return (
    <div className="login">
      <div className="login-title">Login</div>
      <div className="login-inputs">
        <input className="input-box" placeholder="이메일을 입력하세요"></input>
        <input className="input-box" placeholder="비밀번호 입력하세요"></input>
        <a href="#none">아이디/비밀번호 찾기</a>
        <button className="submit-btn" type="submit">
          로그인
        </button>
      </div>

      <div className="insert-word">Or Continue With</div>

      <div className="signin-btns">
        <div className="email">
          <button type="button">이메일로 회원가입</button>
        </div>
        <div className="kakao">
          <img src={kakao} width="30" alt="카카오" />
          <button type="button">카카오로 회원가입</button>
        </div>
        <div className="naver">
          <img src={naver} width="40" alt="네이버" />
          <button type="button">네이버로 회원가입</button>
        </div>
        <div className="google">
          <img src={google} width="30" alt="구글" />
          <button type="button">Google로 회원가입</button>
        </div>
      </div>
    </div>
  );
};

export default Login;
