import React from "react";

import { Link } from "react-router-dom";

import crown from "../../../img/Icons/crown.png";
import "./GroupDetailHome.css";

const GroupDetailHome = (props) => {
  console.log(props);

  const resData = props.resData;
  // const groupTotalTime = resData.groupData.sumTime;
  const groupTotalTime = 0;

  let penalty = [0, 0, 0];
  if (resData.myData.hasOwnProperty("penalty")) {
    for (let index in resData.myData.penalty) {
      penalty[index] = resData.myData.penalty[index];
    }
  }

  const mode = ["", "규칙없음", "졸림 감지", "스마트폰 감시", "화면공유 필수"];

  return (
    <div id="group-detail__home">
      <div id="group-detail__home-top">
        <div id="group-detail__group-achievement">
          <strong>그룹 달성치 ✨</strong>
          <ul>
            {groupTotalTime >= 600000 && <li>🥇 10000시간 달성!</li>}
            {groupTotalTime >= 60000 && <li>🥈 1000시간 달성!</li>}
            <li>{`🎉 총 ${parseInt(groupTotalTime / 60)}시간 ${
              groupTotalTime % 60
            }분`}</li>
          </ul>
        </div>
        {resData.myData.role === 2 && (
          <div id="group-detail__my-achievement">
            <strong>나의 공부 달성치 🙋‍♂️</strong>
            <div id="mydata-anonymous">
              <p>그룹에 가입해보세요!</p>
            </div>
          </div>
        )}
        {resData.myData.role !== 2 && (
          <div id="group-detail__my-achievement">
            <strong>나의 공부 달성치 🙋‍♂️</strong>
            <ul>
              <li>
                <small>⏰ 함께 공부한 시간</small>
                <span>{`${parseInt(resData.myData.studyTime / 60)}시간 ${
                  resData.myData.studyTime % 60
                }분`}</span>
              </li>
              <li>
                <small>😥 페널티 받은 횟수 </small>
                {/* <span>{`📱${penalty[0]} / 😴${penalty[1]}`}</span> */}
              </li>
              <li>
                <small>📆 그룹 가입일</small>
                <span>{`${resData.myData.joinDate}`}</span>
              </li>
            </ul>
          </div>
        )}
      </div>
      <div id="group-detail__home-bottom">
        <div id="group-detail__group-members">
          <strong>{`Group Member: ${resData.group.currMember}`}</strong>
          <ul id="group-detail__group-members-list">
            <li>
              <div
                className="group-detail__group-members-list-item"
                style={{
                  backgroundImage: `url(${resData.leader.imgLink})`,
                  backgroundSize: "cover",
                }}
              >
                <img
                  id="crown"
                  src={crown}
                  alt="리더왕관"
                  style={{ width: "20px" }}
                />
              </div>
              <span>{resData.leader.nickName}</span>
            </li>
            {resData.members !== null &&
              resData.members.length > 0 &&
              resData.members
                .filter((member) => member.role === 1)
                .map((item, index) => {
                  return (
                    <li key={index}>
                      <div
                        className="group-detail__group-members-list-item"
                        style={{
                          backgroundImage: `url(${item.imgLink})`,
                          backgroundSize: "cover",
                        }}
                      ></div>
                      <span>{item.nickName}</span>
                    </li>
                  );
                })}
          </ul>
        </div>
        <div id="group-detail__sprint-summary">
          <div id="sprint-summary-title">
            <strong>진행중인 스프린트</strong>
            {resData.myData.role === 0 && (
              <Link to={`/SprintCreate/${props.groupId}`}>스프린트 만들기</Link>
            )}
          </div>
          <div id="sprint-summary-card">
            {resData.sprints.length === 0 && (
              <div className="sprint-summary-content">
                <p id="sprint-none">진행 중인 스프린트가 없습니다!</p>
              </div>
            )}
            {resData.sprints.length > 0 &&
              resData.sprints.map((sprint, index) => {
                return (
                  <div className="sprint-summary-content" key={index}>
                    <p className="sprint-exists-title">{sprint.name}</p>
                    <ul>
                      <li>{`🕑 루틴 : ${sprint.routineStartAt} ~ ${sprint.routineEndAt} 참여 필수`}</li>
                      <li>{`⏳ 목표 : ${sprint.goal}`}</li>
                      <li>{`⛏ 규칙 : ${mode[+sprint.mode.slice(-1)]}`}</li>
                      <li>{`💸 참가비 : ${
                        sprint.fee === null ? 0 : sprint.fee
                      }원`}</li>
                    </ul>
                  </div>
                );
              })}
          </div>
        </div>
      </div>
    </div>
  );
};

export default GroupDetailHome;
