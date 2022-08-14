import React from "react";
import { useState, useContext, useRef,useEffect } from "react";
import { useNavigate,Link } from "react-router-dom";
import { getCookie } from "../../../../Cookie";
import { FetchUrl } from "../../../../store/communication";
import "./RoomReform.css";
//outputs.status

function RoomReform() {
  //방생성 요청 보내기
  const [inputs, setInputs] = useState({
  });
  const navigate = useNavigate();

  const handleChange = (event) => {
    const name = event.target.name;
    const value = event.target.value;
    setInputs((values) => ({ ...values, [name]: value }));
  };

  //URL
  const FETCH_URL = useContext(FetchUrl);
  
  
  const id=window.location.pathname.split("/")[2].substring(0 );
  const url1 = `${FETCH_URL}/rooms/`+id+`/settings`;
  const url = `${FETCH_URL}/rooms/`+id+`/update`;
    //Otpion

  const imgeRef = useRef();


  console.log("INPUTS")
  console.log(inputs);



  useEffect(() => {
    console.log("REFORM START");
  fetch(url1, {
    headers: {
      accessToken: getCookie("accessToken"),
    },
  })
    .then((response) => {
      console.log(response);
      if (response.ok) {
        console.log("resOK");
        return response.json(); //ok떨어지면 바로 종료.
      } else {
        response.json().then((data) => {
          console.log("리스폰스오류");
          let errorMessage = "";
          throw new Error(errorMessage);
        });
      }
    })
    .then((result) => {
      if (result != null) {
        console.log("resultOK");
        console.log("result.responseData.room");
        setInputs(result.responseData.room);
        if(inputs.roomPwd===null||inputs.roomPwd===""){
          inputs.roomPwd=""}
          
        
        //navigate("/RoomDetail/:" + result.responseData.roomId);
        //window.location.reload(); //리다이렉션관련
      }
    })
    .catch((err) => {
      console.log("에러체크입니다.");
    });
  }, [])
  const handleSubmit = (event) => {
    event.preventDefault();

    const formData = new FormData();
    let outputs={
      roomName: inputs.roomName,
      mode: inputs.mode, //MODE1, MODE2, MODE3
      pwd: inputs.roomPwd,
      roomDescription:inputs.description,
      roomCategory: inputs.categoryName, 
      roomMemberMaxNo: inputs.num,
      endAt: inputs.endTime,
    };
    console.log("outputs");
    console.log(outputs);
    formData.append(
      "roomUpdateDTO",
      new Blob([JSON.stringify(outputs)], { type: "application/json" })
    );
    if(imgeRef!==null&&imgeRef!==""&&imgeRef.current.files[0]!==undefined){
      formData.append("images", imgeRef.current.files[0])};
    
    console.log("OUTPUTSHERE");
    console.log("KEY");
    for (let key of formData.keys()) {
      console.log(key);
    }
    console.log("values");
    for (let value of formData.values()) {
      console.log(value);
    }
    console.log("END");


    console.log(formData);
    fetch(url, {
      method: "POST",
      body: formData,
      headers: {
        accessToken: getCookie("accessToken"),
      },
    })
      .then((response) => {
        console.log(response);
        if (response.ok) {
          return response.json(); //ok떨어지면 바로 종료.
        } else {
          response.json().then((data) => {
            console.log("post 리스폰스오류입니다.");
            let errorMessage = "";
            throw new Error(errorMessage);
          });
        }
      })
      .then((result) => {
        if (result != null) {
          console.log("성공");
          //navigate("/RoomDetail/:" + result.responseData.roomId);
          navigate("./");
          //window.location.reload(); //리다이렉션관련
        }
      })
      .catch((err) => {
        console.log("에러 POST");
      });
  };

  const [fileImage, setFileImage] = useState();
  const saveFileImage = (event) => {
    setFileImage(URL.createObjectURL(event.target.files[0]));
  };
  return (
    <div className="body-frame">
      <form onSubmit={handleSubmit} className="floatRIGHT">
        {/*form과 input의 name, type 수정시 연락부탁드립니다. 그외 구조나 id는 편하신대로 수정하셔도 됩니다. input추가시에는 말해주시면 감사하겠습니다.*/}
        <div className="form-frame">
          <div className="room-image">
          <img
            alt="sample"
            src={fileImage?fileImage:inputs.img}
            style={{
              position: "absolute",
              marginTop: "55px",
              width: "150px",
              height: "150px",
              borderRadius: "50%",
              pointerEvents:"none"
            }}
            />
            {/*룸 이미지, 좌측부분 */}
            <input
              type="file"
              name="roomImage"
              accept="image/*"
              onChange={saveFileImage}
              className="room-image__upload"
              ref={imgeRef}
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
                <span>종료기간</span>
                    <input
                      type="datetime-local"
                      name="endTime"
                      value={inputs.endTime || ""}
                      onChange={handleChange}
                    />

                {/*checkbox이외의 방법으로 구현예정시 알려주세요.*/}
                <input
                  type="password"
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
                <label>
                  <input type="radio" value="MODE1"
                    checked={inputs.mode==="MODE1"?"checked":""}
                    onChange={handleChange}
                    name="mode"/>
                  감시없음
                </label>
                <label>
                  <input type="radio" value="MODE2"
                    checked={inputs.mode==="MODE2"?"checked":""}
                    onChange={handleChange}
                    name="mode"/>
                  졸음감지
                </label>
                <label>
                  <input type="radio" value="MODE3"
                    checked={inputs.mode==="MODE3"?"checked":""}
                    onChange={handleChange}
                    name="mode"/>
                  스마트폰감지
                </label>
                <label>
                  <input type="radio" value="MODE4"
                    checked={inputs.mode==="MODE4"?"checked":""}
                    onChange={handleChange}
                    name="mode"/>
                  화면공유
                </label>
              </div>
            </div>
            <button type="submit">수정하기</button>
          </div>
        </div>
      </form>
    </div>
  );
}

export default RoomReform;
