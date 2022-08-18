import React from "react";

import "./SprintItem.css";

function SprintItem(props) {
  const sprint = props.sprint;
  console.log(sprint);
  const title =
    sprint.status === "YES"
      ? "참가하시려면 클릭하세요"
      : sprint.status === "ING"
      ? "입장하시려면 클릭하세요"
      : null;
  const mode = ["", "규칙없음", "졸림 감지", "스마트폰 감시", "화면공유 필수"];

  return (
    <>
      {!sprint.hasOwnProperty("sprintId") && (
        <div id="no-sprint">
          <p>스프린트가 없습니다</p>
        </div>
      )}
      {sprint.hasOwnProperty("sprintId") && (
        <div id="sprint-wrap">
          <div
            className="sprint__content"
            onClick={props.handler === undefined ? null : props.handler}
            title={title}
          >
            <div
              className="sprint__content-item-img"
              style={{
                backgroundImage: `url(${sprint.sprintImg})`,
                backgroundSize: "cover",
              }}
            ></div>
            <div className="sprint__content-item-desc">
              <span>{sprint.name}</span>
              <small>{sprint.description}</small>
            </div>
            <div className="sprint__content-item-info">
              <ul>
                <li className="item-info-date">
                  {`${sprint.startAt} ~ ${sprint.endAt}`}
                </li>
                <li>
                  <span>
                    <b>🕑 스프린트 루틴 </b>
                    {`${sprint.routineStartAt} ~ ${sprint.routineEndAt}`}
                  </span>
                </li>
                <li>
                  <span>
                    <b>⏳ 목표 </b>
                    {sprint.goal}
                  </span>
                </li>
                <li>
                  <span>
                    #{mode[+sprint.mode.slice(-1)]}
                    {` #${+(
                      sprint.routineEndAt.split(":")[0] -
                      sprint.routineStartAt.split(":")[0]
                    )}시간 #${sprint.fee}원
                `}
                  </span>
                </li>
              </ul>
            </div>

            {/* 모집중이면 fee, 나머지는 status */}
            {sprint.status === "YES" && (
              <div id="sprint-join__content-item-fee">
                <span>{`💸 참가비 ${sprint.fee}원`}</span>
                <small>{`벌점당 차감액 ${sprint.penaltyMoney}원`}</small>
              </div>
            )}
            {sprint.status !== "YES" && (
              <div className="sprint__content-item-status">
                <span className="content-status-title">스프린트 현황</span>
                {sprint.status === "NO" && (
                  <button
                    id="sprintCal-btn"
                    onClick={() => props.sprintCal(sprint.sprintId)}
                  >
                    스프린트 정산
                  </button>
                )}
                <ul>
                  <li>
                    <small>⏰ 내 공부시간</small>
                    <span>
                      {sprint.myStudy === undefined
                        ? "0분"
                        : `${parseInt(sprint.myStudy / 60)}시간 ${
                            sprint.myStudy % 60
                          }분`}
                    </span>
                  </li>
                  <li>
                    <small>😥 벌점 차감액</small>
                    <span>
                      {sprint.myPenalty === undefined
                        ? 0
                        : sprint.myPenalty * sprint.penaltyMoney >= sprint.fee
                        ? sprint.fee
                        : sprint.myPenalty * sprint.penaltyMoney}
                      원
                    </span>
                  </li>
                  <li>
                    <small>공부시간 총합</small>
                    <span>
                      {`${parseInt(sprint.studySum / 60)}시간 ${
                        sprint.studySum % 60
                      }분`}
                    </span>
                  </li>
                  <li>
                    <small>패널티 총합</small>
                    <span>{`${sprint.penaltySum}회`}</span>
                  </li>
                </ul>
              </div>
            )}
          </div>

          {sprint.status !== "YES" && (
            <div className="sprint-king">
              <div className="sprint-king-title">
                <span className="content-status-title">
                  {`공부왕🥇 ${sprint.kingName}`}
                </span>
                <div
                  className="sprint-king-img"
                  style={{
                    backgroundImage: `url(${sprint.sprintImg})`,
                    backgroundSize: "cover",
                  }}
                ></div>
                <span>{sprint.kingName}</span>
              </div>
              <ul>
                <li>
                  <small>⏰ 공부시간</small>
                  <span>
                    {sprint.kingStudy === undefined ? 0 : sprint.kingStudy}원
                  </span>
                </li>
                <li>
                  <small>패널티 횟수</small>
                  <span>
                    {sprint.kingPenalty === undefined ? 0 : sprint.kingPenalty}
                    회
                  </span>
                </li>
              </ul>
            </div>
          )}
        </div>
      )}
    </>
  );
}

export default SprintItem;
