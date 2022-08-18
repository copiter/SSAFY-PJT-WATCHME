import React from "react";

import { useNavigate } from "react-router-dom";
import swal from "sweetalert";
import "./PointCancel.css";

function PointCancel() {
  const navigate = useNavigate();
  swal(
    "사용자 요청에 의해 결제가 취소되었습니다",
    "포인트 페이지로 전환됩니다",
    "warning"
  );
  setInterval(() => {
    navigate("/PointAdd");
  }, [2000]);

  return (
    <div className="point-cancel">
      {/* <div className="point-cancel-msg">
        사용자 요청에 의해 결제가 취소되었습니다.
      </div>
      <div className="point-cancel-loading-msg">🚨 다시 시도해주세요.</div> */}
    </div>
  );
}

export default PointCancel;
