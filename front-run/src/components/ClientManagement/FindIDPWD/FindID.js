import React from "react"; 
import "./FindID.css";

  
function FindID() {
  return(
    <div>
      <div>아이디 찾기</div>
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

export default FindID;
