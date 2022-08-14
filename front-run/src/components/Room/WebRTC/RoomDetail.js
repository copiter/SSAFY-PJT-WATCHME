import axios from "axios";
import { OpenVidu } from "openvidu-browser";
import React, { Component } from "react";
import "./RoomDetail.css";
import UserVideoComponent from "./UserVideoComponent";
import ChatComponent from "./chat/ChatComponent";
import { FetchUrl } from "../../../store/communication";
import { Routes, Route, Link } from "react-router-dom";

import Members from "./componentOnRoom/Members";
import MyStudy from "./componentOnRoom/MyStudy";
import RoomReform from "./componentOnRoom/RoomReform";
import { getCookie } from "../../../Cookie";

//강제 리브세션=추방
//방장 전용 기능 구현.
const OPENVIDU_SERVER_URL = "https://watchme1.shop:4443";
const OPENVIDU_SERVER_SECRET = "MY_SECRET";

class RoomDetail extends Component {
  constructor(props) {
    super(props);

    this.state = {
      mySessionId: "SessionA",
      isRoomLeader:true,
      myUserName: "Participant" + Math.floor(Math.random() * 100),
      session: undefined,
      mainStreamManager: undefined,
      publisher: undefined,
      subscribers: [],
      videoState: true, //보이도록
      audioState: true, //마이크 on
      screenShare: true, //화면공유 버튼
      isScreenShareNow:false,
      chatDisplay: "block",
    };

    this.joinSession = this.joinSession.bind(this);
    this.getUserPermission = this.getUserPermission.bind(this);
    this.leaveSession = this.leaveSession.bind(this);
    this.videoHandlerOn = this.videoHandlerOn.bind(this);
    this.videoHandlerOff = this.videoHandlerOff.bind(this);
    this.audioHandlerOn = this.audioHandlerOn.bind(this);
    this.audioHandlerOff = this.audioHandlerOff.bind(this);
    this.switchCamera = this.switchCamera.bind(this);
    this.screenShare = this.screenShare.bind(this);
    this.handleChangeSessionId = this.handleChangeSessionId.bind(this);
    this.handleChangeUserName = this.handleChangeUserName.bind(this);
    this.handleMainVideoStream = this.handleMainVideoStream.bind(this);
    this.onbeforeunload = this.onbeforeunload.bind(this);
    this.toggleChat = this.toggleChat.bind(this);
    this.checkNotification = this.checkNotification.bind(this);
    this.getMedia();
  }

