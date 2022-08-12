import React from "react";

import "./Banner.css";

function Banner() {
  return (
    <section id="mainpage__banner">
      {/* 배너섹션 수정사항 없을예정. */}
      <div id="banner__left">
        <div id="mainpage__banner__title">
          WATCH ME는
          <br />
          혼자여도 같이합니다.
        </div>
        <div id="mainpage__banner__description">
          공부만 시작하면 스마트폰에 눈이 팔린다구요?
          <br />
          누가 나좀 감시해줬으면 좋겠다구요?
          <br />
          <br />
          Watch Me와 함께라면 걱정마세요!
          <br />
          혼자만의 시간을 보낼 수도,
          <br /> 그룹을 만들어서 스프린트를 만들수도,
          <br />
          아무와나 함께할 수 있습니다.
        </div>
      </div>
      <div id="mainpage__banner__main">
        의지박약자여,
        <br />
        이리로 모여라.
        <br />
        같이 집중하자👊
      </div>
    </section>
  );
}

export default Banner;
