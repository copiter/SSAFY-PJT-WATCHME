import React from "react";

import { useNavigate } from "react-router-dom";
import swal from "sweetalert";

import "./PointFail.css";
function PointFail() {
  const navigate = useNavigate();
  swal("결제 진행중에 실패되었습니다", "포인트 페이지로 전환됩니다", "warning");
  setInterval(() => {
    navigate("/PointAdd");
  }, [2000]);

  return (
    <div className="point-success">
      {/* <div className="point-success-msg">결제 진행중에 실패되었습니다.</div>
      <div className="point-loading-msg">🚨 다시 시도해주세요.</div> */}
    </div>
  );
}
{
  /* <html>
  <h2>hi</h2>
</html>; */
}

export default PointFail;
