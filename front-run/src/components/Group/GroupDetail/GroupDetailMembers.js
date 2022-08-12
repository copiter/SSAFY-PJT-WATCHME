import React, { useState, useEffect } from "react";
import { getCookie } from "../../../Cookie";

import "./GroupDetailMembers.css";
import json from "../../json/groupdetailmembers.json";

function GroupDetailMembers(props) {
  const [memData, setMemData] = useState(json.responseData);
  const role = props.myData.role;
  const url = props.url;

  useEffect(() => {
    const config = {
      method: "GET",
      headers: {
        accessToken: getCookie("accessToken"),
      },
    };
    const getDatas = async () => {
      try {
        const response = await fetch(url + "/members", config);
        const data = await response.json();
        if (data.code === 200) {
          setMemData(data.responseData);
        } else {
          alert(data.message);
        }
      } catch (e) {
        alert(`통신 실패 ` + e);
      }
    };
    getDatas();
  }, []);

  function leaveGroupHandler() {
    const ask = window.confirm("탈퇴하시겠습니까?");
    if (!ask) {
      return;
    }

    const config = {
      method: "POST",
      body: {
        nickName: props.myData.nickName,
      },
      headers: {
        accessToken: getCookie("accessToken"),
      },
    };
    const getDatas = async () => {
      try {
        const response = await fetch(url + "/leave", config);
        const data = await response.json();

        if (data.code === 200) {
          alert("그룹 탈퇴 되었습니다");
        } else {
          alert(data.message);
        }
      } catch (e) {
        alert(e);
      }
    };
    getDatas();
  }

  function confirmJoinHandler(e) {
    const nickName = e.target.parentNode.parentNode.outerText.split("\n")[0];
    const ask = window.confirm(`[${nickName}]님을 가입 승인하시겠습니까?`);
    if (!ask) {
      return;
    }
    //가입 승인
    const config = {
      method: "POST",
      body: JSON.stringify({ nickName: nickName }),
      headers: {
        accessToken: getCookie("accessToken"),
        "Content-Type": "application/json",
      },
    };
    const getDatas = async () => {
      try {
        const response = await fetch(url + "/applies/accept", config);
        const data = await response.json();

        if (data.code === 200) {
          alert("가입 승인되었습니다");
        } else {
          alert(data.message);
        }
      } catch (e) {
        alert("통신 " + e);
      }
    };
    getDatas();
  }

  function refuseJoinHandler(e) {
    const nickName = e.target.parentNode.parentNode.outerText.split("\n")[0];
    const ask = window.confirm(`[${nickName}]님을 반려하시겠습니까?`);
    if (!ask) {
      return;
    }
    //가입 승인
    const config = {
      method: "POST",
      body: JSON.stringify({ nickName: nickName }),
      headers: {
        accessToken: getCookie("accessToken"),
        "Content-Type": "application/json",
      },
    };
    const getDatas = async () => {
      try {
        const response = await fetch(url + "/applies/decline", config);
        const data = await response.json();

        if (data.code === 200) {
          alert("반려되었습니다");
        } else {
          alert(data.message);
        }
        window.location.reload();
        props.setNavBar(2);
      } catch (e) {
        alert("반려 실패 " + e);
      }
    };
    getDatas();
  }

  function expulsionHandler(e) {
    const nickName = e.target.parentNode.parentNode.outerText.split("\n")[0];
    const ask = window.confirm(`[${nickName}]님을 강퇴 하시겠습니까?`);
    if (!ask) {
      return;
    }

    //멤버 강퇴
    const config = {
      method: "POST",
      body: JSON.stringify({ nickName: nickName }),
      headers: {
        accessToken: getCookie("accessToken"),
        "Content-Type": "application/json",
      },
    };
    const getDatas = async () => {
      try {
        const response = await fetch(url + "/kick", config);
        const data = await response.json();

        if (data.code === 200) {
          alert(`[${nickName}]님이 성공적으로 탈퇴되었습니다`);
        } else {
          alert(data.message);
        }
        window.location.reload();
        props.setNavBar(2);
      } catch (e) {
        alert(`통신 ` + e);
      }
    };
    getDatas();
  }

  function transferHandler(e) {
    const nickName = e.target.parentNode.parentNode.outerText.split("\n")[0];
    const ask = window.confirm(
      `[${nickName}]님으로 리더 권한을 이전 하시겠습니까?`
    );
    if (!ask) {
      return;
    }

    //권한 이양
    const config = {
      method: "POST",
      body: JSON.stringify({ nickName: nickName }),
      headers: {
        accessToken: getCookie("accessToken"),
        "Content-Type": "application/json",
      },
    };
    const getDatas = async () => {
      try {
        const response = await fetch(url + "/leader-toss", config);
        const data = await response.json();
        if (data.code === 200) {
          alert(`[${nickName}]님으로 리더 권한이 이전되었습니다`);
        } else {
          alert(data.message);
        }
        window.location.reload();
        props.setNavBar(2);
      } catch (e) {
        alert(`통신 ` + e);
      }
    };
    getDatas();
  }

  return (
    <div id="group-detail__members">
      <div id="group-detail__members-title">
        <strong>그룹멤버</strong>
        <button
          className="group-detail__members-btn"
          onClick={leaveGroupHandler}
        >
          그룹 탈퇴
        </button>
      </div>
      <div id="group-detail__members-content">
        <ul>
          {memData.appliers &&
            memData.appliers.map((applier, index) => (
              <li key={index} className="group-detail__appliers-item">
                <div
                  className="group-detail__members-item-img"
                  style={{
                    backgroundImage: `url(${applier.imgLink})`,
                    backgroundSize: "cover",
                  }}
                ></div>
                <div className="group-detail__members-item-name">
                  <span>{applier.nickName}</span>
                  <small>{applier.email}</small>
                </div>
                <div className="group-detail__members-item-achieve">
                  <div>
                    <span>공부시간</span>
                    <span className="medium-text">{`${parseInt(
                      applier.studyTime / 60
                    )}시간 ${applier.studyTime % 60}분`}</span>
                  </div>
                  <div>
                    <span>페널티 횟수</span>
                    <span className="medium-text">
                      {`📱${applier.penalty[0]} / 😴${applier.penalty[1]}`}
                    </span>
                  </div>
                </div>
                {role === 0 && (
                  <div className="members-btn">
                    <button
                      className="group-detail__members-btn appliers"
                      onClick={confirmJoinHandler}
                    >
                      승인
                    </button>
                    <button
                      className="group-detail__members-btn"
                      onClick={refuseJoinHandler}
                    >
                      반려
                    </button>
                  </div>
                )}
              </li>
            ))}

          {/* members */}
          {memData.members &&
            memData.members.map((member, index) => (
              <li key={index} className="group-detail__members-item">
                <div
                  className="group-detail__members-item-img"
                  style={{
                    backgroundImage: `url(${member.imgLink})`,
                    backgroundSize: "cover",
                  }}
                ></div>
                <div className="group-detail__members-item-name">
                  <span>{member.nickName}</span>
                  <small>{member.email}</small>
                </div>
                <div className="group-detail__members-item-achieve">
                  <div>
                    <span>공부시간</span>
                    <span className="medium-text">{`${parseInt(
                      member.studyTime / 60
                    )}시간 ${member.studyTime % 60}분`}</span>
                  </div>
                  <div>
                    <span>페널티 횟수</span>
                    <span className="medium-text">
                      {`📱${member.penalty[0]} / 😴${member.penalty[1]}`}
                    </span>
                  </div>
                </div>
                {role === 0 && (
                  <div className="members-btn">
                    <button
                      className="group-detail__members-btn"
                      onClick={expulsionHandler}
                    >
                      강퇴
                    </button>
                    <button
                      className="group-detail__members-btn handover"
                      onClick={transferHandler}
                    >
                      이전
                    </button>
                  </div>
                )}
              </li>
            ))}
        </ul>
      </div>
    </div>
  );
}

export default GroupDetailMembers;