  componentDidMount() {
    this.joinSession();
    window.addEventListener("beforeunload", this.onbeforeunload);
  }
  componentWillUnmount() {
    window.removeEventListener("beforeunload", this.onbeforeunload);
  }
  onbeforeunload(event) {
    this.leaveSession();
  }
  handleChangeSessionId(e) {
    this.setState({
      mySessionId: e.target.value,
    });
  }
  handleChangeUserName(e) {
    this.setState({
      myUserName: e.target.value,
    });
  }
  handleMainVideoStream(stream) {
    if (this.state.mainStreamManager !== stream) {
      this.setState({
        mainStreamManager: stream,
      });
    }
  }
  deleteSubscriber(streamManager) {
    let subscribers = this.state.subscribers;
    let index = subscribers.indexOf(streamManager, 0);
    if (index > -1) {
      subscribers.splice(index, 1);
      this.setState({
        subscribers: subscribers,
      });
    }
  }
  async getUserPermission() {}
  joinSession() {
    //세션조인
    // --- 1) Get an OpenVidu object ---
    this.OV = new OpenVidu();

    let myNickName = localStorage.getItem("nickName");
    console.log("닉네임");
    console.log(myNickName);
    this.setState({
      myUserName: myNickName,
    });
    // --- 2) Init a session ---
    this.setState(
      {
        session: this.OV.initSession(),
      },
      () => {
        var mySession = this.state.session;

        // --- 3) Specify the actions when events take place in the session ---

        // On every new Stream received...
        mySession.on("streamCreated", (event) => {
          // Subscribe to the Stream to receive it. Second parameter is undefined
          // so OpenVidu doesn't create an HTML video by its own
          var subscriber = mySession.subscribe(event.stream, undefined);
          var subscribers = this.state.subscribers;
          subscribers.push(subscriber);

          // Update the state with the new subscribers
          this.setState({
            subscribers: subscribers,
          });
        });

        // On every Stream destroyed...
        mySession.on("streamDestroyed", (event) => {
          // Remove the stream from 'subscribers' array
          this.deleteSubscriber(event.stream.streamManager);
        });

        // On every asynchronous exception...
        mySession.on("exception", (exception) => {
          console.warn(exception);
        });

        // --- 4) Connect to the session with a valid user token ---

        // 'getToken' method is simulating what your server-side should do.
        // 'token' parameter should be retrieved and returned by your own backend
        this.getToken().then((token) => {
          // First param is the token got from OpenVidu Server. Second param can be retrieved by every user on event
          // 'streamCreated' (property Stream.connection.data), and will be appended to DOM as the user's nickname
          mySession
            .connect(token, { clientData: this.state.myUserName })
            .then(async () => {
              //브라우저 비디오, 오디오 권한 설정
              try {
                var devices = await navigator.mediaDevices.getUserMedia({
                  video: true,
                  audio: true,
                });
              } catch (e) {
                alert(
                  "서비스 사용을 위해 카메라와 마이크 권한이 필요합니다. 권한 허용 후 새로고침 해주세요"
                );
              }

              devices = await this.OV.getDevices(); //디바이스 없으면 가져옴

              var videoDevices = devices.filter(
                (device) => device.kind === "videoinput"
              );

              // --- 5) Get your own camera stream ---

              // Init a publisher passing undefined as targetElement (we don't want OpenVidu to insert a video
              // element: we will manage it on our own) and with the desired properties
              let publisher = this.OV.initPublisher(undefined, {
                audioSource: undefined, // The source of audio. If undefined default microphone
                videoSource: videoDevices[0].deviceId, // The source of video. If undefined default webcam
                publishAudio: this.state.audioState, // Whether you want to start publishing with your audio unmuted or not
                publishVideo: this.state.videoState, // Whether you want to start publishing with your video enabled or not
                resolution: "640x480", // The resolution of your video
                frameRate: 30, // The frame rate of your video
                insertMode: "APPEND", // How the video is inserted in the target element 'video-container'
                mirror: false, // Whether to mirror your local video or not
              });

              // --- 6) Publish your stream ---

              mySession.publish(publisher);

              console.log("퍼블리셔", publisher);

              // Set the main video in the page to display our webcam and store our Publisher
              this.setState({
                currentVideoDevice: videoDevices[0],
                mainStreamManager: publisher,
                publisher: publisher,
              });
            })
            .catch((error) => {
              console.log(
                "There was an error connecting to the session:",
                error.code,
                error.message
              );
            });
        });
      }
    );
  }

  leaveSession() {
    //세션 탈출
    // --- 7) Leave the session by calling 'disconnect' method over the Session object ---
    clearInterval();

    const mySession = this.state.session;
    const FETCH_URL = FetchUrl._currentValue;
    const id = window.location.pathname.split("/")[2].substring(0);
    const url = `${FETCH_URL}/rooms/` + id + "/leave";

    console.log(id);
    console.log(url);
    console.log("방나가기 시도");

    fetch(url, {
      method: "POST",
      headers:{accessToken: getCookie("accessToken")}
    })
      .then((response) => {
        console.log(response);
        if (response.ok) {
          console.log("리스폰스 성공");
          return response.json(); //ok떨어지면 바로 종료.
        } else {
          response.json().then((data) => {
            console.log("ERR방나기 실패");
            let errorMessage = "";
            throw new Error(errorMessage);
          });
        }
      })
      .then((result) => {
        console.log("RES:");
        if (result != null) {
          console.log("NOTNULL:");
          console.log(result);
          if (result.code === 200) {
            console.log("방나가기 성공");
          } else {
            console.log("오류가 발생하였습니다.");
          }
        }
      })
      .catch((err) => {
        console.log("ERR여기 못나감");
      });

    // Empty all properties...
    this.OV = null;
    try {
      mySession.disconnect();
      window.location.href = "../../";
    } catch {
      console.log("디스콘실패");
    }
    this.setState({
      session: undefined,
      subscribers: [],
      mySessionId: "SessionA",
      myUserName: "Participant" + Math.floor(Math.random() * 100),
      mainStreamManager: undefined,
      publisher: undefined,
    });
  }

