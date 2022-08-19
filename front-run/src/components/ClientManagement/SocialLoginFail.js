import React from "react";
import { useNavigate } from "react-router-dom";

import swal from "sweetalert";

function SocialLoginFail() {
  const navigate = useNavigate();
  swal(
    "소셜로 가입된 계정이 있습니다",
    "다른 계정으로 로그인 해주세요",
    "warning"
  );
  setInterval(() => {
    navigate("/");
  }, [2000]);

  return <div></div>;
}

export default SocialLoginFail;
