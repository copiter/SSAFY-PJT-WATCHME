import React from "react";
import { useState, useContext, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import GroupItem from "./GroupItem";

import "./GroupRecruit.css";
import { FetchUrl } from "../../store/communication";
import getCookie from "../../Cookie";

import btnPlane from "../../img/Icons/btn-plane.png";
import filter from "../../img/Icons/filter.png";
import down from "../../img/Icons/down.png";
import groupJsons from "../json/groupRec.json";

let page = 1;

function GroupRecruit() {
  //Search 못맞춰서 작동 안됩니다...
  const FETCH_URL = useContext(FetchUrl);
  const navigate = useNavigate();
  const [inputs, setInputs] = useState({
    ctg: "",
    groupSearch: "",
    keyword: "",
  });

  const [groups, setGroups] = useState(groupJsons.responseData.groups);

  const handleChange = (event) => {
    const name = event.target.name;
    const value = event.target.value;
    setInputs((values) => ({ ...values, [name]: value }));
  };

  //URL
  const url = `${FETCH_URL}/groups`;
  useEffect(() => {
    fetch(url)
      .then((response) => {
        if (response.bodyUsed) {
          console.log("재사용됨");
        } else if (response.ok) {
          return response.json();
        } else {
          console.log("기본Case4");
        }
      })
      .then((result) => {
        setGroups(result["responseData"]["groups"]);
      })
      .catch((err) => {
        console.log("ERROR");
      });
  }, []);

  const handleSubmit = (event) => {
    //제출하기 실제 값 적용안됨
    event.preventDefault();
    fetch(
      url +
        "?" +
        (inputs.ctg === "" ? "" : "ctg=" + inputs.ctg + "&") +
        (inputs.keyword === "" ? "" : "keyword=" + inputs.keyword)
    )
      .then((response) => {
        if (response.bodyUsed) {
          console.log("재사용됨");
        } else if (response.ok) {
          return response.json();
        } else {
          console.log("제출Case4");
        }
      })
      .then((result) => {
        console.log(1);
        console.log(result["responseData"]["groups"]);
        setGroups(result["responseData"]["groups"]);
      })
      .catch((err) => {
        console.log("ERROR");
      });
  };

  const addMore = (event) => {
    //값 입력 안받은상태임
    page++;
    console.log(
      "실제로 들어가야하는 방식 : "(
        url +
          "?" +
          (inputs.ctg === "" ? "" : "ctg=" + inputs.ctg + "&") +
          (page === "" || page === 1 ? "" : "page=" + page + "&") +
          "keyword=석인방"
      )
    );
    fetch(
      url +
        "?" +
        (inputs.ctg === "" ? "" : "ctg=" + inputs.ctg + "&") +
        (page === "" || page === 1 ? "" : "page=" + page + "&") +
        "keyword=석인방"
    )
      .then((response) => {
        if (response.bodyUsed) {
          console.log("재사용됨");
        } else if (response.ok) {
          return response.json();
        } else {
          console.log("C4");
        }
      })
      .then((result) => {
        setGroups(result["responseData"]["groups"]);
      })
      .catch((err) => {
        console.log("ERROR");
      });
  };

  const ctgChange = (event) => {
    //카테고리 변동(LI라서 이방법 사용)
    event.preventDefault();
    const ARR = ["all", "공무원", "취업", "수능", "자격증", "기타"];

    setInputs((values) => ({ ...values, ctg: ARR[event.target.value] }));
    console.log(ARR[event.target.value]);
    fetch(
      url +
        "?" +
        (ARR[event.target.value] === "" || ARR[event.target.value] === "all"
          ? ""
          : "ctg=" + ARR[event.target.value] + "&") +
        (inputs.keyword === "" ? "" : "keyword=" + inputs.keyword)
    )
      .then((response) => {
        if (response.bodyUsed) {
          console.log("재사용됨");
        } else if (response.ok) {
          return response.json();
        } else {
          console.log("제출Case4");
        }
      })
      .then((result) => {
        console.log(1);
        console.log(result["responseData"]["groups"]);
        setGroups(result["responseData"]["groups"]);
      })
      .catch((err) => {
        console.log("ERROR");
      });
  };

  return (
    <div id="open-group">
      {/* 공개룸 찾기 section */}
      <form onSubmit={handleSubmit}>
        <div className="open-group__search">
          <div className="search__info">
            <strong>스터디 그룹 찾기</strong>
            <small>Search Study Groups</small>
          </div>
          <div className="search__input">
            <input
              type="text"
              name="keyword"
              value={inputs.keyword || ""}
              onChange={handleChange}
              placeholder="찾고자 하는 그룹을 입력하세요"
            />
            <button type="submit">
              <img src={btnPlane} alt="검색" />
            </button>
          </div>
        </div>
      </form>

      {/* 아래 공개룸 보여지는 페이지 */}
      <div className="open-group__module">
        <div className="module__header">
          {/* select tag -> ul tag 로 변경했습니다 
              NavLink 사용해도 괜찮을 것 같습니다 */}
          <ul className="header__tags">
            <li
              className={
                inputs.ctg === "" || inputs.ctg === "all" ? "active" : ""
              }
              onClick={ctgChange}
              value={0}
            >
              All
            </li>
            <li
              className={inputs.ctg === "공무원" ? "active" : ""}
              onClick={ctgChange}
              value={1}
            >
              공무원
            </li>
            <li
              className={inputs.ctg === "취업" ? "active" : ""}
              onClick={ctgChange}
              value={2}
            >
              취업
            </li>
            <li
              className={inputs.ctg === "수능" ? "active" : ""}
              onClick={ctgChange}
              value={3}
            >
              수능
            </li>
            <li
              className={inputs.ctg === "자격증" ? "active" : ""}
              onClick={ctgChange}
              value={4}
            >
              자격증
            </li>
            <li
              className={inputs.ctg === "기타" ? "active" : ""}
              onClick={ctgChange}
              value={5}
            >
              기타
            </li>
          </ul>
          <div className="header__right">
            <Link className="header__link" to="/GroupCreate">
              그룹 만들기
            </Link>
            <button className="header__filter">
              <img src={filter} alt="필터" />
              Filters
            </button>
          </div>
          {/*아직 미구현예정 */}
        </div>
        <ul id="groups__whole" value={0}>
          {groups.map((group, index) => {
            return (
              <li key={index}>
                <GroupItem group={group} width="329" height="285" />
              </li>
            );
          })}
        </ul>
        <button type="button" id="more-btn" name="grouppage" onClick={addMore}>
          <img src={down} alt="+" />
          더보기
        </button>
      </div>
    </div>
  );
}

export default GroupRecruit;
