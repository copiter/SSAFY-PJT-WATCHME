import React from "react";

import { useContext } from "react";
import { useParams } from "react-router-dom";
import { FetchUrl } from "../../../store/communication";
import "./PointCancel.css";

function PointCancel() {
  return (
    <div className="point-cancel">
      <div className="point-cancel-msg">
        사용자 요청에 의해 결제가 취소되었습니다.
      </div>
      <div className="point-cancel-loading-msg">🚨 다시 시도해주세요.</div>
    </div>
  );
}
{
  /* <html>
  <h2>hi</h2>
</html>; */
}

export default PointCancel;
