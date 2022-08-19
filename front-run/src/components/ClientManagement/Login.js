import { useRef, useContext } from "react";
import AuthContext from "../../store/auth-context";
import { FetchUrl } from "../../store/communication";
import { Link, useNavigate } from "react-router-dom";
import "./Login.css";
import kakao from "../../img/SignUpLogos/kakao.png";
import naver from "../../img/SignUpLogos/naver1.png";
import google from "../../img/SignUpLogos/google.png";
import { setCookie } from "../../Cookie";
import ErrorCode from "../../Error/ErrorCode";
import swal from "sweetalert";

const Login = () => {
  const FETCH_URL = useContext(FetchUrl);

  const navigate = useNavigate();

  const emailInputRef = useRef();
  const passwordInputRef = useRef();

  //using Context for token
  const authCtx = useContext(AuthContext);

  // Function handling submit button
  const submitHandler = (event) => {
    event.preventDefault();

    const enteredEmail = emailInputRef.current.value;
    const enteredPassword = passwordInputRef.current.value;

    const url = `${FETCH_URL}/members/auth/login`;

    // Interacting with server
    fetch(url, {
      method: "POST",
      body: JSON.stringify({
        email: enteredEmail,
        password: enteredPassword,
      }),
      headers: {
        "content-type": "application/json",
      },
    })
      .then((response) => response.json())
      .then((result) => {
        if (result.code === 200) {
          setCookie("accessToken", result.responseData.accessToken, {});
          swal("로그인 되었습니다", "", "success");
          navigate("/");
        } else {
          swal("아이디와 비밀번호를 확인하세요", "", "error");
        }
      })
      .catch((err) => {
        swal("통신실패", "", "error");
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
        <div className="link-to-find">
          <Link to="/findID">아이디</Link> /{" "}
          <Link to="/findPWD"> 비밀번호 찾기</Link>
        </div>
        <button className="submit-btn">로그인</button>
      </form>

      {/* <div className="insert-word">Or Continue With</div> */}

      <div className="signup-btns">
        <Link to="/signup">
          <div className="email">이메일로 회원가입</div>
        </Link>
        <a
          href={`${FETCH_URL}/oauth2/authorization/kakao?redirect_uri=https://watchme1.shop`}
        >
          <div className="kakao">
            <img src={kakao} alt="카카오" width={28} />
            <span>카카오로 로그인</span>
          </div>
        </a>
        <a
          href={`${FETCH_URL}/oauth2/authorization/naver?redirect_uri=https://watchme1.shop`}
        >
          <div className="naver">
            <img src={naver} alt="네이버" width={40} />
            <span>네이버로 로그인</span>
          </div>
        </a>
        <a
          href={`${FETCH_URL}/oauth2/authorization/google?redirect_uri=https://watchme1.shop`}
        >
          <div className="google">
            <img src={google} alt="구글" width={40} />
            <span>Google로 로그인</span>
          </div>
        </a>
      </div>
    </div>
  );
};

export default Login;
