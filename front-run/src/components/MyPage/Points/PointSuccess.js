import React from "react";

import {useContext } from "react";
import {  useParams } from "react-router-dom";
import { FetchUrl } from "../../../store/communication";

function PointSuccess() {

    const pg_token=useParams().pg_token;
    const tid="";
    const FETCH_URL = useContext(FetchUrl);
    const url=`${FETCH_URL}/points/kakao/approval`;

        
    function getCookie(name) {
        const cookie = document.cookie
        .split(";")
        .map((cookie) => cookie.split("="))
        .filter((cookie) => cookie[0] === name);
        return cookie[0][1];
    }
  

    fetch(url , {
      body:{
        "pg_toeken":pg_token,
        "tid":tid
      },
      headers: {
        accessToken: getCookie("accessToken"),
      },
    })

    return (
        <div>
        <h2>Result page</h2>
        </div>
    );
}
export default PointSuccess;