import React from "react";

import { useContext } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { FetchUrl } from "../../../store/communication";
import { getCookie } from "../../../Cookie";

import "./PointSuccess.css";

function PointSuccess() {
  // const pg_token = useParams().pg_token;
  const pg_token = window.location.search.split("=")[1];
  const tid = localStorage.getItem("tid");
  const FETCH_URL = useContext(FetchUrl);
  const url = `${FETCH_URL}/points/kakao/approval`;
  console.log(pg_token);

  const navigate = useNavigate();

  fetch(url, {
    method: "POST",
    body: JSON.stringify({
      pg_token: pg_token,
      tid: tid,
    }),
    headers: {
      accessToken: getCookie("accessToken"),
      "content-type": "application/json",
    },
  })
    .then((response) => response.json())
    .then((result) => {
      if (result.code === 200) {
        localStorage.removeItem("tid");
        navigate("/PointAdd");
      }
    });

  return (
    <div className="point-success">
      <div className="point-success-msg">결제 진행중입니다...</div>
      <div className="point-loading-msg">
        🚨 이 페이지가 지속되면 다시 시도해주세요. 🚨
      </div>
    </div>
  );
}
export default PointSuccess;
