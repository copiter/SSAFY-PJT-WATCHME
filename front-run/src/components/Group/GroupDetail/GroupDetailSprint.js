import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { getCookie } from "../../../Cookie";
import ErrorCode from "../../../Error/ErrorCode";
import swal from "sweetalert";

import "./GroupDetailSprint.css";
import json from "../../json/groupdetailsprint.json";

import SprintItem from "./SprintItem";

function GroupDetailSprint(props) {
  // const [sprints, setSprints] = useState(json.responseData.sprints);
  const [sprints, setSprints] = useState([]);
  const [reload, setReload] = useState(false);
  const navigate = useNavigate();
  // const mode = ["규칙없음", "졸림 감지", "스마트폰 감시", "화면공유 필수"];

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
    swal({
      title: "정산하시겠습니까?",
      text: "",
      icon: "info",
      buttons: true,
      dangerMode: true,
    }).then((willDelete) => {
      if (willDelete) {
        fetch(`${props.href}/sprints/${sprintJoin.sprintId}/points`, {
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
              swal("정산되었습니다", "", "success");
              setReload(!reload);
            } else {
              ErrorCode(result);
            }
          })
          .catch((err) => {
            swal("통신실패", "", "error");
          });
      } else {
        return;
      }
    });
  }

  function sprintDelete() {
    swal({
      title: "모집중인 스프린트를 삭제하시겠습니까?",
      text: "",
      icon: "warning",
      buttons: true,
      dangerMode: true,
    }).then((willDelete) => {
      if (willDelete) {
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
              swal("모집중인 스프린트를 삭제했습니다", "", "success");
              setReload(!reload);
            } else {
              ErrorCode(result);
            }
          })
          .catch((err) => {
            swal("통신실패", "", "error");
          });
      } else {
        return;
      }
    });
  }
  function sprintJoinHandler() {
    swal({
      title: "스프린트 참가신청 하시겠습니까?",
      text: "",
      icon: "info",
      buttons: true,
      dangerMode: true,
    }).then((willDelete) => {
      if (willDelete) {
        fetch(`${props.href}/sprints/${sprintJoin.sprintId}/join`, {
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
              swal("스프린트에 정상적으로 참가신청 되었습니다", "", "success");
              setReload(!reload);
            } else {
              ErrorCode(result);
            }
          })
          .catch((err) => {
            swal("통신실패", "", "error");
          });
      } else {
        return;
      }
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
        swal("통신실패", "", "error");
      });
  }
  function sprintCancel() {
    swal({
      title: "스프린트 참가 취소하시겠습니까?",
      text: "",
      icon: "warning",
      buttons: true,
      dangerMode: true,
    }).then((willDelete) => {
      if (willDelete) {
        fetch(`${props.href}/sprints/${sprintJoin.sprintId}/cancel`, {
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
              swal("성공적으로 취소되었습니다", "", "success");
              setReload(!reload);
            } else {
              ErrorCode(result);
            }
          })
          .catch((err) => {
            swal("통신실패", "", "error");
          });
      } else {
        return;
      }
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
