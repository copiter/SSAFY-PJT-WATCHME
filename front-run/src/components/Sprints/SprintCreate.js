import React, { useRef, useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import { FetchUrl } from "../../store/communication";
import { getCookie } from "../../Cookie";
import GetToday from "../ETC/GetToday";

import "./SprintCreate.css";
import swal from "sweetalert";
import ErrorCode from "../../Error/ErrorCode";

function SprintCreate() {
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

  // 파일처리
  const [fileImage, setFileImage] = useState("");
  const saveFileImage = (event) => {
    setFileImage(URL.createObjectURL(event.target.files[0]));
  };

  function dateCheck() {
    if (
      startAtInputRef.current.value === "" ||
      endAtInputRef.current.value === ""
    ) {
      return;
    }
    if (startAtInputRef.current.value >= endAtInputRef.current.value) {
      swal("시작일자는 종료일자보다 빨라야 합니다", "", "error");
      endAtInputRef.current.value = "";
    }
  }

  function timeCheck() {
    if (
      routineStartAtInputRef.current.value === "" ||
      routineEndAtInputRef.current.value === ""
    ) {
      return;
    }
    if (
      routineStartAtInputRef.current.value ===
      routineEndAtInputRef.current.value
    ) {
      swal("루틴 시작과 종료는 달라야 합니다", "", "error");
      routineEndAtInputRef.current.value = "";
    }
  }

  //submit
  const handleSubmit = (event) => {
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
    // debugger;
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
        if (result.code === 200) {
          swal("방이 성공적으로 생성되었습니다", "", "success");
          navigate(`/GroupDetail/${groupId}`);
        } else {
          ErrorCode(result);
        }
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div id="sprint-create-whole">
      <strong id="sprint-create-title">스프린트 생성</strong>
      <div id="sprint-create">
        <div id="sprint-image">
          <input
            type="file"
            name="img"
            accept="image/*"
            onChange={saveFileImage}
            id="sprint-image__upload"
            ref={imgeRef}
          />
          {fileImage && (
            <img id="sprint-image-loaded" alt="sample" src={fileImage} />
          )}
          <span id="sprint-image__message">스프린트 사진을 올려주세요</span>
        </div>

        {/*우측부분*/}
        <div id="sprint-input">
          <div id="sprint-input-items">
            <input
              id="sprint-name"
              type="text"
              required
              ref={nameInputRef}
              placeholder="스프린트 이름을 적으세요"
            />
            <input
              type="text"
              required
              ref={descriptionInputRef}
              placeholder="스프린트 설명을 적으세요"
            />
            <input
              type="text"
              required
              ref={goalInputRef}
              placeholder="스프린트 목표를 적으세요"
            />

            <div id="label-container">
              <label>
                시작일을 선택하세요
                <input
                  type="date"
                  required
                  min={GetToday(1)}
                  max="9999-12-31"
                  ref={startAtInputRef}
                  onChange={dateCheck}
                />
              </label>
              <label>
                종료일을 선택하세요
                <input
                  type="date"
                  required
                  min={
                    startAtInputRef.current
                      ? startAtInputRef.current.value
                      : GetToday(1)
                  }
                  max="9999-12-31"
                  ref={endAtInputRef}
                  onChange={dateCheck}
                />
              </label>

              <label>
                루틴 시작시각을 선택하세요
                <input
                  type="time"
                  required
                  defaultValue="09:00"
                  ref={routineStartAtInputRef}
                  onChange={timeCheck}
                  step="600"
                />
              </label>
              <label>
                루틴 종료시각을 선택하세요
                <input
                  type="time"
                  required
                  defaultValue="18:00"
                  ref={routineEndAtInputRef}
                  onChange={timeCheck}
                  step="600"
                />
              </label>

              <label>
                규칙선택
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
                  <option value="MODE4">자리이탈 감지</option>
                </select>
              </label>
            </div>

            <div id="sprint-fee">
              <input
                type="number"
                step="1000"
                disabled={selectMode === "MODE1" ? true : false}
                ref={feeInputRef}
                placeholder="참가비를 입력하세요(원)"
              />
              <input
                type="number"
                step="100"
                max={
                  feeInputRef.current === undefined
                    ? 0
                    : feeInputRef.current.value
                }
                disabled={selectMode === "MODE1" ? true : false}
                ref={penaltyMoneyInputRef}
                placeholder="벌금 단위를 입력하세요(원)"
              />
            </div>
          </div>
          <button type="button" onClick={handleSubmit}>
            생성하기
          </button>
        </div>
      </div>
    </div>
  );
}

export default SprintCreate;
