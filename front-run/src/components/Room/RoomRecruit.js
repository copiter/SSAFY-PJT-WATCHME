import React from "react";
import { Link } from "react-router-dom";
import roomInfor from "../json/roomInfor"
let rooms=  roomInfor[0]["MainpageRooms"];
let roomNo=0;

function RoomRecruit() {
  return (
    <form>
      <div id="위에 검색창">
        <div>공개룸찾기</div>
        <input type="text" 
        
        placeholder="찾는 공개룸을 입력하세요"/>
        <input type="submit"/>
      </div>
      <div id="흰부분">
          <div id="up">
            <div id="tags">
              all
            </div>
            <Link to="/RoomCreate"><div >공개방 만들기</div></Link>
            <div >필터</div>{/*아직 미구현예정 */}
          </div>
          <div className="rows">
            <Link to='/RoomDetail'> 
              <div className='Lines'>
                <div className=''>{/*미팅룸 이미지 내부에 기능들 표기됨*/}
                  {rooms[roomNo]["roomImage"]==="none"?"(이미지없음)":rooms[roomNo]["roomImage"]}
                  <div className=''>
                    <p>⏱ 기능</p>
                    ✔ 뽀모도로<br></br>
                    ✔ 캠 + 화면공유<br></br>
                    ✔ 채팅 O<br></br><br></br>
                    <p>📝 규칙</p>
                    ✔ 휴대폰 인식<br></br>
                    ✔ 얼굴 인식<br></br>
                    ✔ 캠 켜기<br></br>
                  </div>
                </div>
                <div className=''>
                  <div className=''>{/*태그*/}
                    {rooms[roomNo]["roomTag"]}
                  </div>
                  <div className=''>{/*이름*/}
                    {rooms[roomNo]["roomName"]}
                  </div>
                  <div className=''>{/*세부설명*/}
                    {rooms[roomNo]["roomDiscription"]}
                  </div>
                  <div className=''>
                    {rooms[roomNo]["romMemberNo"]}/{rooms[roomNo]["roomMemberMaxNo"]}
                  </div>
                </div>
              </div>
            </Link>
            <Link to='/RoomDetail'> 
              <div className='Lines'>
                <div className=''>{/*미팅룸 이미지 내부에 기능들 표기됨*/}
                  {rooms[roomNo]["roomImage"]==="none"?"(이미지없음)":rooms[roomNo]["roomImage"]}
                  <div className=''>
                    <p>⏱ 기능</p>
                    ✔ 뽀모도로<br></br>
                    ✔ 캠 + 화면공유<br></br>
                    ✔ 채팅 O<br></br><br></br>
                    <p>📝 규칙</p>
                    ✔ 휴대폰 인식<br></br>
                    ✔ 얼굴 인식<br></br>
                    ✔ 캠 켜기<br></br>
                  </div>
                </div>
                <div className=''>
                  <div className=''>{/*태그*/}
                    {rooms[++roomNo]["roomTag"]}
                  </div>
                  <div className=''>{/*이름*/}
                    {rooms[roomNo]["roomName"]}
                  </div>
                  <div className=''>{/*세부설명*/}
                    {rooms[roomNo]["roomDiscription"]}
                  </div>
                  <div className=''>
                    {rooms[roomNo]["romMemberNo"]}/{rooms[roomNo]["roomMemberMaxNo"]}
                  </div>
                </div>
              </div>
            </Link>
            
          </div>
          <div id="more">
            <button>더보기</button>
          </div>
      </div>
    </form>
  );
}

export default RoomRecruit;
