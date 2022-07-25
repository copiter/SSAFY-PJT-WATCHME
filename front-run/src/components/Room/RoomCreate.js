import React from "react";
import {useState} from 'react'
import { Link } from "react-router-dom";


import "./RoomCreate.css";

function CreateRoom() {

  const [inputs, setInputs] = useState({});

  const handleChange = (event) => {
    const name = event.target.name;
    const value = event.target.value;
    setInputs(values => ({...values, [name]: value}))
  }

  const handleSubmit = (event) => {
    event.preventDefault();
    alert(inputs);
  }
//https://www.w3schools.com/react/react_forms.asp


  return (
    <div id="bodyFraome">
      <Link to="/RoomRecruit"><div id="back">목록으로 돌아가기</div></Link>

      <form onSubmit={handleSubmit}>{/*form과 input의 name, type 수정시 연락부탁드립니다. 그외 구조나 id는 편하신대로 수정하셔도 됩니다. input추가시에는 말해주시면 감사하겠습니다.*/}
        
        <div id="room-image">{/*룸 이미지, 좌측부분 */}
          <input type='file' name='roomImage'
              value={inputs.roomImage || ""} onChange={handleChange}
              id="room-image__upload" 
              />
          <div id="room-image__message">미팅룸 사진을 올리세요</div>
        </div>
        <div id="room-infor">{/*우측부분*/}
          <div id="up">
            <input type="text" name='roomName' 
              value={inputs.roomName || ""} onChange={handleChange}
              placeholder="미팅룸 이름을 적으세요" />
            <input type="text" name='roomDiscription' 
              value={inputs.roomDiscription || ""} onChange={handleChange}
              placeholder="간단한 설명을 적으세요"  />
            <input tpye="number" name="roomMemMaxNoMex" 
              value={inputs.roomMemMaxNoMex || ""} onChange={handleChange}
              placeholder="인원수를 선택하세요(1~25)"  />
            <input type="checkbox" name="roomPublic" 
              value={inputs.roomPublic || ""} onChange={handleChange}
              />{/*checkbox이외의 방법으로 구현예정시 알려주세요.*/}
            <input type="text" name="roomPassword" 
              value={inputs.roomPassword || ""} onChange={handleChange}
              placeholder="비밀번호 4자리를 적으세요"  />
          </div>
          
          <div id="center">{/*규칙입니다. 현재 진행파트아닙니다. */}
            <div>📝 규칙</div>
            <div>{/*규칙 미정이라서 편하신대로 임시본으로 넣으시면됩니다.*/}</div>
          </div>
          <div id="button">
            <input type="submit"/>
          </div>
        </div>
      </form>
    </div>
  );
}

export default CreateRoom;
