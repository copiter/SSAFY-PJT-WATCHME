import React, { useEffect } from "react";
import { useState, useContext, useRef } from "react";
import { Link, useNavigate } from "react-router-dom";
import { FetchUrl } from "../../store/communication";
import { getCookie } from "../../Cookie";

import "./GroupReform.css";
import ErrorCode from "../../Error/ErrorCode";
import swal from "sweetalert";

function GroupReform() {
  const [inputs, setInputs] = useState({
    ctg: [false, false, false, false],
    secret: 0,
  });
  const navigate = useNavigate();

  const nameInputRef = useRef();
  const descriptionInputRef = useRef();
  const maxMemberInputRef = useRef();

  //URL
  const FETCH_URL = useContext(FetchUrl);
  const url = `${FETCH_URL}/groups`;

  //groupId 구하기
  const pathnameArr = window.location.pathname.split("/");
  const groupId = +pathnameArr[pathnameArr.length - 1];

  const [zeroBox, setZeroBox] = useState(false);
  const [firstBox, setFirstBox] = useState(false);
  const [secondBox, setSecondBox] = useState(false);
  const [thirdBox, setThirdBox] = useState(false);
  const [fourthBox, setFourthBox] = useState(false);

  useEffect(() => {
    fetch(`${url}/${groupId}/update-form`, {
      method: "GET",
      headers: {
        accessToken: getCookie("accessToken"),
      },
    })
      .then((response) => response.json())
      .then((result) => {
        if (result.code === 200) {
          const group = result.responseData.group;
          nameInputRef.current.value = group.name;
          descriptionInputRef.current.value = group.description;
          maxMemberInputRef.current.value = group.maxMember;
          setFileImage(group.imgLink);
          for (let item of group.ctg) {
            if (item === "공무원") {
              setZeroBox(true);
            } else if (item === "취업") {
              setFirstBox(true);
            } else if (item === "수능") {
              setSecondBox(true);
            } else if (item === "코딩") {
              setThirdBox(true);
            } else if (item === "기타") {
              setFourthBox(true);
            }
          }
        } else {
          ErrorCode(result);
        }
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);

  const handleChangeSelect = (event) => {
    const value = event.target.value;

    if (value === "공무원") {
      inputs.ctg[0] = !inputs.ctg[0];
      setZeroBox(!zeroBox);
    } else if (value === "취업") {
      inputs.ctg[1] = !inputs.ctg[1];
      setFirstBox(!firstBox);
    } else if (value === "수능") {
      inputs.ctg[2] = !inputs.ctg[2];
      setSecondBox(!secondBox);
    } else if (value === "코딩") {
      inputs.ctg[3] = !inputs.ctg[3];
      setThirdBox(!thirdBox);
    } else if (value === "기타") {
      inputs.ctg[4] = !inputs.ctg[4];
      setFourthBox(!fourthBox);
    }
  };

  const [isChecked, setIsChecked] = useState(true);
  const handleChangeCheck = (event) => {
    setIsChecked((current) => !current);
    console.log(isChecked);
    const name = event.target.name;
    setInputs((values) => ({ ...values, [name]: isChecked ? 1 : 0 }));
  };

  const imgeRef = useRef();
  const [fileImage, setFileImage] = useState("");
  const saveFileImage = (event) => {
    // console.log(event.target.files[0]);
    setFileImage(URL.createObjectURL(event.target.files[0]));
  };

  // 수정
  const handleSubmit = (event) => {
    event.preventDefault();

    let ctgs = [];
    let i = 0;
    let j = 0;
    for (i = 0; i < 5; i++) {
      if (inputs.ctg[i]) {
        ctgs[j] =
          i === 0
            ? "공무원"
            : i === 1
            ? "취업"
            : i === 2
            ? "수능"
            : i === 3
            ? "코딩"
            : "기타";
        j++;
      }
    }
    //inputs.cgs
    const outputs = {
      name: nameInputRef.current.value,
      description: descriptionInputRef.current.value,
      maxMember: maxMemberInputRef.current.value,
      ctg: ctgs,
      secret: isChecked ? 0 : 1,
    };
    // console.log(outputs);

    const formData = new FormData();
    formData.append("images", imgeRef.current.files[0]);
    formData.append(
      "groupUpdateReqDTO",
      new Blob([JSON.stringify(outputs)], { type: "application/json" })
    );

    fetch(`${url}/${groupId}/update`, {
      method: "POST",
      body: formData,
      headers: {
        accessToken: getCookie("accessToken"),
      },
    })
      .then((response) => response.json())
      .then((result) => {
        if (result.code === 200) {
          swal("그룹이 수정되었습니다!", "", "success");
          navigate(`/GroupDetail/${groupId}`);
        } else {
          ErrorCode(result);
        }
      })
      .catch((err) => {
        console.log("ERR");
      });
  };

  return (
    <div id="group-create">
      <Link to={`/GroupDetail/${groupId}`} className="back-to-recruit">
        &lt; 목록으로 돌아가기
      </Link>
      <form onSubmit={handleSubmit}>
        <div className="form-frame">
          <div className="group-image-reform">
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
              className="group-image__upload-reform"
              ref={imgeRef}
            />
            <div className="group-image__message">그룹 사진을 올리세요</div>
          </div>
          <div className="group-infor">
            {/*우측부분*/}
            <div className="group-type">
              <div className="line">
                <label>
                  그룹이름
                  <input
                    type="text"
                    name="name"
                    required
                    placeholder="그룹 이름을 적으세요"
                    ref={nameInputRef}
                  />
                </label>
              </div>
              <div className="line">
                <label>
                  그룹설명
                  <input
                    type="text"
                    name="description"
                    required
                    placeholder="간단한 설명을 적으세요"
                    ref={descriptionInputRef}
                  />
                </label>
              </div>
              <div id="group-twin">
                <div className="line">
                  <label>
                    최대인원
                    <input
                      type="number"
                      name="maxMember"
                      accept="number"
                      min="1"
                      max="25"
                      required
                      placeholder="인원수를 선택하세요(1~25)"
                      ref={maxMemberInputRef}
                    />
                  </label>
                </div>
                <div className="line">
                  <span>비공개</span>
                  <label className="switch">
                    <input
                      type="checkbox"
                      name="secret"
                      value={isChecked}
                      onChange={handleChangeCheck}
                    />
                    <span className="slider round"></span>
                  </label>
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
                    checked={zeroBox}
                  />
                  공무원
                </label>
                <label>
                  <input
                    type="checkbox"
                    onChange={handleChangeSelect}
                    value="취업"
                    checked={firstBox}
                  />
                  취업
                </label>
                <label>
                  <input
                    type="checkbox"
                    onChange={handleChangeSelect}
                    value="수능"
                    checked={secondBox}
                  />
                  수능
                </label>
                <label>
                  <input
                    type="checkbox"
                    onChange={handleChangeSelect}
                    value="코딩"
                    checked={thirdBox}
                  />
                  코딩
                </label>
                <label>
                  <input
                    type="checkbox"
                    onChange={handleChangeSelect}
                    value="기타"
                    checked={fourthBox}
                  />
                  기타
                </label>
              </div>
            </div>
            <button type="submit" onSubmit={handleSubmit}>
              수정하기
            </button>
          </div>
        </div>
      </form>
    </div>
  );
}

export default GroupReform;
