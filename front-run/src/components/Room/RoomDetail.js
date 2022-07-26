import React, { useState } from "react";

import out from "../../img/Icons/out.png";
import headset from "../../img/Icons/headset.png";
import headset_mute from "../../img/Icons/headset_mute.png";
import mic from "../../img/Icons/mic.png";
import mic_mute from "../../img/Icons/mic_mute.png";
import setting from "../../img/Icons/setting.png";
import share_video from "../../img/Icons/share_video.png";

import "./RoomDetail.css";

function RoomDetail() {
  const [isMicOn, setIsMicOn] = useState(false);

  return (
    <>
      <div className="room">
        <div className="nav">
          <button type="button">방장설정</button>
        </div>
        <div className="body">
          <div className="webrtc">webrtc 페이지</div>
          <div className="side">side bar</div>
        </div>
        <div className="footer">
          <img src={share_video} alt="out" width={30} />
          <img src={mic} alt="out" width={30} />

          <img src={mic_mute} alt="out" width={30} />
          <img src={headset} alt="out" width={30} />
          <img src={headset_mute} alt="out" width={30} />
          <img src={setting} alt="out" width={30} />
          <img src={out} alt="out" width={30} />
        </div>
      </div>
    </>
  );
}

export default RoomDetail;
