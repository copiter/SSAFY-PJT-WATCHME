import React from "react"; 
import "./FindPWD.css";

  
function FindPWD() {
  return(
    <div>
      <div>비밀번호 찾기</div>
      <div>
        <form>
          <input type="string"/>
          <input type="password"/>
          <input type="submit"/>
        </form>
      </div>

    </div>
  )
}

export default FindPWD;

  