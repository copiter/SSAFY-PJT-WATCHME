import React, { useState, useContext, useEffect } from "react";
import { FetchUrl } from "../../store/communication";
import { getCookie } from "../../Cookie";

import "./MyGroups.css";
import json from "../json/mygroups.json";
import { useNavigate } from "react-router-dom";
import ErrorCode from "../../Error/ErrorCode";

function MyGroups() {
  // const [datas, setDatas] = useState(json.responseData.myGroups);
  const [datas, setDatas] = useState({ myGroups: [] });
  const navigate = useNavigate();

  const FETCH_URL = useContext(FetchUrl);
  const url = `${FETCH_URL}/members/mygroup`;
  useEffect(() => {
    const config = {
      method: "GET",
      headers: {
        accessToken: getCookie("accessToken"),
      },
    };
    const getDatas = async () => {
      const response = await fetch(url, config);
      const data = await response.json();
      if (data.code === 200) {
        setDatas(data.responseData.myGroups);
      } else {
        ErrorCode(data);
      }
    };
    getDatas();
  }, []);

  function navigateHandler(id) {
    navigate(`/GroupDetail/${id}`);
  }

  console.log(datas);

  return (
    <div id="my-group">
      <strong>내 그룹</strong>
      {datas.length > 0 &&
        datas
          .sort((a, b) => {
            return a.myData.joinDate - b.myData.joinDate;
          })
          .map((data, index) => {
            return (
              <div
                className="my-group__item"
                onClick={() => navigateHandler(data.myGroup.id)}
                key={index}
              >
                <div className="my-group__img">
                  <img
                    src={data.myGroup.imgLink}
                    alt="그룹 사진"
                    id="group-detail__img"
                  />
                </div>
                <div className="my-group__desc">
                  <div className="my-group__desc-title">
                    <strong>{data.myGroup.name}</strong>
                    <div className="desc-title__ctg">
                      {data.myGroup.ctg.map((item, index) => (
                        <li key={index} className="desc-title__ctg-item">
                          {item}
                        </li>
                      ))}
                    </div>
                  </div>
                  <div className="my-group__desc-desc">
                    <p>{data.myGroup.description}</p>
                  </div>
                </div>
                <div className="my-group__achvmnt">
                  <ul>
                    <li>
                      <small>⏰ 함께 공부한 시간</small>
                      <span>{`${parseInt(data.myData.studyTime / 60)}시간 ${
                        data.myData.studyTime % 60
                      }분`}</span>
                    </li>
                    <li>
                      <small>😥 페널티 받은 횟수 </small>
                      <span>{`😴${data.myData.penalty[1]} / 📱${data.myData.penalty[2]}`}</span>
                    </li>
                    <li>
                      <small>📆 그룹 가입일</small>
                      <span>{`${data.myData.joinDate}`}</span>
                    </li>
                  </ul>
                </div>
              </div>
            );
          })}
    </div>
  );
}

export default MyGroups;