  async switchCamera() {
    //카메라 교환
    try {
      const devices = await this.OV.getDevices();
      var videoDevices = devices.filter(
        (device) => device.kind === "videoinput"
      );

      if (videoDevices && videoDevices.length > 1) {
        var newVideoDevice = videoDevices.filter(
          (device) => device.deviceId !== this.state.currentVideoDevice.deviceId
        );

        if (newVideoDevice.length > 0) {
          // Creating a new publisher with specific videoSource
          // In mobile devices the default and first camera is the front one
          var newPublisher = this.OV.initPublisher(undefined, {
            videoSource: newVideoDevice[0].deviceId,
            publishAudio: true,
            publishVideo: true,
            mirror: true,
          });

          //newPublisher.once("accessAllowed", () => {
          await this.state.session.unpublish(this.state.mainStreamManager);

          await this.state.session.publish(newPublisher);
          this.setState({
            currentVideoDevice: newVideoDevice,
            mainStreamManager: newPublisher,
            publisher: newPublisher,
          });
        }
      }
    } catch (e) {
      console.error(e);
    }
  }

  //화면 공유 기능
  async screenShare() {
    try{
      const latestPublisher = this.state.publisher;
    

    //화면 공유 중지 누른 뒤 로직
    if(this.state.isScreenShareNow){
       newPublisher.stream
        .getMediaStream()
        .getVideoTracks()[0]
        .addEventListener("ended", () => {
          console.log('User pressed the "Stop sharing" button');
          this.state.session.unpublish(newPublisher);
          this.state.session.publish(latestPublisher);
          this.setState({
            screenShare: true,
          });
        });
    }
    else{
//기존 정보 저장
          
      //screenshare를 위한 publisher 생성
      var newPublisher = this.OV.initPublisher(undefined, {
        videoSource: "screen",
      });

      //mainStream 없애고 새로 생성한 stream 추가
      await this.state.session.unpublish(this.state.mainStreamManager);
      await this.state.session.publish(newPublisher);
      this.setState({
        mainStreamManager: newPublisher,
        publisher: newPublisher,
      });

    }
      console.log(this.state.isScreenShareNow)
      this.state.isScreenShareNow=!this.state.isScreenShareNow;
    }
    catch{}
  }

  //Video On / Off
  videoHandlerOn() {
    this.setState({
      videoState: true,
      screenShare: true,
    });
    this.state.publisher.publishVideo(true);
  }
  videoHandlerOff() {
    this.setState({
      videoState: false,
      screenShare: false,
    });
    this.state.publisher.publishVideo(false);
  }
  //Audio ON / OFF
  audioHandlerOn() {
    this.state.publisher.publishAudio(true);

    // Update the state with the new subscribers
    this.setState({
      audioState: true,
    });
  }
  audioHandlerOff() {
    this.state.publisher.publishAudio(false);

    this.setState({
      audioState: false,
    });
  }

  toggleChat(property) {
    let display = property;

    if (display === undefined) {
      display = this.state.chatDisplay === "none" ? "block" : "none";
    }
    if (display === "block") {
      this.setState({ chatDisplay: display, messageReceived: false });
    } else {
      console.log("chat", display);
      this.setState({ chatDisplay: display });
    }
  }

  checkNotification(event) {
    this.setState({
      messageReceived: this.state.chatDisplay === "none",
    });
  }

  closeRoom() {
    const FETCH_URL = FetchUrl._currentValue;
    fetch(FETCH_URL, {
      method: "POST",
      headers: {
        accessToken: this.getCookie("accessToken"),
      },
    });

    this.closeSession();
    this.banALL();
  }
  closeSession() {}
  banALL() {}

