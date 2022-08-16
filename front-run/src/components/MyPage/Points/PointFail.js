import React from "react";

import { useContext } from "react";
import { useParams } from "react-router-dom";
import { FetchUrl } from "../../../store/communication";
import "./PointFail.css";
function PointFail() {
  return (
    <div className="point-success">
      <div className="point-success-msg">결제 진행중에 실패되었습니다.</div>
      <div className="point-loading-msg">🚨 다시 시도해주세요.</div>
    </div>
  );
}
{
  /* <html>
  <h2>hi</h2>
</html>; */
}

export default PointFail;
