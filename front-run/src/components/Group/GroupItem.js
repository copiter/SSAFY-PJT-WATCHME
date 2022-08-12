import React, { useContext } from "react";
import { useNavigate } from "react-router-dom";

import "./GroupItem.css";

function GroupItem(props) {
  const group = props.group;

  const navigate = useNavigate();

  function enteringGroup(id) {
    navigate(`/GroupDetail/${id}`);
  }

  return (
    <article onClick={() => enteringGroup(group.id)}>
      <div
        className="group-specs"
        style={{
          backgroundImage: `url(${group.imgLink})`,
          backgroundSize: "cover",
          width: `${props.width}px`,
          height: `${props.height}px`,
        }}
      >
        <div className="backdrop">
          <dl className="group-info">
            <div className="category member-no">
              <dt className="sr-only">카테고리</dt>
              <dl>{group.ctg !== undefined && group.ctg.join(" | ")}</dl>
            </div>
            <div>
              <dt className="sr-only">이름</dt>
              <dl>
                {group.secret ? "🔒" : null}
                {group.name}
              </dl>
            </div>
            <div className="info-content">
              <div>
                <dt className="sr-only">세부설명</dt>
                <dl>{group.description}</dl>
              </div>
              <div id="info-member">
                <dt className="sr-only">인원수</dt>
                <dl>
                  &#128509;
                  {group.currMember}/{group.maxMember}
                </dl>
              </div>
            </div>
          </dl>
        </div>
        {!props.myGroup && (
          <div className="group-specs__rules">
            <span>🏃‍♂️ 스프린트</span>
            {group.hasOwnProperty("sprint") &&
            group.sprint.hasOwnProperty("status") ? (
              <>
                <li>
                  <p>{group.sprint.name}</p>
                </li>
                <li>
                  <p>{group.sprint.description}</p>
                </li>
                <li>
                  <p>
                    {group.sprint.startAt} ~ {group.sprint.endAt}
                  </p>
                </li>
              </>
            ) : (
              <p>현재 모집중인 스프린트가 없습니다</p>
            )}
          </div>
        )}
      </div>
    </article>
  );
}

export default GroupItem;
