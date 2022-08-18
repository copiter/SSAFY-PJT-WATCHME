import React from "react";
import { useContext, useState, useEffect } from "react";
import { FetchUrl } from "../../../store/communication";
import { getCookie } from "../../../Cookie";

import "./PointAdd.css";
import swal from "sweetalert";
function PointAdd() {
  const FETCH_URL = useContext(FetchUrl);
  const inquireUrl = `${FETCH_URL}/members/points`;
  const chargeUrl = `${FETCH_URL}/points/kakao?`;
  const refundUrl = `${FETCH_URL}/points/return?`;
  const [reload, setReload] = useState(false); //환급 받고 리로딩
  const [inputs, setInputs] = useState({
    valueSelect: "1000",
    valueInputs: "",
  });
  const [inputsRefund, setInputsRefund] = useState({
    valueSelect: "1000",
    valueInputs: "",
  });

  const [pointInfo, setPointInfo] = useState({
    sumPoint: 0,
    chargePoint: 0,
    getPoint: 0,
    losePoint: 0,
    pointList: [],
  });

  useEffect(() => {
    fetch(inquireUrl, {
      method: "GET",
      headers: {
        accessToken: getCookie("accessToken"),
      },
    })
      .then((response) => response.json())
      .then((result) => {
        if (result.code === 200) {
          let tmpData = result.responseData;
          tmpData.pointList.reverse();
          setPointInfo(tmpData);
        }
      })
      .catch((err) => console.log(err));
  }, [reload]);
  // console.log(pointInfo);

  const handleSubmitCharge = (event) => {
    event.preventDefault();
    fetch(
      chargeUrl +
        "value=" +
        (inputs.valueSelect === "Free"
          ? inputs.valueInputs
          : inputs.valueSelect),
      {
        method: "POST",
        headers: {
          accessToken: getCookie("accessToken"),
        },
      }
    )
      .then((response) => {
        console.log(response);
        if (response.ok) {
          return response.json(); //ok떨어지면 바로 종료.
        } else {
          response.json().then((data) => {
            let errorMessage = "";
            throw new Error(errorMessage);
          });
        }
      })
      .then((result) => {
        if (result != null) {
          window.location.href = result.responseData.REDIRECT_URL;
          localStorage.setItem("tid", result.responseData.tid);
        }
      })
      .catch((err) => {
        console.log("ERR");
      });
  };
  const handleSubmitRefund = (event) => {
    event.preventDefault();
    fetch(
      refundUrl +
        "value=" +
        (inputsRefund.valueSelect === "Free"
          ? inputsRefund.valueInputs
          : inputsRefund.valueSelect),
      {
        method: "POST",
        headers: {
          accessToken: getCookie("accessToken"),
        },
      }
    )
      .then((response) => response.json())
      .then((result) => {
        if (result.code === 200) {
          swal(
            "환급되었습니다!",
            `환급액 ${
              inputsRefund.valueSelect === "Free"
                ? inputsRefund.valueInputs
                : inputsRefund.valueSelect
            }`,
            "success"
          );
          setReload(!reload);
        }
      })
      .catch((err) => {
        console.log("ERR");
      });
  };

  function sendApproval() {}

  //충전 금액 설정
  const [isShown, setIsShown] = useState(false);
  const handleChange = (event) => {
    const name = event.target.name;
    const value = event.target.value;
    setInputs((values) => ({ ...values, [name]: value }));
    if (value === "Free" && name === "valueSelect") {
      //valueInputs비허용
      setIsShown(true);
    } else if (value !== "Free" && name === "valueSelect") {
      setIsShown(false);
    }
  };

  //환급금액 설정
  const [isShownRefund, setIsShownRefund] = useState(false);
  const handleChangeRefund = (event) => {
    const name = event.target.name;
    const value = event.target.value;
    setInputsRefund((values) => ({ ...values, [name]: value }));
    if (value === "Free" && name === "valueSelect") {
      //valueInputs비허용
      setIsShownRefund(true);
    } else if (value !== "Free" && name === "valueSelect") {
      setIsShownRefund(false);
    }
  };

  //버틀 클릭 처리
  const [currentCase, setCurrentCase] = useState(0);
  function handleCurrentCase(e) {
    setCurrentCase(e.target.value);
  }

  return (
    <div id="pointadd">
      <h1 id="pointadd-title">Point</h1>
      <div id="pointadd-content">
        <div className="point-my-point">
          <div className="point-my-pointdes">
            <ul className="point-my-pointdet">
              <li>
                <span>💰 내가 보유한 포인트</span>
                <span>{pointInfo.sumPoint} 💎</span>
              </li>
              <li>
                <span className="point-mypoint-sub">내가 충전한 포인트</span>
                <span>{pointInfo.chargePoint} 💎</span>
              </li>
              <li>
                <span className="point-mypoint-sub">내가 환급한 포인트</span>
                <span>{pointInfo.refundPoint} 💎</span>
                {/* <span>{pointInfo.chargePoint} 💎</span> */}
              </li>
              <li>
                <span className="point-mypoint-sub">내가 획득한 포인트</span>
                <span>{pointInfo.getPoint} 💎</span>
              </li>
              <li>
                <span className="point-mypoint-sub">내가 잃은 포인트</span>
                <span>{pointInfo.losePoint} 💎</span>
              </li>
            </ul>
            <div className="point-watchme">
              <span id="point-watchme-title">&lt;WatchMe Point&gt;는요..</span>
              <p id="point-watchme-text">
                WatchMe 서비스의 유료 서비스를 이용하기 위해 필요한 결제
                수단입니다. 휴대폰, 신용카드, 계좌이체 등 다양한 방법으로
                WatchMe Point를 충전하여 이용하실 수 있습니다.
              </p>
            </div>
          </div>
          <div className="point-log">
            <div className="point-list">🧾 포인트내역</div>

            <table id="point-table-header">
              <colgroup>
                <col width="40%" />
                <col width="30%" />
                <col width="30%" />
              </colgroup>
              <thead>
                <tr>
                  <td>일자</td>
                  <td>내용</td>
                  <td>포인트내용</td>
                </tr>
              </thead>
            </table>
            <div id="point-table">
              <table>
                <colgroup>
                  <col width="40%" />
                  <col width="30%" />
                  <col width="30%" />
                </colgroup>
                <tbody>
                  {pointInfo.pointList.length > 0 &&
                    pointInfo.pointList.map((point, index) => {
                      return (
                        <tr key={index}>
                          <td>{point.date}</td>
                          <td>{point.content}</td>
                          <td>{point.point}</td>
                        </tr>
                      );
                    })}
                </tbody>
              </table>
            </div>
          </div>
        </div>
        <div id="charge-form-frame">
          <div id="charge-cash">
            <div className="point-cash-title">캐쉬충전</div>
            <div className="selection-title">💳 결제수단을 선택하세요</div>
            <ul id="pay-selection">
              <li value={0} onClick={handleCurrentCase}>
                <input
                  type="button"
                  value="카카오페이"
                  className={currentCase === "카카오페이" ? "active" : null}
                />
              </li>
              <li value={1} onClick={handleCurrentCase}>
                <input
                  type="button"
                  value="신용카드"
                  className={currentCase === "신용카드" ? "active" : null}
                />
              </li>
              <li value={2} onClick={handleCurrentCase}>
                <input
                  type="button"
                  value="휴대폰"
                  className={currentCase === "휴대폰" ? "active" : null}
                />
              </li>
              <li value={3} onClick={handleCurrentCase}>
                <input
                  type="button"
                  value="계좌이체"
                  className={currentCase === "계좌이체" ? "active" : null}
                />
              </li>
              <li value={4} onClick={handleCurrentCase}>
                <input
                  type="button"
                  value="네이버페이"
                  className={currentCase === "네이버페이" ? "active" : null}
                />
              </li>
              <li value={5} onClick={handleCurrentCase}>
                <input
                  type="button"
                  value="기타"
                  className={currentCase === "기타" ? "active" : null}
                />
              </li>
            </ul>
            <form onSubmit={handleSubmitCharge} id="charge-input">
              <span>충전할 금액을 선택하세요(원)</span>
              <div id="charge-select">
                <select onChange={handleChange} name="valueSelect">
                  <option value="1000">1,000</option>
                  <option value="5000">5,000</option>
                  <option value="10000">10,000</option>
                  <option value="20000">20,000</option>
                  <option value="50000">50,000</option>
                  <option value="100000">100,000</option>
                  <option value="Free">직접입력</option>
                </select>
                {isShown && (
                  <input
                    type="number"
                    className="dirInput"
                    name="valueInputs"
                    defaultValue="1000"
                    min="1000"
                    step="1000"
                    placeholder="금액을 입력하세요"
                    onChange={handleChangeRefund}
                  />
                )}

                <button type="submit" id="charge-submit-btn">
                  충전
                </button>
              </div>
            </form>
          </div>
          <div id="refund-cash">
            <div className="point-cash-title">캐쉬환급</div>
            <form onSubmit={handleSubmitRefund} id="refund-input">
              <span>환급받을 금액을 선택하세요(원)</span>
              <div id="charge-select">
                <select onChange={handleChangeRefund} name="valueSelect">
                  <option value="1000">1,000</option>
                  <option value="5000">5,000</option>
                  <option value="10000">10,000</option>
                  <option value="20000">20,000</option>
                  <option value="50000">50,000</option>
                  <option value="100000">100,000</option>
                  <option value="Free">직접입력</option>
                </select>
                {isShownRefund && (
                  <input
                    type="number"
                    className="dirInput"
                    name="valueInputs"
                    defaultValue="1000"
                    min="1000"
                    step="1000"
                    placeholder="금액을 입력하세요"
                    onChange={handleChangeRefund}
                  />
                )}

                <button type="submit" id="charge-submit-btn">
                  환급
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}

export default PointAdd;
