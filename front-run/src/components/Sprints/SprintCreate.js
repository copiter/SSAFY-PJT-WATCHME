import React from "react";
import { useState, useContext } from "react";
import { Link, useNavigate } from "react-router-dom";
import { FetchUrl } from "../../store/communication";


function SprintCreate() {
  const FETCH_URL = useContext(FetchUrl);
  const url = `${FETCH_URL}/sprints`;
  const [inputs, setInputs] = useState({
    "name" : "",
    "description" : "",
    "routineStartAt" : "",
    "routineEndAt" :""
    });
  function getCookie(name) {
    const cookie = document.cookie
      .split(";")
      .map((cookie) => cookie.split("="))
      .filter((cookie) => cookie[0] === name);
    return cookie[0][1];
  }

  const handleSubmit = (event) => {
    event.preventDefault();
    fetch(url, {
        method: "POST",
        body:JSON.stringify(inputs),
        headers: {
        accessToken: getCookie("accessToken"),
        },
    })
    }   


  
  return(
    <>
    </>
  );
}

export default SprintCreate;




