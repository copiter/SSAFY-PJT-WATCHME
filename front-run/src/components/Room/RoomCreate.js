import React from "react";
import { useState, useContext, useRef } from "react";
import { Link, useNavigate } from "react-router-dom";
import { FetchUrl } from "../../store/communication";

import "./RoomCreate.css";

function RoomCreate() {
  //방생성 요청 보내기
  const [inputs, setInputs] = useState({
    roomName: "",
    status: "MODE1", //MODE1, MODE2, MODE3
    roomPwd: "",
    description: "",
    categoryName: "", //TAG1, TAG2, TAG3
    num: 0,
    roomPublic: "Private",
    endTime: "",
    display: 1,
  });
  const navigate = useNavigate();

  const handleChange = (event) => {
    const name = event.target.name;
    const value = event.target.value;
    setInputs((values) => ({ ...values, [name]: value }));
  };

  //URL
  const FETCH_URL = useContext(FetchUrl);
  const url = `${FETCH_URL}/addRoom`;
  //Otpion

  const imgeRef = useRef();

  const handleSubmit = (event) => {
    event.preventDefault();

    function getCookie(name) {
      const cookie = document.cookie
        .split(";")
        .map((cookie) => cookie.split("="))
        .filter((cookie) => cookie[0] === name);
      return cookie[0][1];
    }

    const formData = new FormData();
    formData.append("images", imgeRef.current.files[0]);
    formData.append(
      "postRoomReqDTO",
      new Blob([JSON.stringify(inputs)], { type: "application/json" })
    );

    console.log(url);
    fetch(url, {
      method: "POST",
      body: formData,
      headers: {
        accessToken: getCookie("accessToken"),
      },
    })
      .then((response) => {
        if (response.ok) {
          console.log("C1");
          console.log(response);
          return response.json(); //ok떨어지면 바로 종료.
        } else {
          response.json().then((data) => {
            console.log("ERR");
            let errorMessage = "";
            throw new Error(errorMessage);
          });
        }
      })
      .then((result) => {
        if (result != null) {
          console.log("방생성 완료");
          console.log(result);
          console.log("CK");
          console.log(result.responseData.roomId);
          navigate("/RoomDetail/:" + result.responseData.roomId);
          window.location.reload(); //리다이렉션관련
        }
      })
      .catch((err) => {
        console.log("ERR");
      });
  };

  const [fileImage, setFileImage] = useState("");
  const saveFileImage = (event) => {
    setFileImage(URL.createObjectURL(event.target.files[0]));
  };
  return (
    <div className="body-frame">
      <Link to="/RoomRecruit" className="back-to-recruit">
        &lt; 목록으로 돌아가기
      </Link>

      <form onSubmit={handleSubmit}>
        {/*form과 input의 name, type 수정시 연락부탁드립니다. 그외 구조나 id는 편하신대로 수정하셔도 됩니다. input추가시에는 말해주시면 감사하겠습니다.*/}
        <div className="form-frame">
          <div className="room-image">
            {fileImage && (
              <img
                alt="sample"
                src={fileImage}
                style={{
                  position: "absolute",
                  marginTop: "55px",
                  width: "150px",
                  height: "150px",
                  borderRadius: "50%",
                }}
              />
            )}
            {/*룸 이미지, 좌측부분 */}
            <input
              type="file"
              name="roomImage"
              accept="image/*"
              onChange={saveFileImage}
              className="room-image__upload"
              ref={imgeRef}
            />
            <div className="room-image__message">미팅룸 사진을 올려주세요</div>
          </div>
          <div className="room-infor">
            {/*우측부분*/}

            <div className="input-type">
              <div className="line">
                <input
                  type="text"
                  name="roomName"
                  value={inputs.roomName || ""}
                  onChange={handleChange}
                  placeholder="미팅룸 이름을 적으세요"
                />
              </div>
              <div className="line">
                <input
                  type="text"
                  name="description"
                  value={inputs.description || ""}
                  onChange={handleChange}
                  placeholder="간단한 설명을 적으세요"
                />
              </div>
              <div className="line">
                <input
                  type="number"
                  name="num"
                  value={inputs.num ? inputs.num : ""}
                  onChange={handleChange}
                  accept="number"
                  placeholder="인원수를 선택하세요(1~25)"
                />
                <select
                  name="categoryName"
                  value={inputs.categoryName || ""}
                  onChange={handleChange}
                >
                  <option value="" disabled>
                    카테고리를 선택하세요
                  </option>
                  <option value="공무원">공무원</option>
                  <option value="취업">취업</option>
                  <option value="수능">수능</option>
                  <option value="자격증">자격증</option>
                  <option value="기타">기타</option>
                </select>
              </div>
              <div className="line">
                <span>비공개</span>
                <label className="switch">
                  <input
                    type="checkbox"
                    name="roomPublic"
                    value={inputs.roomPublic || ""}
                    onChange={handleChange}
                  />
                  <span className="slider round"></span>
                </label>

                {/*checkbox이외의 방법으로 구현예정시 알려주세요.*/}
                <input
                  type="text"
                  name="roomPwd"
                  value={inputs.roomPwd || ""}
                  onChange={handleChange}
                  maxLength="4"
                  minLength="4"
                  placeholder="비밀번호 4자리"
                />
              </div>
            </div>

            <div className="input-rules">
              {/*규칙입니다. 현재 진행파트아닙니다. */}
              <div className="rules-title">📝 규칙</div>
              <div className="rules-box">
                {/*규칙 미정이라서 편하신대로 임시본으로 넣으시면됩니다.*/}
                <label>
                  <input type="checkbox" />
                  공부루틴 설정
                </label>
                <label>
                  <input type="checkbox" />
                  딴짓 감지
                </label>
                <label>
                  <input type="checkbox" />
                  얼굴인식
                </label>
                <label>
                  <input type="checkbox" />
                  화면공유
                </label>
              </div>
            </div>
            <button type="submit">생성하기</button>
          </div>
        </div>
      </form>
    </div>
  );
}

export default RoomCreate;
