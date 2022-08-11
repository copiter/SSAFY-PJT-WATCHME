import React, { useRef, useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import { FetchUrl } from "../../store/communication";

import "./SprintCreate.css";

import getCookie from "../../Cookie";

function SprintCreate(props) {
  const navigate = useNavigate();

  //url
  const FETCH_URL = useContext(FetchUrl);
  const pathnameArr = window.location.pathname.split("/");
  const groupId = +pathnameArr[pathnameArr.length - 1];
  const url = `${FETCH_URL}/sprints/${groupId}`;

  const [selectMode, setSelectMode] = useState("MODE1");
  const handleMode = (e) => {
    setSelectMode(e.target.value);
  };

  const nameInputRef = useRef();
  const descriptionInputRef = useRef();
  const goalInputRef = useRef();
  const startAtInputRef = useRef();
  const endAtInputRef = useRef();
  const routineStartAtInputRef = useRef();
  const routineEndAtInputRef = useRef();
  const feeInputRef = useRef();
  const penaltyMoneyInputRef = useRef();
  const modeInputRef = useRef();
  const imgeRef = useRef();

  //오늘날짜
  const day = new Date();
  const today = `${day.getFullYear()}-${day.getMonth() + 1}-${day.getDate()}`;

  // 파일처리
  const [fileImage, setFileImage] = useState("");
  const saveFileImage = (event) => {
    setFileImage(URL.createObjectURL(event.target.files[0]));
  };

  //submit
  const handleSubmit = (event) => {
    event.preventDefault();

    const data = {
      name: nameInputRef.current.value,
      description: descriptionInputRef.current.value,
      goal: goalInputRef.current.value,
      startAt: startAtInputRef.current.value,
      endAt: endAtInputRef.current.value,
      routineStartAt: routineStartAtInputRef.current.value,
      routineEndAt: routineEndAtInputRef.current.value,
      fee: feeInputRef.current === undefined ? 0 : feeInputRef.current.value,
      penaltyMoney: penaltyMoneyInputRef.current.value,
      mode: modeInputRef.current.value,
    };

    const formData = new FormData();
    formData.append("images", imgeRef.current.files[0]);
    formData.append(
      "sprintPostDTO",
      new Blob([JSON.stringify(data)], { type: "application/json" })
    );

    fetch(url, {
      method: "POST",
      body: formData,
      headers: {
        accessToken: getCookie("accessToken"),
      },
    })
      .then((response) => {
        return response.json();
      })
      .then((result) => {
        console.log(result);
        alert("방이 성공적으로 생성되었습니다");
        navigate(`/GroupDetail/${props.groupId}`);
      })
      .then((response) => {})
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div className="body-frame">
      <form onSubmit={handleSubmit}>
        <div className="form-frame">
          <div className="sprint-image">
            {fileImage && (
              <img
                /*이미지 띄워지는곳 */
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
            <input
              type="file"
              name="img"
              accept="image/*"
              onChange={saveFileImage}
              className="group-image__upload"
              ref={imgeRef}
            />
            <div className="group-image__message">스프린트 사진을 올리세요</div>
          </div>
          <div className="sprint-infor">
            {/*우측부분*/}

            <div className="input-type">
              <div className="line">
                <input
                  type="text"
                  required
                  ref={nameInputRef}
                  placeholder="스프린트 이름을 적으세요"
                />
              </div>
              <div className="line">
                <input
                  type="text"
                  required
                  ref={descriptionInputRef}
                  placeholder="스프린트 설명을 적으세요"
                />
              </div>
              <div className="line">
                <input
                  type="text"
                  required
                  ref={goalInputRef}
                  placeholder="스프린트 목표를 적으세요"
                />
              </div>
              <div className="line">
                <input
                  type="number"
                  step="1000"
                  ref={feeInputRef}
                  placeholder="참가비를 입력하세요"
                />
                <input
                  type="number"
                  step="100"
                  max={
                    feeInputRef.current === undefined
                      ? 0
                      : feeInputRef.current.value
                  }
                  ref={penaltyMoneyInputRef}
                  placeholder="벌금 단위를 입력하세요"
                />
              </div>
              <div className="line">
                <label for="startAt" value="hi">
                  <input
                    name="startAt"
                    type="date"
                    placeholder="시작일을 선택하세요"
                    required
                    min={today}
                    ref={startAtInputRef}
                  />
                </label>
                <input
                  type="date"
                  placeholder="종료일을 선택하세요"
                  required
                  min={today}
                  ref={endAtInputRef}
                />
              </div>
              <div className="line">
                <input
                  type="time"
                  placeholder="루틴 시작시각을 선택하세요"
                  required
                  ref={routineStartAtInputRef}
                />
                <input
                  type="time"
                  placeholder="루틴 종료시각을 선택하세요"
                  required
                  ref={routineEndAtInputRef}
                />
              </div>
              <div className="line">
                <select
                  name="mode"
                  placeholder="규칙을 고르세요"
                  onChange={handleMode}
                  value={selectMode}
                  required
                  ref={modeInputRef}
                >
                  <option value="MODE1">규칙없음</option>
                  <option value="MODE2">졸림 감지</option>
                  <option value="MODE3">스마트폰 감시</option>
                  <option value="MODE4">화면공유 필수</option>
                </select>
              </div>
            </div>
          </div>
          <div>
            <button type="submit">생성하기</button>
          </div>
        </div>
      </form>
    </div>
  );
}

export default SprintCreate;
