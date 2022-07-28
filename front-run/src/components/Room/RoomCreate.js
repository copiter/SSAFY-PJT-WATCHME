import React from "react";
import { useState,useContext } from "react";
import { Link,useNavigate  } from "react-router-dom";
import { FetchUrl } from "../../store/communication";

import "./RoomCreate.css";

function CreateRoom() {
  
  //방생성 요청 보내기
  const [inputs, setInputs] = useState({
    "roomImage":"",
    "roomName":"",
    "roomDiscription":"",
    "roomMemMaxNoMex":"",
    "roomPublic":"Private",
    "roomCategory":"공무원",
    "roomPassword":"",
    "roomTags":""
  });
  const navigate=useNavigate()


  const handleChange = (event) => {
    const name = event.target.name;
    const value = event.target.value;
    setInputs((values) => ({ ...values, [name]: value }));
  };


  //URL
  const FETCH_URL = useContext(FetchUrl);
  const url = `${FETCH_URL}/MainPage`;
  //Otpion
  const requestOptions ={
    method: "POST", 
    headers: {"content-type": "application/json",},
    body: JSON.stringify(inputs),
  } 
  const handleSubmit = (event) => {
    event.preventDefault();
    console.log(requestOptions);
    fetch(url, requestOptions)
    .then(response => response.json())//보내기 문제없으면 넘어감
    .then((response) => {
      if (response.ok) {return response.json();//ok떨어지면 바로 종료.
      } else {
        response.json().then((data) => { 
          let errorMessage = "";
          throw new Error(errorMessage);
        });
      }
    })
    .then((result) => {
      if (result != null) {
        console.log("방생성 완료")
        navigate("/");
        window.location.reload();//리다이렉션관련
      }
    })
    .catch((err) => {
      console.log(err.message);
    });

  }
  return (
    <div className="body-frame">
      <Link to="/RoomRecruit" className="back-to-recruit">
        &lt; 목록으로 돌아가기
      </Link>

      <form onSubmit={handleSubmit}>
        {/*form과 input의 name, type 수정시 연락부탁드립니다. 그외 구조나 id는 편하신대로 수정하셔도 됩니다. input추가시에는 말해주시면 감사하겠습니다.*/}
        <div className="form-frame">
          <div className="room-image">
            {/*룸 이미지, 좌측부분 */}
            <input
              type="file"
              name="roomImage"
              value={inputs.roomImage || ""}
              onChange={handleChange}
              className="room-image__upload"
            />
            <div className="room-image__message">미팅룸 사진을 올리세요</div>
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
                  name="roomDiscription"
                  value={inputs.roomDiscription || ""}
                  onChange={handleChange}
                  placeholder="간단한 설명을 적으세요"
                />
              </div>
              <div className="line">
                <input
                  type="number"
                  name="roomMemMaxNoMex"
                  value={inputs.roomMemMaxNoMex || ""}
                  onChange={handleChange}
                  placeholder="인원수를 선택하세요(1~25)"
                />
                <select
                  name="roomTags"
                  value={inputs.roomTags || ""}
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
                  name="roomPassword"
                  value={inputs.roomPassword || ""}
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

export default CreateRoom;
