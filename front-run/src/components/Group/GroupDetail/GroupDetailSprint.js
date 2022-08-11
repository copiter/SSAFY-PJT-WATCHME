import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import SprintItem from "./SprintItem";

import getCookie from "../../../Cookie";
import json from "../../json/groupdetailsprint.json";
import "./GroupDetailSprint.css";

function GroupDetailSprint(props) {
  const [sprints, setSprints] = useState(json.responseData.sprints);
  const navigate = useNavigate();

  const mode = ["규칙없음", "졸림 감지", "스마트폰 감시", "화면공유 필수"];

  //fetch
  const url = `${props.href}/sprints`;
  useEffect(() => {
    const config = {
      method: "GET",
      headers: {
        accessToken: getCookie("accessToken"),
      },
    };
    const getDatas = async () => {
      const response = await fetch(`${url}/${props.groupId}`, config);
      const data = await response.json();
      setSprints(data.responseData.sprints);
    };
    getDatas();
  }, []);

  //sprint 분류
  let sprintJoin,
    sprintOngoing,
    sprintDone = [];
  sprints.forEach((sprint) => {
    if (sprint.status === "YES") {
      sprintJoin = sprint;
    } else if (sprint.status === "ING") {
      sprintOngoing = sprint;
    } else if (sprint.status === "NO") {
      sprintDone.push(sprint);
    }
  });

  function sprintJoinHandler() {
    const ask = window.confirm("스프린트 참가신청 하시겠습니까?");
    if (!ask) {
      return;
    }
    fetch(`${url}/${sprintJoin.sprintId}/join`, {
      method: "POST",
      headers: {
        accessToken: getCookie("accessToken"),
      },
    })
      .then((response) => {
        return response.json();
      })
      .then((result) => {
        console.log(result);
        if (result.code === 200) {
          alert("스프린트에 정상적으로 참가신청 되었습니다");
        }
      })
      .catch((err) => {
        alert("참가실패 " + err);
      });
  }
  function sprintOnGoingHandler() {
    fetch(`${url}/${sprintOngoing.sprintId}/start`, {
      method: "POST",
      headers: {
        accessToken: getCookie("accessToken"),
      },
    })
      .then((response) => {
        return response.json();
      })
      .then((result) => {
        const roomId = result.reponseData.roomId;
        navigate(`/RoomDetail/${roomId}`);
      })
      .catch((err) => {
        alert("입장실패 " + err);
      });
  }

  return (
    <div id="group-detail__sprint">
      {/* sprintJoin */}
      <div id="sprint-join">
        <strong>
          모집중인 스프린트<i>(👇클릭)</i>
        </strong>
        <SprintItem sprint={sprintJoin} handler={sprintJoinHandler} />
      </div>

      {/* sprintOngoing */}
      <div id="sprint-ongoing">
        <strong>
          진행중인 스프린트<i>(👇클릭)</i>
        </strong>
        <SprintItem sprint={sprintOngoing} handler={sprintOnGoingHandler} />
      </div>
      {sprintDone.length > 0 && (
        <div id="sprint-done">
          <strong>지난 스프린트</strong>

          {sprintDone.map((sprint, index) => {
            return <SprintItem sprint={sprint} key={index} />;
          })}
        </div>
      )}
    </div>
  );
}

export default GroupDetailSprint;
