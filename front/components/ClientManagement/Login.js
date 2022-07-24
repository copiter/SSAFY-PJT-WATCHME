import { useState, useRef, useContext } from "react";
import AuthContext from "../../store/auth-context";
import { Link, useNavigate } from "react-router-dom";

import "./Login.css";
import kakao from "../../img/SignUpLogos/kakao.png";
import naver from "../../img/SignUpLogos/naver1.png";
import google from "../../img/SignUpLogos/google.png";

const Login = () => {
  const navigate = useNavigate();

  const emailInputRef = useRef();
  const passwordInputRef = useRef();

  const [isLogin, setIsLogin] = useState(false);

  //using Context for idToken
  const authCtx = useContext(AuthContext);

  // Function handling submit button
  const submitHandler = (event) => {
    event.preventDefault();

    const enteredEmail = emailInputRef.current.value;
    const enteredPassword = passwordInputRef.current.value;

    const url =
      "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=AIzaSyA7OgmwziXipTd23RnFtyt6SZ17gqW_V48";

    // Interacting with server
    fetch(url, {
      method: "POST",
      withCredentials: true,
      credentials: "include",
      body: JSON.stringify({
        email: enteredEmail,
        password: enteredPassword,
      }),
      headers: {
        "content-type": "application/json",
        authorization: `Bearer ${authCtx.accessToken}`,
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
        navigate("/");
      })
      .catch((err) => {
        alert(err.message);
      });
  };

  // ! btn not yet finished
  return (
    <div className="login">
      <div className="login-title">Login</div>
      <form className="login-inputs" onSubmit={submitHandler}>
        <input
          className="input-box"
          type="email"
          placeholder="이메일을 입력하세요"
          required
          ref={emailInputRef}
        />
        <input
          className="input-box"
          type="password"
          placeholder="비밀번호 입력하세요"
          required
          ref={passwordInputRef}
        />
        <a href="#none">아이디/비밀번호 찾기</a>
        <button className="submit-btn">로그인</button>
      </form>

      {/* <div className="insert-word">Or Continue With</div> */}

      <div className="signup-btns">
        <Link to="/signup">
          <div className="email">이메일로 회원가입</div>
        </Link>
        <Link to="#none">
          <div className="kakao">
            <img src={kakao} alt="카카오" width={28} />
            <span>카카오로 로그인</span>
          </div>
        </Link>
        <Link to="#none">
          <div className="naver">
            <img src={naver} alt="네이버" width={40} />
            <span>네이버로 로그인</span>
          </div>
        </Link>
        <Link to="/slogin">
          <div className="google">
            <img src={google} alt="구글" width={40} />
            <span>Google로 로그인</span>
          </div>
        </Link>
      </div>
    </div>
  );
};

export default Login;
