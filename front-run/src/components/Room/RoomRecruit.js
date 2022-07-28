import React from "react";
import { Link } from "react-router-dom";
import { useState, useContext } from "react";
import roomInfor from "../json/roomInfor";
import { FetchUrl } from "../../store/communication";

import "./RoomRecruit.css";
import btnPlane from "../../img/Icons/btn-plane.png";
import filter from "../../img/Icons/filter.png";
import down from "../../img/Icons/down.png";

let roomPageNo = 0;

function RoomRecruit() {
  const FETCH_URL = useContext(FetchUrl);

  const [inputs, setInputs] = useState({
    roomCategory: "",
    roomSearch: "",
  });

  const handleChange = (event) => {
    const name = event.target.name;
    const value = event.target.value;
    setInputs((values) => ({ ...values, [name]: value }));
  };

  //URL
  const url = `${FETCH_URL}/RoomRecruit"`;
  //Otpion

  let rooms = roomInfor[0]["MainpageRooms"];
  let roomNo = 0;

  const handleSubmit = (event) => {
    event.preventDefault();
    console.log(
      url +
        "?roomCategory=" +
        inputs["roomCategory"] +
        "&roomSearch=" +
        inputs["roomSearch"] +
        "&pageNo=0"
    );
    fetch(
      url +
        "?roomCategory=" +
        inputs["roomCategory"] +
        "&roomSearch=" +
        inputs["roomSearch"] +
        "&pageNo=0"
    )
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else {
          response.json().then((data) => {
            let errorMessage = "검색오류 발생";
            throw new Error(errorMessage);
          });
        }
      })
      .then((result) => {
        rooms = result;
      })
      .catch((err) => {
        console.log(err.message);
      });
  };

  const addMore = (event) => {
    roomPageNo++;
    console.log(
      url +
        "?roomCategory=" +
        inputs["roomCategory"] +
        "&roomSearch=" +
        inputs["roomSearch"] +
        "&pageNo=" +
        roomPageNo
    );
    fetch(
      url +
        "?roomCategory=" +
        inputs["roomCategory"] +
        "&roomSearch=" +
        inputs["roomSearch"] +
        "&pageNo=" +
        roomPageNo
    )
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else {
          response.json().then((data) => {
            let errorMessage = "검색오류 발생";
            throw new Error(errorMessage);
          });
        }
      })
      .then((result) => {
        rooms = result;
      })
      .catch((err) => {
        console.log(err.message);
      });
  };

  return (
    <div id="open-room">
      {/* 공개룸 찾기 section */}
      <form onSubmit={handleSubmit}>
        <div className="open-room__search">
          <div className="search__info">
            <strong>공개룸 찾기</strong>
            <small>Search Open Room</small>
          </div>
          <div className="search__input">
            <input
              type="text"
              name="roomSearch"
              value={inputs.roomSearch || ""}
              onChange={handleChange}
              placeholder="찾는 공개룸을 입력하세요"
            />
            <button type="submit">
              <img src={btnPlane} alt="검색" />
            </button>
          </div>
        </div>
      </form>

      {/* 아래 공개룸 보여지는 페이지 */}
      <div className="open-room__module">
        <div className="module__header">
          {/* select tag -> ul tag 로 변경했습니다 
              NavLink 사용해도 괜찮을 것 같습니다 */}
          <ul className="header__tags">
            <li>All</li>
            <li className="active">공무원</li>
            <li>취업</li>
            <li>수능</li>
            <li>자격증</li>
            <li>기타</li>
          </ul>

          {/* handleChange 확인 */}
          {/* <select
            name="roomTag"
            value={inputs.roomTag || ""}
            onChange={handleChange}
          >
            <option value="">All</option>
            <option value="공무원">공무원</option>
            <option value="취업">취업</option>
            <option value="수능">수능</option>
            <option value="자격증">자격증</option>
            <option value="기타">기타</option>
          </select> */}
          <div className="header__right">
            <Link className="header__link" to="/RoomCreate">
              공개룸 만들기
            </Link>
            <button className="header__filter">
              <img src={filter} alt="필터" />
              Filters
            </button>
          </div>
          {/*아직 미구현예정 */}
        </div>

        {/* 그룹들 모여 있는 부분. 나중에 component화 예정 */}
        <div className="module__rooms">
          <ul className="rooms__whole">
            <li>
              <Link to="/RoomDetail">
                <article>
                  <div className="group-specs">
                    {/* 미팅룸 이미지가 background가 되거나
                        img태그 자체를 적용해야 합니다*/}
                    <img
                      src={
                        rooms[roomNo]["roomImage"] === "none"
                          ? "(이미지없음)"
                          : rooms[roomNo]["roomImage"]
                      }
                      alt="룸 이미지"
                    />
                    <div className="group-specs__rules">
                      <span>📝 규칙</span>
                      <ul>
                        <li>✔ 휴대폰 인식</li>
                        <li>✔ 얼굴 인식</li>
                        <li>✔ 캠 켜기</li>
                      </ul>
                    </div>
                  </div>

                  <dl className="group-info">
                    <div className="category member-no">
                      <dt className="sr-only">카테고리</dt>
                      <dl>{rooms[roomNo]["roomCategory"]}</dl>
                      <dt className="sr-only">인원수</dt>
                      <dl>
                        &#128509;
                        {rooms[roomNo]["romMemberNo"]}/
                        {rooms[roomNo]["roomMemberMaxNo"]}
                      </dl>
                    </div>
                    <div>
                      <dt className="sr-only">이름</dt>
                      <dl>{rooms[roomNo]["roomName"]}</dl>
                    </div>
                    <div className="info-content">
                      <dt className="sr-only">세부설명</dt>
                      <dl>{rooms[roomNo]["roomDiscription"]}</dl>
                    </div>
                  </dl>
                </article>
              </Link>
            </li>
            <li>
              <Link to="/RoomDetail">
                <article>
                  <div className="group-specs">
                    {/*미팅룸 이미지 내부에 기능들 표기됨*/}
                    <img
                      src={
                        rooms[roomNo]["roomImage"] === "none"
                          ? "(이미지없음)"
                          : rooms[roomNo]["roomImage"]
                      }
                      alt="룸 이미지"
                    />
                    <div className="group-specs__rules">
                      <span>📝 규칙</span>
                      <ul>
                        <li>✔ 휴대폰 인식</li>
                        <li>✔ 얼굴 인식</li>
                        <li>✔ 캠 켜기</li>
                      </ul>
                    </div>
                  </div>

                  <dl className="group-info">
                    <div className="category member-no">
                      <dt className="sr-only">카테고리</dt>
                      <dl>{rooms[roomNo]["roomCategory"]}</dl>
                      <dt className="sr-only">인원수</dt>
                      <dl>
                        &#128509;
                        {rooms[roomNo]["romMemberNo"]}/
                        {rooms[roomNo]["roomMemberMaxNo"]}
                      </dl>
                    </div>
                    <div>
                      <dt className="sr-only">이름</dt>
                      <dl>{rooms[roomNo]["roomName"]}</dl>
                    </div>
                    <div className="info-content">
                      <dt className="sr-only">세부설명</dt>
                      <dl>{rooms[roomNo]["roomDiscription"]}</dl>
                    </div>
                  </dl>
                </article>
              </Link>
            </li>
            <li>
              <Link to="/RoomDetail">
                <article>
                  <div className="group-specs">
                    {/*미팅룸 이미지 내부에 기능들 표기됨*/}
                    <img
                      src={
                        rooms[roomNo]["roomImage"] === "none"
                          ? "(이미지없음)"
                          : rooms[roomNo]["roomImage"]
                      }
                      alt="룸 이미지"
                    />
                    <div className="group-specs__rules">
                      <span>📝 규칙</span>
                      <ul>
                        <li>✔ 휴대폰 인식</li>
                        <li>✔ 얼굴 인식</li>
                        <li>✔ 캠 켜기</li>
                      </ul>
                    </div>
                  </div>

                  <dl className="group-info">
                    <div className="category member-no">
                      <dt className="sr-only">카테고리</dt>
                      <dl>{rooms[roomNo]["roomCategory"]}</dl>
                      <dt className="sr-only">인원수</dt>
                      <dl>
                        &#128509;
                        {rooms[roomNo]["romMemberNo"]}/
                        {rooms[roomNo]["roomMemberMaxNo"]}
                      </dl>
                    </div>
                    <div>
                      <dt className="sr-only">이름</dt>
                      <dl>{rooms[roomNo]["roomName"]}</dl>
                    </div>
                    <div className="info-content">
                      <dt className="sr-only">세부설명</dt>
                      <dl>{rooms[roomNo]["roomDiscription"]}</dl>
                    </div>
                  </dl>
                </article>
              </Link>
            </li>
            <li>
              <Link to="/RoomDetail">
                <article>
                  <div className="group-specs">
                    {/*미팅룸 이미지 내부에 기능들 표기됨*/}
                    <img
                      src={
                        rooms[roomNo]["roomImage"] === "none"
                          ? "(이미지없음)"
                          : rooms[roomNo]["roomImage"]
                      }
                      alt="룸 이미지"
                    />
                    <div className="group-specs__rules">
                      <span>📝 규칙</span>
                      <ul>
                        <li>✔ 휴대폰 인식</li>
                        <li>✔ 얼굴 인식</li>
                        <li>✔ 캠 켜기</li>
                      </ul>
                    </div>
                  </div>

                  <dl className="group-info">
                    <div className="category member-no">
                      <dt className="sr-only">카테고리</dt>
                      <dl>{rooms[roomNo]["roomCategory"]}</dl>
                      <dt className="sr-only">인원수</dt>
                      <dl>
                        &#128509;
                        {rooms[roomNo]["romMemberNo"]}/
                        {rooms[roomNo]["roomMemberMaxNo"]}
                      </dl>
                    </div>
                    <div>
                      <dt className="sr-only">이름</dt>
                      <dl>{rooms[roomNo]["roomName"]}</dl>
                    </div>
                    <div className="info-content">
                      <dt className="sr-only">세부설명</dt>
                      <dl>{rooms[roomNo]["roomDiscription"]}</dl>
                    </div>
                  </dl>
                </article>
              </Link>
            </li>
          </ul>
        </div>
        <button type="button" id="more-btn" name="roomPageNo" onClick={addMore}>
          <img src={down} alt="+" />
          더보기
        </button>
      </div>
    </div>
  );
}

export default RoomRecruit;
