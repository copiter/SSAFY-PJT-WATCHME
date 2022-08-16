import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import SprintItem from "./SprintItem";

import { getCookie } from "../../../Cookie";
import json from "../../json/groupdetailsprint.json";
import "./GroupDetailSprint.css";
import ErrorCode from "../../../Error/ErrorCode";

function GroupDetailSprint(props) {
  const [sprints, setSprints] = useState([]);
  // const [sprints, setSprints] = useState(json.responseData.sprints);
  const [reload, setReload] = useState(false);

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

      if (data.code === 200) {
        setSprints(data.responseData.sprints);
      }
    };
    getDatas();
  }, [reload]);

  //sprint 분류
  let sprintJoin = {},
    sprintOngoing = {},
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

  function sprintCal() {
    const ask = window.confirm("정산하시겠습니까?");
    if (!ask) {
      return;
    }
    fetch(`${props.href}/sprints/${sprintJoin.sprintId}/`, {
      // url!!!!!!!!!!!!
      method: "POST",
      headers: {
        accessToken: getCookie("accessToken"),
      },
    })
      .then((response) => {
        return response.json();
      })
      .then((result) => {
        if (result.code === 200) {
          alert("정산되었습니다");
          setReload(!reload);
        } else {
          ErrorCode(result);
        }
      })
      .catch((err) => {
        alert("통신실패 " + err);
      });
  }

  function sprintDelete() {
    const ask = window.confirm("모집중인 스프린트를 삭제하시겠습니까?");
    if (!ask) {
      return;
    }
    fetch(`${props.href}/sprints/${sprintJoin.sprintId}/delete`, {
      method: "POST",
      headers: {
        accessToken: getCookie("accessToken"),
      },
    })
      .then((response) => {
        return response.json();
      })
      .then((result) => {
        if (result.code === 200) {
          alert("모집중인 스프린트를 삭제했습니다");
          setReload(!reload);
        } else {
          ErrorCode(result);
        }
      })
      .catch((err) => {
        alert("통신실패 " + err);
      });
  }
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
        } else {
          ErrorCode(result);
        }
      })
      .catch((err) => {
        alert("통신실패 " + err);
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
        if (result.code === 200) {
          const roomId = result.reponseData.roomId;
          navigate(`/RoomDetail/${roomId}`);
        } else {
          ErrorCode(result);
        }
      })
      .catch((err) => {
        alert("입장실패 " + err);
      });
  }
  function sprintCancel() {
    const ask = window.confirm("스프린트 참가 취소하시겠습니까?");
    if (!ask) {
      return;
    }
    fetch(`${url}/${sprintJoin.sprintId}/cancel`, {
      method: "POST",
      headers: {
        accessToken: getCookie("accessToken"),
      },
    })
      .then((response) => {
        return response.json();
      })
      .then((result) => {
        if (result.code === 200) {
          alert("성공적으로 취소되었습니다");
          setReload(!reload);
        } else {
          ErrorCode(result);
        }
      })
      .catch((err) => {
        alert("입장실패 " + err);
      });
  }

  return (
    <div id="group-detail__sprint">
      {/* sprintJoin */}
      <div id="sprint-join">
        <div id="sprint-join-header">
          <strong>
            모집중인 스프린트<i>(👇클릭)</i>
          </strong>
          {props.role !== 2 && sprintJoin.joined && (
            <button onClick={sprintCancel}>스프린트 참가 취소</button>
          )}
          {props.role === 0 && sprintJoin.length > 0 && (
            <button onClick={sprintDelete}>스프린트 삭제</button>
          )}
        </div>
        <SprintItem
          sprint={sprintJoin}
          handler={sprintJoinHandler}
          sprintCancel={sprintCancel}
        />
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
          <div id="sprint-done-header">
            <strong>지난 스프린트</strong>
            {props.role !== 2 && sprintDone.length > 0 && (
              <button onClick={sprintCal}>스프린트 정산</button>
            )}
          </div>

          {sprintDone.map((sprint, index) => {
            return <SprintItem sprint={sprint} key={index} />;
          })}
        </div>
      )}
    </div>
  );
}

export default GroupDetailSprint;
