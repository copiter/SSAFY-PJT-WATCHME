import React from "react";

import crown from "../../../img/Icons/crown.png";
import "./GroupDetailHome.css";

const GroupDetailHome = (props) => {
  const resData = props.resData;
  const groupTotalTime = resData.groupData.sumTime;
  const crewsData = resData.members.filter((member) => member.role == 0);
  const onGoingSprint = resData.sprints[0]; //하나만 들어와야 함.

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
              <span>{`${resData.myData.penaltyScore}회`}</span>
            </li>
            <li>
              <small>📆 그룹 가입일</small>
              <span>{`${resData.myData.joinDate}`}</span>
            </li>
          </ul>
        </div>
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
            {crewsData.map((item, index) => (
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
            ))}
          </ul>
        </div>
        <div id="group-detail__sprint-summary">
          <div id="sprint-summary-title">
            <strong>진행중인 스프린트</strong>
            <a href="#none">스프린트 만들기</a>
          </div>
          <div id="sprint-summary-content">
            {resData.sprints === [] && (
              <p id="sprint-none">진행 중인 스프린트가 없습니다!</p>
            )}
            {resData.sprints !== [] && (
              <>
                <p id="sprint-exists-title">{onGoingSprint.name}</p>
                <ul>
                  <li>{`🕑 ${onGoingSprint.routineStartAt} ~ ${onGoingSprint.routineEndAt} 참여 필수`}</li>
                  <li>⏳ {onGoingSprint.description}</li>
                  <li>{`💸 참가비 ${onGoingSprint.fee}원`}</li>
                </ul>
                <span>#{onGoingSprint.sprintRuleList.join(" #")}</span>
              </>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default GroupDetailHome;
