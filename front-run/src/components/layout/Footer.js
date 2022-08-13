import React from "react";

import "./Footer.css";
import logo from "../../img/logo.png";

function Footer() {
  return (
    <footer>
      <section id="footer">
        <h2 id="coInfo">
          <img src={logo} alt="로고" />
          <span>WATCHME</span>
        </h2>
        <strong id="contact-info">CONTACT INFO</strong>
        <ul id="list-sns">
          <li>
            Project By SSAFY 7th Team <b>WatchMe</b>
          </li>
          <li>EMAIL : hrpt8@naver.com</li>
          <li>
            Developer : Seok-In, Tae-Gyeong, Young-Hoon, Chul-Min, Dong-Hun
          </li>
        </ul>
        <small id="txt-copy">
          &copy; 2022 thecreation.design | All rights reserved
        </small>
        <small id="txt-created">Created with love by July</small>
      </section>
    </footer>
  );
}

export default Footer;
