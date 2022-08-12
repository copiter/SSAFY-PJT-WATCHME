import React, { useContext } from "react";
import { useNavigate } from "react-router-dom";
import { FetchUrl } from "../../store/communication";
import { getCookie } from "../../Cookie";

import "./RoomItem.css";

function RoomItem(props) {
  const room = props.room;
  const mode = ["", "규칙없음", "졸림 감지", "스마트폰 감시", "화면공유 필수"];

  const navigate = useNavigate();
  const FETCH_URL = useContext(FetchUrl);

  async function enteringRoom(id) {
    //방들어가기 문제없이 작동
    const url = `${FETCH_URL}/rooms/${id}/join`;

    function configWithPwd() {
      const pwd = window.prompt("비공개 방입니다. 비밀번호를 입력해주세요:");
      if (!pwd) {
        return;
      }
      return {
        method: "POST",
        body: JSON.stringify({ pwd: pwd }),
        headers: {
          accessToken: getCookie("accessToken"),
          "Content-Type": "application/json",
        },
      };
    }
    function configNoPwd() {
      return {
        method: "POST",
        headers: {
          accessToken: getCookie("accessToken"),
        },
      };
    }

    let config;
    if (room.secret) {
      config = await configWithPwd();
    } else {
      config = configNoPwd();
    }

    try {
      fetch(url, config)
        .then((response) => {
          console.log("TEST");
          console.log(response);
          console.log("TEST");
          if (response.bodyUsed) {
            console.log("재사용됨");
          } else if (response.ok) {
            console.log(response);
            return response.json();
          }
        })
        .then((result) => {
          if (result.code == 200) {
            navigate(`/RoomDetail/${id}`);
          } else {
            console.log(result);
          }
        })
        .catch((err) => {
          alert("로그인후 이용부탁드립니다.");
        });
    } catch {
      alert("로그인후 이용부탁드립니다.");
    }
  }

  return (
    <article onClick={() => enteringRoom(room.id)}>
      <div
        className="room-specs"
        style={{
          backgroundImage: `url(${room.roomImage})`,
          backgroundSize: "cover",
        }}
      >
        <div className="room-specs__rules">
          <span>📝 규칙</span>
          <p>✔ {mode[+room.roomStatus.slice(-1)]}</p>
          <span>📆 기간</span>
          <p>{room.endTime}</p>
        </div>
      </div>

      <dl className="room-info">
        <div className="category member-no">
          <dt className="sr-only">카테고리</dt>
          <dl>{room.ctgName}</dl>
          <dt className="sr-only">인원수</dt>
          <dl>
            &#128509;
            {room.nowNum}/{room.maxNum}
          </dl>
        </div>
        <div>
          <dt className="sr-only">이름</dt>
          <dl>
            {room.secret ? "🔒" : null}
            {room.roomName}
          </dl>
        </div>
        <div className="info-content">
          <dt className="sr-only">세부설명</dt>
          <dl>{room.description}</dl>
        </div>
      </dl>
    </article>
  );
}

export default RoomItem;
