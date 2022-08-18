import { now } from "jquery";
import React from "react";
import { useContext, useState, useEffect } from "react";
import { getCookie } from "../../../../Cookie";
import { FetchUrl } from "../../../../store/communication";
import "./MyStudy.css";

function MyStudy(props) {
  const FETCH_URL = useContext(FetchUrl);
  const url = `${FETCH_URL}/rooms`;

  const [study, setStudy] = useState({
    // name: "123",
    startTime: new Date(),
    // mode: "MODE2",
    // penalty: 13,
  });

  const id = window.location.pathname.split("/")[2].substring(0);
  console.log("URL:" + url + "/" + id);

  useEffect(() => {
    setInterval(() => {
      console.log("TESTHERE");
      fetch(url + "/" + id, {
        headers: {
          accessToken: getCookie("accessToken"),
        },
      })
        .then((response) => {
          console.log(response);
          if (response.ok) {
            return response.json(); //ok떨어지면 바로 종료.
          } else {
            response.json().then((data) => {
              let errorMessage = "";
              throw new Error(errorMessage);
            });
          }
        })
        .then((result) => {
          if (result != null) {
            setStudy(result.responseData.room);
            setStudyTimes(
              new Date().getTime() -
                new Date(result.responseData.room.startTime).getTime()
            );
          }
        })
        .catch((err) => {
          console.log("ERR");
        });
    }, 1000);
  }, []);

  const [studyTimes, setStudyTimes] = useState(0);
  const [errorLogs, setErrorLogs] = useState([{}]);

  useEffect(() => {
    const time = new Date().toTimeString().split(" ")[0];
    let modeArray = [{ mode: props.mode, time: time }, ...errorLogs];
    if (modeArray.length > 5) {
      modeArray.pop();
    }
    setErrorLogs(modeArray);
    console.log("바뀜");
    console.log("errorLogs", errorLogs);
  }, [props.newError]);

  console.log(studyTimes);
  let hours = studyTimes / 1000 / 60 / 60,
    minutes = (studyTimes / 1000 / 60) % 60,
    seconds = (studyTimes / 1000) % 60;
  return (
    <div id="borders">
      <div id="clock">
        <strong id="study-time__title">공부시간</strong>
        <div id="study-time__time">
          {hours - 9 >= 10
            ? parseInt(hours - 9)
            : hours - 9 >= 1
            ? "0" + parseInt(hours - 9)
            : "00"}
          :
          {minutes >= 10
            ? parseInt(minutes)
            : minutes >= 1
            ? "0" + parseInt(minutes)
            : "00"}
          :
          {seconds >= 10
            ? parseInt(seconds)
            : seconds >= 1
            ? "0" + parseInt(seconds)
            : "00"}
        </div>
      </div>
      <div id="study-rule">
        <div>
          <p id="study-rule__title">
            <span>📝 규칙 - </span>
            <span>
              {study.mode === "MODE1"
                ? " 자율"
                : study.mode === "MODE2"
                ? " 졸림 감지"
                : study.mode === "MODE3"
                ? " 스마트폰"
                : " 자리 이탈"}
            </span>
            <span id="study-rule__penalty">
              {study.mode !== "MODE1" && ` ${study.penalty}회`}
            </span>
          </p>
        </div>
        <div id="study-rule__error">
          {errorLogs.length > 2 &&
            errorLogs.map((log, index) => {
              return (
                <div className="study-rule__error-item" key={index}>
                  <span>{log.time}</span>
                  <span>
                    {log.mode === "MODE1"
                      ? " 자율"
                      : log.mode === "MODE2"
                      ? " 졸림 감지"
                      : log.mode === "MODE3"
                      ? " 스마트폰"
                      : log.mode === "MODE4"
                      ? " 자리 이탈"
                      : ""}
                  </span>
                </div>
              );
            })}
        </div>
      </div>
    </div>
  );
}

export default MyStudy;
