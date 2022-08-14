import React from "react";
import { useContext, useState } from "react";
import { FetchUrl } from "../../../store/communication";

import "./PointAdd.css";
function PointAdd() {
  const FETCH_URL = useContext(FetchUrl);
  const url = `${FETCH_URL}/points/kakao?`;
  const [inputs, setInputs] = useState({
    valueSelect: "1000",
    valueInputs: "",
  });
  function getCookie(name) {
    const cookie = document.cookie
      .split(";")
      .map((cookie) => cookie.split("="))
      .filter((cookie) => cookie[0] === name);
    return cookie[0][1];
  }

  const handleSubmit = (event) => {
    event.preventDefault();
    console.log(
      url + "value=" + (inputs.valueSelect === "Free" ? inputs.valueInputs : inputs.valueSelect)
    );
    fetch(
      url + "value=" + (inputs.valueSelect === "Free" ? inputs.valueInputs : inputs.valueSelect),
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
          console.log(result);
        }
      })
      .catch((err) => {
        console.log("ERR");
      });
  };

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

  return (
    <>
      <form className="kakaopay-form-frame" onSubmit={handleSubmit}>
        <div className="point-my-point">
          <div className="point-my-pointvalue">
            <div className="point-my-pointtitle">Point</div>
            <div className="point-my-pointdes">
              <div className="point-my-pointdet">
                <div className="point-my-ownpoint" id="point-my-ownpoint-mine">
                  <div id="point-my-ownpoint-mine1">💰 내가 보유한 포인트</div>
                  <div id="point-my-ownpoint-value1">50000 💎</div>
                </div>
                <div className="point-my-ownpoint">
                  <div id="point-my-ownpoint-mine2">내가 충전한 포인트</div>
                  <div id="point-my-ownpoint-value2">50000 💎</div>
                </div>
                <div className="point-my-ownpoint">
                  <div id="point-my-ownpoint-mine2">내가 획득한 포인트</div>
                  <div id="point-my-ownpoint-value2">50000 💎</div>
                </div>
                <div className="point-my-ownpoint">
                  <div id="point-my-ownpoint-mine2">내가 잃은 포인트</div>
                  <div id="point-my-ownpoint-value2">50000 💎</div>
                </div>
              </div>
              <div className="point-watchme">
                <div id="point-watchme-title">WatchMe Point는요..</div>
                <div id="point-watchme-text">
                  WatchMe 서비스의 유료 서비스를 이용하기 위해 필요한 결제 수단입니다. 휴대폰,
                  신용카드, 계좌이체 등 다양한 방법으로 WatchMe Point를 충전하여 이용하실 수
                  있습니다.
                </div>
              </div>
            </div>
          </div>
          <div className="point-log">
            <div className="point-list">🧾 포인트내역</div>
            <div className="point-table">
              <table>
                <thead>
                  <tr className="point-table-align">
                    <th>일자</th>
                    <th>내용</th>
                    <th>포인트내용</th>
                  </tr>
                </thead>
                <tbody>
                  <tr className="point-table-align">
                    <td>2022-08-13</td>
                    <td>Sprint 벌금</td>
                    <td>-500</td>
                  </tr>
                  <tr>
                    <td>2022-08-13</td>
                    <td>Sprint 벌금</td>
                    <td>-500</td>
                  </tr>
                  <tr>
                    <td>2022-08-13</td>
                    <td>Sprint 벌금</td>
                    <td>-500</td>
                  </tr>
                  <tr>
                    <td>2022-08-13</td>
                    <td>Sprint 벌금</td>
                    <td>-500</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
        <div>
          <div className="point-cash-title">캐쉬충전</div>
          <div className="selection">💳 결제수단을 선택하세요</div>
          <div className="kakaopay-pay1">
            <input type="button" className="kakaopay-pay-item" value="카카오페이" />
            <input type="button" className="kakaopay-pay-item" value="신용카드" />
            <input type="button" className="kakaopay-pay-item" value="휴대폰" />
          </div>
          <div className="kakaopay-pay1">
            <input type="button" className="kakaopay-pay-item" value="계좌이체" />
            <input type="button" className="kakaopay-pay-item" value="네이버페이" />
            <input type="button" className="kakaopay-pay-item" value="기타" />
          </div>
          <div className="kakaopay-money">
            결제 금액
            <input placeholder="금액을 입력해주세요." />
          </div>
          <div className="money-selection">충전할 금액을 선택하세요</div>
          <div className="kakaopay-money-btn">
            <input value="+ 1만원" type="button" className="kakaopay-money-btn-item" />
            <input type="button" className="kakaopay-money-btn-item" value="+ 3만원"></input>
            <input type="button" className="kakaopay-money-btn-item" value="+ 5만원"></input>
            <input type="button" className="kakaopay-money-btn-item" value="+ 10만원"></input>
          </div>
        </div>
      </form>
    </>
  );
}

export default PointAdd;