  async getMedia() {
    const FETCH_URL = FetchUrl._currentValue;
    const id = window.location.pathname.split("/")[2].substring(0);
    let mode = "MODE1";

    console.log(id);
    fetch(`${FETCH_URL}/rooms/` + id, {
      headers: {
        accessToken: getCookie("accessToken"),
      },
    })
      .then((response) => {
        if (response.ok) {
          console.log(response);
          return response.json(); //ok떨어지면 바로 종료.
        } else {
          response.json().then((data) => {
            let errorMessage = "";
            throw new Error(errorMessage);
          });
        }
      })
      .then((result) => {
        if (result != null) {
          console.log("성공");
          console.log(result.responseData.room);
          console.log("백통신 결과입니다.");
          this.state.mySessionId=result.responseData.room.name;
          this.state.isRoomLeader=(result.responseData.room.leaderTrue===0?false:true);
          localStorage.setItem({"L":this.state.isRoomLeader});
          console.log("뭐냐");  
          this.state.screenShare=(result.responseData.room.mode==="MODE1"?false:true)
          console.log("모드");
          mode=result.responseData.room.mode;
          console.log("모드2");
          setInterval(() => {
            this.openTeli(id, mode);
          }, 3000);
        }
      })
      .catch((err) => {
        console.log("백통신 실패");
      });
    
    /*
    try {
      const blob = await imageCapture.takePhoto();
      const image = document.querySelector('img');
      image.src = URL.createObjectURL(blob);
      console.log(blob);
      console.log(image);
    } catch (err) {
      console.error("takePhoto() failed: ", err);
    }
    */
  }
  async openTeli(id, mode) {
    console.log(mode);
    console.log("정상작동");
    const formData = new FormData();
    const json = { nickName: this.state.myUserName, roomId: id, mode: mode };
    formData.append(
      "flaskDTO",
      new Blob([JSON.stringify(json)], { type: "application/json" }),
      "flaskDTO"
    );

    let imageCapture;

    try {
      const stream = await navigator.mediaDevices.getUserMedia({
        video: { pan: true, tilt: true, zoom: true },
      });

      const [track] = stream.getVideoTracks();
      imageCapture = new ImageCapture(track);
    } catch (err) {
      console.error(err);
    }
    const blob = await imageCapture.takePhoto();
    formData.append("img", blob, "img");

   fetch("https://watchme1.shop/flask/openCV", {
      method: "POST",
      body: formData,
    })
      .then((response) => {
        if (response.ok) {
          return response.json(); //ok떨어지면 바로 종료.
        } else {
          response.json().then((data) => {
            let errorMessage = "";
            throw new Error(errorMessage);
          });
        }
      })
      .then((result) => {
        if (result != null) {
          if (result.code === 200) {
            console.log("오류없음");
          } else if (result.code === 205) {
            this.errorFound();
          } else if(result.code === 202){
            
          }
          else if(result.code==504){
            console.log("504에러");
          }
          else if(result.code===522)
          {
            console.log("522에러");
          }
          else if(result.code==553)
          {
            console.log("553에러");
          }
        }
      })
      .catch((err) => {
        console.log("ERR여기임");
      }); 
  
  }



  errorFound(){
    alert("감지되었습니다.");
  }
  ban(){
    
    alert("벌점이 과다로 추방되었습니다.");
  }

  render() {
    const mySessionId = this.state.mySessionId;
    var chatDisplay = { display: this.state.chatDisplay };
    return (
      <div className="container">
        {this.state.session === undefined ? null : (
          <div id="session" className="out">
            <div className="Main">
              <div id="session-header" className="Header">
                <div id="session-title" className="headerTitle"><h1>{mySessionId}</h1></div>
                <div className="headerButtons">
                  <div className="btnTotal">
                    {this.state.videoState && (
                      <button onClick={this.videoHandlerOff} className="btns ">Video OFF</button>
                    )}
                    {!this.state.videoState && (
                      <button onClick={this.videoHandlerOn} className="btns ">Video ON</button>
                    )}
                    {this.state.audioState && (
                      <button onClick={this.audioHandlerOff} className="btns ">Audio OFF</button>
                    )}
                    {!this.state.audioState && (
                      <button onClick={this.audioHandlerOn} className="btns ">Audio ON</button>
                    )}
                    {this.state.screenShare && (
                      <button onClick={this.screenShare} className="btns">화면공유</button>
                    )}
                  </div>
                </div>
                 
              </div>
              <div className="cams">
                <div className="myCams">{
                  //개인카메라
                  this.state.mainStreamManager !== undefined ? (
                    <div id="main-video" className="col-md-6">
                      <UserVideoComponent
                        streamManager={this.state.mainStreamManager}
                        audioState={this.state.audioState}
                      />
                      <input
                        className="btn btn-large btn-success"
                        type="button"
                        id="buttonSwitchCamera"
                        onClick={this.switchCamera}
                        value="내 화면을 다시 보기"
                      />
                    </div>
                  ) : null
                }
                  <div id="video-container" className="col-md-6">{console.log(this.state.isScreenShareNow)}
                    {this.state.publisher !== undefined &&this.state.isScreenShareNow? (
                      <div className="stream-container col-md-6 col-xs-6">
                        <UserVideoComponent streamManager={this.state.publisher} />
                      </div>
                    ) : null}
                  </div>
                </div>
                <div className="others">
                  {this.state.subscribers.map((sub, i) => (
                    <div
                      key={i}
                      className="stream-container col-md-6 col-xs-6 "
                      onClick={() => this.handleMainVideoStream(sub)}
                    >
                      <UserVideoComponent streamManager={sub} />
                    </div>
                  ))}
                </div>
              </div>
            </div>
            <div className="Aside">
              <div className="rightSide">
                <div className="sideNav">
                  <div className="linksUl">
                    <Link to="./">
                      <button className="linksLi">내 공부</button>
                    </Link>
                    <Link to="./members">
                       <button className="linksLi">맴버</button>
                    </Link>
                    {this.state.isRoomLeader?<Link to="./RoomReform">
                      <button className="linksLi">방 수정</button>
                    </Link>:""}
                  </div>
                </div>
                <div className="AsideMain">
                  <div className="sideBoards">
                    <Routes >
                      <Route path="/" element={<MyStudy />} />
                      <Route path="/members" element={<Members />} />
                      <Route path="/RoomReform" element={<RoomReform />} />
                    </Routes>
                  </div>
                  <div id="chat-container" className="chatBoards" >
                  {this.state.publisher !== undefined &&
                    this.state.publisher.stream !== undefined && (
                      <div
                        className="OT_root OT_publisher custom-class"
                        style={chatDisplay}
                      >
                        <ChatComponent
                          user={this.state.publisher}
                          chatDisplay={this.state.chatDisplay}
                          close={this.toggleChat}
                          messageReceived={this.checkNotification}
                        />
                      </div>
                    )}  
                  </div>
                </div>
                
              </div>
              <div className="btnRight">
                  <div className="btnRightInner">
                    <input
                        className="btn btn-large btn-danger btnR"
                        type="button"
                        id="buttonLeaveSession"
                        onClick={this.leaveSession}
                        value="방 나가기"
                      />
                    {this.state.isRoomLeader?<input
                      className="btn btn-large btn-danger btnR"
                      type="button"
                      id="buttonLeaveSession"
                      onClick={this.closeRoom}
                      value="방 닫기"
                    />:""}
                    <button className="btnR" onClick={() => this.toggleChat()}>채팅</button>
                  </div>
                </div>
            </div>
          </div>
        )}
      </div>
    );
  }

