import React from "react";
import { useState, useContext, useRef } from "react";
import { Link, useNavigate } from "react-router-dom";
import { FetchUrl } from "../../store/communication";
import getCookie from "../../Cookie";

import "./GroupCreate.css";

function GroupCreate() {
  const handleChangeSelect = (event) => {
    const value = event.target.value;

    if (value === "공무원") {
      inputs.ctg[0] = !inputs.ctg[0];
    } else if (value === "취업") {
      inputs.ctg[1] = !inputs.ctg[1];
    } else if (value === "수능") {
      inputs.ctg[2] = !inputs.ctg[2];
    } else if (value === "기타") {
      inputs.ctg[3] = !inputs.ctg[3];
    }
    // console.log(inputs.ctg);
  };

  const [inputs, setInputs] = useState({
    name: "",
    description: "",
    maxMember: 0,
    ctg: [false, false, false, false],
    display: 1,
    pwd: "",
  });
  const navigate = useNavigate();

  const handleChange = (event) => {
    const name = event.target.name;
    const value = event.target.value;
    setInputs((values) => ({ ...values, [name]: value }));
  };

  const [isChecked, setIsChecked] = useState(false);
  const handleChangeCheck = (event) => {
    setIsChecked((current) => !current);
    const name = event.target.name;
    setInputs((values) => ({ ...values, [name]: isChecked ? 1 : 0 }));
  };

  //URL
  const FETCH_URL = useContext(FetchUrl);
  const url = `${FETCH_URL}/groups`;
  //Otpion

  const imgeRef = useRef();

  const handleSubmit = (event) => {
    event.preventDefault();

    const formData = new FormData();
    formData.append("images", imgeRef.current.files[0]);
    let ctgs = [];
    let i = 0;
    let j = 0;
    for (i = 0; i < 4; i++) {
      if (inputs.ctg[i]) {
        ctgs[j] =
          i === 0 ? "공무원" : i === 1 ? "취업" : i === 2 ? "수능" : "기타";
        j++;
      }
    }
    //inputs.cgs
    const outputs = {
      name: inputs.name,
      description: inputs.description,
      maxMember: inputs.maxMember,
      ctg: ctgs,
      display: inputs.display,
      pwd: inputs.pwd,
    };
    formData.append(
      "postGroupReqDTO",
      new Blob([JSON.stringify(outputs)], { type: "application/json" })
    );
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
          console.log(result["responseData"]["groupId"]);
          navigate("/GroupDetail/" + result["responseData"]["groupId"]);
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
    <div id="group-create">
      <Link to="/GroupRecruit" className="back-to-recruit">
        &lt; 목록으로 돌아가기
      </Link>
      <form onSubmit={handleSubmit}>
        <div className="form-frame">
          <div className="group-image">
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
            <div className="group-image__message">그룹 사진을 올리세요</div>
          </div>
          <div className="group-infor">
            {/*우측부분*/}
            <div className="group-type">
              <div className="line">
                <input
                  type="text"
                  name="name"
                  value={inputs.name || ""}
                  onChange={handleChange}
                  placeholder="그룹 이름을 적으세요"
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
              <div id="group-twin">
                <div className="line">
                  <input
                    type="number"
                    name="maxMember"
                    value={inputs.maxMember ? inputs.maxMember : ""}
                    onChange={handleChange}
                    accept="number"
                    placeholder="인원수를 선택하세요(1~25)"
                  />
                </div>
                <div className="line">
                  <span>비공개</span>
                  <label className="switch" >
                    <input
                      type="checkbox"
                      name="display"
                      value={isChecked}
                      onChange={handleChangeCheck}
                    />
                    <span className="slider round"></span>
                  </label>
                  <input
                    type="password"
                    name="pwd"
                    value={inputs.pwd || ""}
                    onChange={handleChange}
                    disabled={!isChecked}
                    maxLength="4"
                    minLength="4"
                    placeholder={!isChecked?"공개방입니다":"비밀번호 4자리"}
                  />
                </div>
              </div>
            </div>
            <div className="input-rules">
              <div className="rules-title" name="ctg">
                카테고리
              </div>
              <div className="rules-box">
                <label>
                  <input
                    type="checkbox"
                    onChange={handleChangeSelect}
                    value="공무원"
                  />
                  공무원
                </label>
                <label>
                  <input
                    type="checkbox"
                    onChange={handleChangeSelect}
                    value="취업"
                  />
                  취업
                </label>
                <label>
                  <input
                    type="checkbox"
                    onChange={handleChangeSelect}
                    value="수능"
                  />
                  수능
                </label>
                <label>
                  <input
                    type="checkbox"
                    onChange={handleChangeSelect}
                    value="기타"
                  />
                  기타
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

export default GroupCreate;
