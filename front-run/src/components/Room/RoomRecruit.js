import React from "react";
import { Link, useNavigate } from "react-router-dom";
import { useState } from "react";
import roomInfor from "../json/roomInfor";

let roomPageNo = 0;

function RoomRecruit() {
  const [inputs, setInputs] = useState({
    roomTag: "",
    roomSearch: "",
  });
  const navigate = useNavigate();

  const handleChange = (event) => {
    const name = event.target.name;
    const value = event.target.value;
    setInputs((values) => ({ ...values, [name]: value }));
  };

  //URL
  const url = "http://localhost:81/RoomRecruit";
  //Otpion

  let rooms = roomInfor[0]["MainpageRooms"];
  let roomNo = 0;

  const handleSubmit = (event) => {
    event.preventDefault();
    {
      /*완성후삭제 */
    }
    console.log(
      url +
        "?roomTag=" +
        inputs["roomTag"] +
        "&roomSearch=" +
        inputs["roomSearch"] +
        "&pageNo=0"
    );
    fetch(
      url +
        "?roomTag=" +
        inputs["roomTag"] +
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
    {
      /*완성후삭제 */
    }
    console.log(
      url +
        "?roomTag=" +
        inputs["roomTag"] +
        "&roomSearch=" +
        inputs["roomSearch"] +
        "&pageNo=" +
        roomPageNo
    );
    fetch(
      url +
        "?roomTag=" +
        inputs["roomTag"] +
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
    <form onSubmit={handleSubmit}>
      <div id="위에 검색창">
        <div>공개룸찾기</div>
        <input
          type="text"
          name="roomSearch"
          value={inputs.roomSearch || ""}
          onChange={handleChange}
          placeholder="찾는 공개룸을 입력하세요"
        />
        <input type="submit" />
      </div>
      <div id="흰부분">
        <div id="up">
          <div id="tags">
            <select
              name="roomTag"
              value={inputs.roomTag || ""}
              onChange={handleChange}
            >
              <option value="">all</option>
              <option value="공무원">공무원</option>
              <option value="취업">취업</option>
              <option value="수능">수능</option>
              <option value="자격증">자격증</option>
              <option value="기타">기타</option>
            </select>
          </div>
          <Link to="/RoomCreate">
            <div>공개방 만들기</div>
          </Link>
          <div>필터</div>
          {/*아직 미구현예정 */}
        </div>
        <div className="rows">
          <Link to="/RoomDetail">
            <div className="Lines">
              <div className="">
                {/*미팅룸 이미지 내부에 기능들 표기됨*/}
                {rooms[roomNo]["roomImage"] === "none"
                  ? "(이미지없음)"
                  : rooms[roomNo]["roomImage"]}
                <div className="">
                  <p>⏱ 기능</p>✔ 뽀모도로<br></br>✔ 캠 + 화면공유<br></br>✔ 채팅
                  O<br></br>
                  <br></br>
                  <p>📝 규칙</p>✔ 휴대폰 인식<br></br>✔ 얼굴 인식<br></br>✔ 캠
                  켜기<br></br>
                </div>
              </div>
              <div className="">
                <div className="">
                  {/*태그*/}
                  {rooms[roomNo]["roomTag"]}
                </div>
                <div className="">
                  {/*이름*/}
                  {rooms[roomNo]["roomName"]}
                </div>
                <div className="">
                  {/*세부설명*/}
                  {rooms[roomNo]["roomDiscription"]}
                </div>
                <div className="">
                  {rooms[roomNo]["romMemberNo"]}/
                  {rooms[roomNo]["roomMemberMaxNo"]}
                </div>
              </div>
            </div>
          </Link>
          <Link to="/RoomDetail">
            <div className="Lines">
              <div className="">
                {/*미팅룸 이미지 내부에 기능들 표기됨*/}
                {rooms[roomNo]["roomImage"] === "none"
                  ? "(이미지없음)"
                  : rooms[roomNo]["roomImage"]}
                <div className="">
                  <p>⏱ 기능</p>✔ 뽀모도로<br></br>✔ 캠 + 화면공유<br></br>✔ 채팅
                  O<br></br>
                  <br></br>
                  <p>📝 규칙</p>✔ 휴대폰 인식<br></br>✔ 얼굴 인식<br></br>✔ 캠
                  켜기<br></br>
                </div>
              </div>
              <div className="">
                <div className="">
                  {/*태그*/}
                  {rooms[++roomNo]["roomTag"]}
                </div>
                <div className="">
                  {/*이름*/}
                  {rooms[roomNo]["roomName"]}
                </div>
                <div className="">
                  {/*세부설명*/}
                  {rooms[roomNo]["roomDiscription"]}
                </div>
                <div className="">
                  {rooms[roomNo]["romMemberNo"]}/
                  {rooms[roomNo]["roomMemberMaxNo"]}
                </div>
              </div>
            </div>
          </Link>
        </div>
        <div id="more">
          <button type="button" name="roomPageNo" onClick={addMore}>
            더보기
          </button>
        </div>
      </div>
    </form>
  );
}

export default RoomRecruit;