  /**
   * --------------------------
   * SERVER-SIDE RESPONSIBILITY
   * --------------------------
   * These methods retrieve the mandatory user token from OpenVidu Server.
   * This behavior MUST BE IN YOUR SERVER-SIDE IN PRODUCTION (by using
   * the API REST, openvidu-java-client or openvidu-node-client):
   *   1) Initialize a Session in OpenVidu Server	(POST /openvidu/api/sessions)
   *   2) Create a Connection in OpenVidu Server (POST /openvidu/api/sessions/<SESSION_ID>/connection)
   *   3) The Connection.token must be consumed in Session.connect() method
   */

  getToken() {
    return this.createSession(this.state.mySessionId).then((sessionId) =>
      this.createToken(sessionId)
    );
  }

  createSession(sessionId) {
    return new Promise((resolve, reject) => {
      var data = JSON.stringify({ customSessionId: sessionId });
      axios
        .post(OPENVIDU_SERVER_URL + "/openvidu/api/sessions", data, {
          headers: {
            Authorization:
              "Basic " + btoa("OPENVIDUAPP:" + OPENVIDU_SERVER_SECRET),
            "Content-Type": "application/json",
          },
        })
        .then((response) => {
          console.log("CREATE SESION", response);
          resolve(response.data.id);
        })
        .catch((response) => {
          var error = Object.assign({}, response);
          if (error?.response?.status === 409) {
            resolve(sessionId);
          } else {
            console.log(error);
            console.warn(
              "No connection to OpenVidu Server. This may be a certificate error at " +
                OPENVIDU_SERVER_URL
            );
            if (
              window.confirm(
                'No connection to OpenVidu Server. This may be a certificate error at "' +
                  OPENVIDU_SERVER_URL +
                  '"\n\nClick OK to navigate and accept it. ' +
                  'If no certificate warning is shown, then check that your OpenVidu Server is up and running at "' +
                  OPENVIDU_SERVER_URL +
                  '"'
              )
            ) {
              window.location.assign(
                OPENVIDU_SERVER_URL + "/accept-certificate"
              );
            }
          }
        });
    });
  }

  createToken(sessionId) {
    return new Promise((resolve, reject) => {
      var data = {};
      axios
        .post(
          OPENVIDU_SERVER_URL +
            "/openvidu/api/sessions/" +
            sessionId +
            "/connection",
          data,
          {
            headers: {
              Authorization:
                "Basic " + btoa("OPENVIDUAPP:" + OPENVIDU_SERVER_SECRET),
              "Content-Type": "application/json",
            },
          }
        )
        .then((response) => {
          console.log("TOKEN", response);
          resolve(response.data.token);
        })
        .catch((error) => reject(error));
    });
  }
}

export default RoomDetail;
