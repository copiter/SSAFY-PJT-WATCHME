import React, { useState, useEffect } from "react";
import { getCookie } from "../../../Cookie";
import { useNavigate } from "react-router-dom";

import "./GroupDetailMembers.css";
import json from "../../json/groupdetailmembers.json";
import ErrorCode from "../../../Error/ErrorCode";

function GroupDetailMembers(props) {
  const [memData, setMemData] = useState({ appliers: [], members: [] });
  const [reload, setReload] = useState(false);
  const role = props.myData.role;
  const url = props.url; //${FETCH_URL}/groups/${groupId}
  const navigate = useNavigate();

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
          console.log(data);
          ErrorCode(data);
        }
      } catch (e) {
        alert(`통신 실패 ` + e);
      }
    };
    getDatas();
  }, [reload]);

  console.log(memData);

  function reformGroupHandler() {
    navigate(`/GroupReform/${props.groupId}`);
  }

  function deleteGroupHandler() {
    const ask = window.confirm("그룹을 정말 삭제하시겠습니까?");
    if (!ask) {
      return;
    }

    const config = {
      method: "POST",
      headers: {
        accessToken: getCookie("accessToken"),
      },
    };
    const getDatas = async () => {
      const response = await fetch(url + "/delete", config);
      const data = await response.json();

      if (data.code === 200) {
        alert("그룹 삭제 되었습니다");
        navigate("/");
      } else {
        ErrorCode(data);
      }
    };
    getDatas();
  }

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
      const response = await fetch(url + "/leave", config);
      const data = await response.json();

      if (data.code === 200) {
        alert("그룹 탈퇴 되었습니다");
        window.location.reload();
      } else {
        ErrorCode(data);
      }
    };
    getDatas();
  }

  function confirmJoinHandler(nickName) {
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
      const response = await fetch(url + "/applies/accept", config);
      const data = await response.json();

      if (data.code === 200) {
        alert("가입 승인되었습니다");
        setReload(!reload);
      } else {
        ErrorCode(data);
      }
    };
    getDatas();
  }

  function refuseJoinHandler(nickName) {
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
      const response = await fetch(url + "/applies/decline", config);
      const data = await response.json();

      if (data.code === 200) {
        alert("반려되었습니다");
        setReload(!reload);
      } else {
        ErrorCode(data);
      }
    };
    getDatas();
  }

  function expulsionHandler(nickName) {
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
      const response = await fetch(url + "/kick", config);
      const data = await response.json();

      if (data.code === 200) {
        alert(`[${nickName}]님이 성공적으로 탈퇴되었습니다`);
        setReload(!reload);
      } else {
        ErrorCode(data);
      }
    };
    getDatas();
  }

  function transferHandler(nickName) {
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
      const response = await fetch(url + "/leader-toss", config);
      const data = await response.json();
      if (data.code === 200) {
        alert(`[${nickName}]님으로 리더 권한이 이전되었습니다`);
        setReload(!reload);
      } else {
        ErrorCode(data);
      }
    };
    getDatas();
  }

  return (
    <div id="group-detail__members">
      <div id="group-detail__members-title">
        <strong>그룹멤버</strong>
        {role === 1 && (
          <button
            className="group-detail__members-btn"
            onClick={leaveGroupHandler}
          >
            그룹 탈퇴
          </button>
        )}
        {role === 0 && (
          <div>
            <button
              className="group-detail__leader-btn"
              onClick={reformGroupHandler}
            >
              그룹 수정
            </button>
            <button
              className="group-detail__members-btn"
              onClick={deleteGroupHandler}
            >
              그룹 삭제
            </button>
          </div>
        )}
      </div>
      <div id="group-detail__members-content">
        <ul>
          {memData.appliers !== null &&
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
                    <span className="medium-text">
                      {applier.hasOwnProperty("studyTime")
                        ? `${parseInt(applier.studyTime / 60)}시간 ${
                            applier.studyTime % 60
                          }분`
                        : "0분"}
                    </span>
                  </div>
                  <div>
                    <span>페널티 횟수</span>
                    <span className="medium-text">
                      {applier.hasOwnProperty("penalty")
                        ? `😴${applier.penalty[1]} / 📱${applier.penalty[2]}`
                        : null}{" "}
                    </span>
                  </div>
                </div>
                {role === 0 && (
                  <div className="dropdown">
                    {/* <div className="members-btn"></div> */}
                    <button className="dropbtn">가입</button>
                    <div className="dropdown-content">
                      <button
                        className="group-detail__members-btn appliers"
                        onClick={() => confirmJoinHandler(applier.nickName)}
                      >
                        승인
                      </button>
                      <button
                        className="group-detail__members-btn"
                        onClick={() => refuseJoinHandler(applier.nickName)}
                      >
                        반려
                      </button>
                    </div>
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
                    <span className="medium-text">
                      {member.hasOwnProperty("studyTime")
                        ? `${parseInt(member.studyTime / 60)}시간 ${
                            member.studyTime % 60
                          }분`
                        : "0분"}
                    </span>
                  </div>
                  <div>
                    <span>페널티 횟수</span>
                    <span className="medium-text">
                      {member.hasOwnProperty("penalty")
                        ? `😴${member.penalty[1]} / 📱${member.penalty[2]}`
                        : null}
                    </span>
                  </div>
                </div>
                {role === 0 && (
                  <div className="dropdown">
                    {/* <div className="members-btn"></div> */}
                    <button className="dropbtn">관리</button>
                    <div className="dropdown-content">
                      <button
                        className="group-detail__members-btn"
                        onClick={() => expulsionHandler(member.nickName)}
                      >
                        강퇴
                      </button>
                      <button
                        className="group-detail__members-btn handover"
                        onClick={() => transferHandler(member.nickName)}
                      >
                        권한이전
                      </button>
                    </div>
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
