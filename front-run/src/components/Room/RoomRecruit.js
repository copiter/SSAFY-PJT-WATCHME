import React from "react";
import { useState, useContext,useEffect } from "react";
import { Link,useNavigate } from "react-router-dom";

import "./RoomRecruit.css";
import { FetchUrl } from "../../store/communication";

import btnPlane from "../../img/Icons/btn-plane.png";
import filter from "../../img/Icons/filter.png";
import down from "../../img/Icons/down.png";
import roomJsons from "../json/roomRec.json"

let page = 1;

function RoomRecruit() {
  const FETCH_URL = useContext(FetchUrl);
  const navigate = useNavigate();
  const [inputs, setInputs] = useState({
    category: "",
    roomSearch: "",
    keyword:"",
    secret:"",
    mode:""
  });
  const handleChange = (event) => {
    const name = event.target.name;
    const value = event.target.value;
    setInputs((values) => ({ ...values, [name]: value }));
  };
	const [rooms, setRooms] = useState(roomJsons["responseData"]["rooms"])
  const inputLength=rooms.length;
  //URL
  const url = `${FETCH_URL}/rooms`;
  let roomNo = 0;

  
  function getCookie(name) {
    const cookie = document.cookie
      .split(";").map((cookie) => cookie.split("="))
      .filter((cookie) => cookie[0] === name);
    return cookie[0][1];
  }


	useEffect(() => { 
    fetch(url)
    .then((response) => {
      if(response.bodyUsed)
      {
        console.log("재사용됨");

      }
      else if(response.ok)
      {
        return response.json();
      }
      else{
        console.log("기본Case4");
      }
    })
    .then((result)=>{
       setRooms(result["responseData"]["rooms"])
    })
    .catch((err) => {
      console.log("ERROR");
    });
	}, [])
  const handleSubmit = (event) => {
    event.preventDefault();
    fetch(url+"?"+
    (inputs.category===""||inputs.category==="all"?"":"category="+inputs.category+"&")
    +(inputs.keyword===""?"":"keyword="+inputs.keyword)
    +(inputs.secret===""?"":"secret="+inputs.secret)
    )
    .then((response) => {
      if(response.bodyUsed)
      {
        console.log("재사용됨");
      }
      else if(response.ok)
      {
        return response.json();
      }
      else{
        console.log("제출Case4");
      }
    })
    .then((result)=>{
       setRooms(result["responseData"]["rooms"])
    })
    .catch((err) => {
      console.log("ERROR");
    });
  
  };
  const ctgChange=(event)=>{//
    event.preventDefault();
    const ARR=["all","공무원","취업","수능","자격증","기타"];
    setInputs((values) => ({ ...values, category:  ARR[event.target.value]}));   
    console.log(ARR[event.target.value]);
     fetch(url+"?"+
    (ARR[event.target.value]===""||ARR[event.target.value]==="all"?"":"category="+ARR[event.target.value]+"&")
    +(inputs.keyword===""?"":"keyword="+inputs.keyword)
    +(inputs.secret===""?"":"secret="+inputs.secret)
    )
    
    .then((response) => {
      if(response.bodyUsed)
      {
        console.log("재사용됨");

      }
      else if(response.ok)
      {
        return response.json();
      }
      else{

        console.log("제출Case4");
      }
    })
    .then((result)=>{
      console.log(1);
      console.log(result["responseData"]["rooms"]);
       setRooms(result["responseData"]["rooms"])
    })
    .catch((err) => {
      console.log("ERROR");
    });
  
  }
  const addMore = (event) => {//테스트 요망
    page++;
    fetch(url+"?"+
    (inputs.category===""||inputs.category==="all"?"":"category="+inputs.category+"&")
    +(page===""||page===1?"":"page="+page+"&")
    +(inputs.keyword===""?"":"keyword="+inputs.keyword)
    +(inputs.secret===""?"":"secret="+inputs.secret)
    )
    .then((response) => {
      if(response.bodyUsed)
      {
        console.log("재사용됨");

      }
      else if(response.ok)
      {
        return response.json();
      }
      else{

        console.log("C4");
      }
    })
    .then((result)=>{
      setRooms(result);
       //setRooms((values) => ({ ...values}+ result["responseData"]["rooms"]))
      console.log(rooms);
    })
    .catch((err) => {
      console.log("ERROR");
    });
  };
  function enteringRoom(id){//방들어가기 문제없이 작동
    const urls= `${FETCH_URL}/rooms/`+id+"/join";
    console.log(urls);
    
    try{
      fetch(urls,{
        method:"POST",
        headers:{
          accessToken: getCookie("accessToken")
        }
      })
      .then((response) => {
        console.log("TEST");
        console.log(response);
        console.log("TEST");
        if(response.bodyUsed)
        {
          console.log("재사용됨");

        }
        else if(response.ok)
        {
          console.log(response);
          return response.json();
        }
      })
      .then((result)=>{
        console.log("성공");
        console.log(result);
        console.log("END");
        navigate(`/RoomDetail/:${id}`);
      })
      .catch((err) => {
        alert("로그인후 이용부탁드립니다.");
      });
    }
    catch{
      alert("로그인후 이용부탁드립니다.")
    }
  };


  function filterClicked(){
  }
  
  return (
    <div id="open-room">
      {/* 공개룸 찾기 section */}
      <form onSubmit={handleSubmit}>
        <div className="open-room__search">
          <div className="search__info">
            <strong>공개룸 찾기</strong>
            <small>Search Open Room</small>
          </div>
          <div className="search__input">
            <input
              type="text"
              name="keyword"
              value={inputs.keyword || ""}
              onChange={handleChange}
              placeholder="찾는 공개룸을 입력하세요"
            />
            <button type="submit">
              <img src={btnPlane} alt="검색" />
            </button>
          </div>
        </div>
      </form>

      {/* 아래 공개룸 보여지는 페이지 */}
      <div className="open-room__module">
        <div className="module__header">
          <ul className="header__tags">
            <li className={inputs.category===""||inputs.category==="all"||inputs.category==="all"?"active":""}onClick={ctgChange} value={0}>All</li>
            <li className={inputs.category==="공무원"?"active":""} onClick={ctgChange} value={1}>공무원</li>
            <li className={inputs.category==="취업"?"active":""}onClick={ctgChange} value={2}>취업</li>
            <li className={inputs.category==="수능"?"active":""}onClick={ctgChange} value={3}>수능</li>
            <li className={inputs.category==="자격증"?"active":""}onClick={ctgChange} value={4}>자격증</li>
            <li className={inputs.category==="기타"?"active":""}onClick={ctgChange} value={5} >기타</li>
          </ul>
          <div className="header__right">
            <Link className="header__link" to="/RoomCreate">
              공개룸 만들기
            </Link>{/*
            <button className="header__filter">
              <img src={filter} alt="필터" 
              onClick={filterClicked}/>
              Filters
            </button>
            <div className="filters">
              <select defaultValue={1} name="secret" onChange={handleChange}>
                <option value="0">비공개방</option>
                <option value="1">공개방</option>
                <option value="2">모두보기</option>
              </select>
              <select defaultValue={1} name="active" onChange={handleChange}>
                <option value="0">비공개방</option>
                <option value="1">공개방</option>
                <option value="2">모두보기</option>
              </select>
              <input type="button" onClick={submitFilter} value="필터적용하기"/>
            </div>*/}
          </div>
          {/*아직 미구현예정 */}
        </div>

        {/* 그룹들 모여 있는 부분. 나중에 component화 예정 */}
        
        <div className="module__rooms">
          <ul className="rooms__whole" value={0}>
            {inputLength>roomNo&&<li value={0} onClick={(e)=>enteringRoom(rooms[e.currentTarget.value]["id"])} >
                <article>
                  <div className="group-specs">
                    {/* 미팅#가 background가 되거나
                        img태그 자체를 적용해야 합니다*/}
                    <img
                      src={
                        rooms[roomNo]["roomImage"] === "none"||rooms[roomNo]["roomImage"] === ""
                          ? "#"
                          : rooms[roomNo]["roomImage"]
                      }
                      alt="#" className="imgSrc"
                    />
                    <div className="group-specs__rules">
                      <span>📝 규칙</span>
                      <ul>
                        <li>✔ 휴대폰 인식</li>
                        <li>✔ 얼굴 인식</li>
                        <li>✔ 캠 켜기</li>
                      </ul>
                    </div>
                  </div>

                  <dl className="group-info">
                    <div className="category member-no">
                      <dt className="sr-only">카테고리</dt>
                      <dl>{rooms[roomNo]["ctgName"]}</dl>
                      <dt className="sr-only">인원수</dt>
                      <dl>
                        &#128509;
                        {rooms[roomNo]["nowNum"]}/
                        {rooms[roomNo]["maxNum"]}
                      </dl>
                    </div>
                    <div>
                      <dt className="sr-only">이름</dt>
                      <dl>{rooms[roomNo]["roomName"]}</dl>
                    </div>
                    <div className="info-content">
                      <dt className="sr-only">세부설명</dt>
                      <dl>{rooms[roomNo]["description"]}</dl>
                    </div>
                  </dl>
                </article>
            </li>}
            {inputLength>++roomNo&&<li value={1} onClick={(e)=>enteringRoom(rooms[e.currentTarget.value]["id"])} >
              <article>
                <div className="group-specs">
                  {/*미팅# 내부에 기능들 표기됨*/}
                  <img
                    src={
                      rooms[roomNo]["roomImage"] === "none"||rooms[roomNo]["roomImage"] === ""
                        ? "#"
                        : rooms[roomNo]["roomImage"]
                    }
                    alt="#" className="imgSrc" 
                  />
                  <div className="group-specs__rules">
                    <span>📝 규칙</span>
                    <ul>
                      <li>✔ 휴대폰 인식</li>
                      <li>✔ 얼굴 인식</li>
                      <li>✔ 캠 켜기</li>
                    </ul>
                  </div>
                </div>

                <dl className="group-info">
                  <div className="category member-no">
                    <dt className="sr-only">카테고리</dt>
                    <dl>{rooms[roomNo]["ctgName"]}</dl>
                    <dt className="sr-only">인원수</dt>
                    <dl>
                      &#128509;
                      {rooms[roomNo]["nowNum"]}/
                      {rooms[roomNo]["maxNum"]}
                    </dl>
                  </div>
                  <div>
                    <dt className="sr-only">이름</dt>
                    <dl>{rooms[roomNo]["roomName"]}</dl>
                  </div>
                  <div className="info-content">
                    <dt className="sr-only">세부설명</dt>
                    <dl>{rooms[roomNo]["description"]}</dl>
                  </div>
                </dl>
              </article>
            </li>}
            {inputLength>++roomNo&&<li value={2} onClick={(e)=>enteringRoom(rooms[e.currentTarget.value]["id"])} >
              <article>
                <div className="group-specs">
                  {/*미팅# 내부에 기능들 표기됨*/}
                  <img
                    src={
                      rooms[roomNo]["roomImage"] === "none"||rooms[roomNo]["roomImage"] === ""
                        ? "#"
                        : rooms[roomNo]["roomImage"]
                    }
                    alt="#" className="imgSrc"
                  />
                  <div className="group-specs__rules">
                    <span>📝 규칙</span>
                    <ul>
                      <li>✔ 휴대폰 인식</li>
                      <li>✔ 얼굴 인식</li>
                      <li>✔ 캠 켜기</li>
                    </ul>
                  </div>
                </div>

                <dl className="group-info">
                  <div className="category member-no">
                    <dt className="sr-only">카테고리</dt>
                    <dl>{rooms[roomNo]["ctgName"]}</dl>
                    <dt className="sr-only">인원수</dt>
                    <dl>
                      &#128509;
                      {rooms[roomNo]["nowNum"]}/
                      {rooms[roomNo]["maxNum"]}
                    </dl>
                  </div>
                  <div>
                    <dt className="sr-only">이름</dt>
                    <dl>{rooms[roomNo]["roomName"]}</dl>
                  </div>
                  <div className="info-content">
                    <dt className="sr-only">세부설명</dt>
                    <dl>{rooms[roomNo]["description"]}</dl>
                  </div>
                </dl>
              </article>
            </li>}
          </ul>
          <ul className="rooms__whole" value={1}>
            {inputLength>++roomNo&&<li value={3} onClick={(e)=>enteringRoom(rooms[e.currentTarget.value]["id"])} > 
            <article>
              <div className="group-specs">
                {/*미팅# 내부에 기능들 표기됨*/}
                <img
                  src={
                    rooms[roomNo]["roomImage"] === "none"||rooms[roomNo]["roomImage"] === ""
                      ? "#"
                      : rooms[roomNo]["roomImage"]
                  }
                  alt="#" className="imgSrc"
                />
                <div className="group-specs__rules">
                  <span>📝 규칙</span>
                  <ul>
                    <li>✔ 휴대폰 인식</li>
                    <li>✔ 얼굴 인식</li>
                    <li>✔ 캠 켜기</li>
                  </ul>
                </div>
              </div>

              <dl className="group-info">
                <div className="category member-no">
                  <dt className="sr-only">카테고리</dt>
                  <dl>{rooms[roomNo]["ctgName"]}</dl>
                  <dt className="sr-only">인원수</dt>
                  <dl>
                    &#128509;
                    {rooms[roomNo]["nowNum"]}/
                    {rooms[roomNo]["maxNum"]}
                  </dl>
                </div>
                <div>
                  <dt className="sr-only">이름</dt>
                  <dl>{rooms[roomNo]["roomName"]}</dl>
                </div>
                <div className="info-content">
                  <dt className="sr-only">세부설명</dt>
                  <dl>{rooms[roomNo]["description"]}</dl>
                </div>
              </dl>
            </article>
            </li> }
            {inputLength>++roomNo&&<li value={4} onClick={(e)=>enteringRoom(rooms[e.currentTarget.value]["id"])} > 
              <article>
                <div className="group-specs">
                  {/*미팅# 내부에 기능들 표기됨*/}
                  <img
                    src={
                      rooms[roomNo]["roomImage"] === "none"||rooms[roomNo]["roomImage"] === ""
                        ? "#"
                        : rooms[roomNo]["roomImage"]
                    }
                    alt="#" className="imgSrc"
                  />
                  <div className="group-specs__rules">
                    <span>📝 규칙</span>
                    <ul>
                      <li>✔ 휴대폰 인식</li>
                      <li>✔ 얼굴 인식</li>
                      <li>✔ 캠 켜기</li>
                    </ul>
                  </div>
                </div>

                <dl className="group-info">
                  <div className="category member-no">
                    <dt className="sr-only">카테고리</dt>
                    <dl>{rooms[roomNo]["ctgName"]}</dl>
                    <dt className="sr-only">인원수</dt>
                    <dl>
                      &#128509;
                      {rooms[roomNo]["nowNum"]}/
                      {rooms[roomNo]["maxNum"]}
                    </dl>
                  </div>
                  <div>
                    <dt className="sr-only">이름</dt>
                    <dl>{rooms[roomNo]["roomName"]}</dl>
                  </div>
                  <div className="info-content">
                    <dt className="sr-only">세부설명</dt>
                    <dl>{rooms[roomNo]["description"]}</dl>
                  </div>
                </dl>
              </article>
            </li> }
            {inputLength>++roomNo&&<li value={5} onClick={(e)=>enteringRoom(rooms[e.currentTarget.value]["id"])} > 
              <article>
                <div className="group-specs">
                  {/*미팅# 내부에 기능들 표기됨*/}
                  <img
                    src={
                      rooms[roomNo]["roomImage"] === "none"||rooms[roomNo]["roomImage"] === ""
                        ? "#"
                        : rooms[roomNo]["roomImage"]
                    }
                    alt="#" className="imgSrc"
                  />
                  <div className="group-specs__rules">
                    <span>📝 규칙</span>
                    <ul>
                      <li>✔ 휴대폰 인식</li>
                      <li>✔ 얼굴 인식</li>
                      <li>✔ 캠 켜기</li>
                    </ul>
                  </div>
                </div>

                <dl className="group-info">
                  <div className="category member-no">
                    <dt className="sr-only">카테고리</dt>
                    <dl>{rooms[roomNo]["ctgName"]}</dl>
                    <dt className="sr-only">인원수</dt>
                    <dl>
                      &#128509;
                      {rooms[roomNo]["nowNum"]}/
                      {rooms[roomNo]["maxNum"]}
                    </dl>
                  </div>
                  <div>
                    <dt className="sr-only">이름</dt>
                    <dl>{rooms[roomNo]["roomName"]}</dl>
                  </div>
                  <div className="info-content">
                    <dt className="sr-only">세부설명</dt>
                    <dl>{rooms[roomNo]["description"]}</dl>
                  </div>
                </dl>
              </article>
            </li>}
          </ul>
        </div>
        <button type="button" id="more-btn" name="roompage" onClick={addMore}>
          <img src={down} alt="+" />
          더보기
        </button>
      </div>
    </div>
  );
}

export default RoomRecruit;
