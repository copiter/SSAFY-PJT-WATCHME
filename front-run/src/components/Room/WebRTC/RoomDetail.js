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
      //방데이터
      mySessionId: "SessionA", //세션이름
      myUserName: "Participant" + Math.floor(Math.random() * 100), //내 닉네임.
      isRoomLeader: true, //방장인지 체크->방장전용 데이터 보임
      mode: "MODE1",

      //카메라 설정 데이터
      videoState: true, //보이도록
      audioState: true, //마이크 on
      screenShare: true, //화면공유 버튼,
      //채팅관련 설정 데이터
      chatDisplay: "block",

      //내 카메라 및 영상 배치 관련 데이터
      session: undefined,
      mainStreamManager: undefined,
      publisher: undefined,
      latestPublisher: undefined,

      //전체 카메라 관련 데이터
      subscribers: [],
      isScreenShareNow: false,
      screenShareCameraNeeded: false,
    };

    this.joinSession = this.joinSession.bind(this);
    this.getUserPermission = this.getUserPermission.bind(this);
    this.leaveSession = this.leaveSession.bind(this);
    this.videoHandlerOn = this.videoHandlerOn.bind(this);
    this.videoHandlerOff = this.videoHandlerOff.bind(this);
    this.audioHandlerOn = this.audioHandlerOn.bind(this);
    this.audioHandlerOff = this.audioHandlerOff.bind(this);
    this.switchCamera = this.switchCamera.bind(this);
    this.shareScreen = this.shareScreen.bind(this);
    this.handleChangeSessionId = this.handleChangeSessionId.bind(this);
    this.handleChangeUserName = this.handleChangeUserName.bind(this);
    this.handleMainVideoStream = this.handleMainVideoStream.bind(this);
    this.onbeforeunload = this.onbeforeunload.bind(this);
    this.toggleChat = this.toggleChat.bind(this);
    this.checkNotification = this.checkNotification.bind(this);
    this.shareScreenCancle = this.shareScreenCancle.bind(this);
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
      headers: { accessToken: getCookie("accessToken") },
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
      mySessionId: "SessionA" + Math.floor(Math.random() * 100),
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

  //오디오 관련
  audioHandlerOn() {}
  audioHandlerOff() {
    this.state.publisher.publishAudio(false);

    this.setState({
      audioState: false,
    });
  }

  //화면 공유 기능
  async shareScreen() {
    try {
      const latestPublisher = this.state.publisher;
      var newPublisher = this.OV.initPublisher(undefined, {
        videoSource: "screen",
      });
      //mainStream 없애고 새로 생성한 stream 추가
      await this.state.session.unpublish(this.state.mainStreamManager);
      await this.state.session.publish(newPublisher);
      await this.state.session.unpublish(this.state.mainStreamManager);
      await this.state.session.publish(latestPublisher);
      this.setState({
        mainStreamManager: newPublisher,
        publisher: latestPublisher,
        isScreenShareNow: true,
        screenShareCameraNeeded: true,
      });
    } catch {}
  }
  async shareScreenCancle() {
    const latestPublisher = this.state.publisher;
    this.setState({
      isScreenShareNow: false,
      screenShareCameraNeeded: false,
    });
    await this.state.session.unpublish(this.state.publisher);
    await this.state.session.publish(latestPublisher);

    /*
     //화면 공유 중지 누른 뒤 로직
      newPublisher.stream
      .getMediaStream()
      .getVideoTracks()[0]
      .addEventListener("ended", () => {
        console.log('User pressed the "Stop sharing" button');
        this.state.session.unpublish(newPublisher);
        this.state.session.publish(latestPublisher);
       
      });*/
  }

  //방 기본설정들, 문제없이 진행됨.
  //비디오 키고 끄기관련
  videoHandlerOn() {
    this.setState({
      videoState: true,
    });
    this.state.publisher.publishVideo(true);
  }
  videoHandlerOff() {
    this.setState({
      videoState: false,
    });
    this.state.publisher.publishVideo(false);
  }
  //오디오 관련
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
  //채팅관련
  toggleChat(property) {
    //채팅 열고 닫기
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
    //채팅용
    this.setState({
      messageReceived: this.state.chatDisplay === "none",
    });
  }

  closeRoom() {
    //아직 구현안됨.
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

  joinSession() {
    //방데이터 세팅을 위한 백과의 통신
    const FETCH_URL = FetchUrl._currentValue;
    const id = window.location.pathname.split("/")[2].substring(0);
    fetch(`${FETCH_URL}/rooms/` + id, {
      headers: {
        accessToken: getCookie("accessToken"),
      },
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
          console.log("리저트 테스트");
          console.log(result.responseData.room);
          this.setState({
            mySessionId: result.responseData.room.name,
            isRoomLeader:
              result.responseData.room.leaderTrue === 0 ? false : true,
            screenShare:
              result.responseData.room.mode === "MODE1" ? false : true,
            mode: result.responseData.room.mode,
          });
          this.joinSessionSetOpenVidu(id);
          setInterval(() => {
            this.openTeli(
              id,
              result.responseData.room.name,
              result.responseData.room.mode
            );
          }, 3000);
        }
      })
      .catch((err) => {
        console.log("백통신 실패");
      });

    //통신용 개인 닉네임 확인
    let myNickName = localStorage.getItem("nickName");
    this.setState({
      myUserName: myNickName,
    });
  }
  async joinSessionSetOpenVidu(newSessionId) {
    console.log("오픈비두 테스트");
    console.log(this.state);
    //오픈비두 세팅
    this.OV = new OpenVidu();
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
          this.setState({ subscribers: subscribers });
        });

        this.sessionStreamCheck();

        // --- 4) Connect to the session with a valid user token ---
        // 'getToken' method is simulating what your server-side should do.
        // 'token' parameter should be retrieved and returned by your own backend
        this.getToken(newSessionId).then((token) => {
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
              // Set the main video in the page to display our webcam and store our Publisher
              this.setState({
                currentVideoDevice: videoDevices[0],
                mainStreamManager: publisher,
                publisher: publisher,
              });
            })
            .catch((error) => {
              console.log("오픈비드 JoinSession에러입니다.");
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

  sessionStreamCheck() {
    var mySession = this.state.session;
    // 스트림 파괴될때마다 'subscribers' array에서 스트림 제거
    mySession.on("streamDestroyed", (event) => {
      this.deleteSubscriber(event.stream.streamManager);
    });
    //비정상적인 예외사항 발생시 경고날림
    mySession.on("exception", (exception) => {
      console.warn(exception);
    });
  }
  async openTeli(id) {
    //특정 기간마다 반복해서
    //포멧 만들기
    //JSON넣기
    const formData = new FormData();
    const json = {
      nickName: this.state.myUserName,
      roomId: id,
      mode: this.state.mode,
    };
    formData.append(
      "flaskDTO",
      new Blob([JSON.stringify(json)], { type: "application/json" }),
      "flaskDTO"
    );

    //이미지 넣기
    let imageCapture;
    try {
      const stream = await navigator.mlogediaDevices.getUserMedia({
        video: { pan: true, tilt: true, zoom: true },
      });
      const [track] = stream.getVideoTracks();
      imageCapture = new ImageCapture(track);
    } catch (err) {
      console.error(err);
    }
    const blob = await imageCapture.takePhoto();
    formData.append("img", blob, "img");

    this.viduSendFormatToVidu(formData);
    this.sessionStreamCheck();
  }
  viduSendFormatToVidu(formData) {
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
          } else if (result.code === 202) {
            this.ban();
          } else if (result.code === 504) {
            console.log("504에러");
          } else if (result.code === 522) {
            console.log("522에러");
          } else if (result.code === 553) {
            console.log("553에러");
          }
        }
      })
      .catch((err) => {
        console.log("ERR여기임");
      });
  }

  errorFound() {
    if (this.state.mode === "MODE2") {
      alert("졸음이 감지되었습니다.");
    } else if (this.state.mode === "MODE3") {
      alert("스마트폰이 감지되었습니다");
    } else {
      console.log("알수없는에러");
    }
  }
  ban() {
    alert("벌점이 과다로 추방되었습니다.");
    window.location.href = "../";
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
                <div id="session-title" className="headerTitle">
                  <h1>{mySessionId}</h1>
                </div>
                <div className="headerButtons">
                  <div className="btnTotal">
                    {this.state.videoState && (
                      <button onClick={this.videoHandlerOff} className="btns ">
                        Video OFF
                      </button>
                    )}
                    {!this.state.videoState && (
                      <button onClick={this.videoHandlerOn} className="btns ">
                        Video ON
                      </button>
                    )}
                    {this.state.audioState && (
                      <button onClick={this.audioHandlerOff} className="btns ">
                        Audio OFF
                      </button>
                    )}
                    {!this.state.audioState && (
                      <button onClick={this.audioHandlerOn} className="btns ">
                        Audio ON
                      </button>
                    )}
                    {this.state.screenShare && !this.state.isScreenShareNow && (
                      <button onClick={this.shareScreen} className="btns">
                        화면공유
                      </button>
                    )}
                    {this.state.screenShare && this.state.isScreenShareNow && (
                      <button onClick={this.shareScreenCancle} className="btns">
                        화면공유 취소
                      </button>
                    )}
                  </div>
                </div>
              </div>
              <div className="cams">
                <div className="myCams">
                  <div id="main-video" className="mainVideo">
                    {
                      //개인카메라
                      this.state.mainStreamManager !== undefined ? (
                        <>
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
                        </>
                      ) : null
                    }
                  </div>
                  <div id="video-container" className="subVideo">
                    {
                    this.state.publisher !== undefined &&
                    this.state.screenShareCameraNeeded ? (
                      <div className="stream-container col-md-6 col-xs-6">
                        <UserVideoComponent
                          streamManager={this.state.publisher}
                        />
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
                    {this.state.isRoomLeader ? (
                      <Link to="./RoomReform">
                        <button className="linksLi">방 수정</button>
                      </Link>
                    ) : (
                      ""
                    )}
                  </div>
                </div>
                <div className="AsideMain">
                  <div className="sideBoards">
                    <Routes>
                      <Route path="/" element={<MyStudy />} />
                      <Route path="/members" element={<Members />} />
                      <Route path="/RoomReform" element={<RoomReform />} />
                    </Routes>
                  </div>
                  <div id="chat-container" className="chatBoards">
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
                  {this.state.isRoomLeader ? (
                    <input
                      className="btn btn-large btn-danger btnR"
                      type="button"
                      id="buttonLeaveSession"
                      onClick={this.closeRoom}
                      value="방 닫기"
                    />
                  ) : (
                    ""
                  )}
                  <button className="btnR" onClick={() => this.toggleChat()}>
                    채팅
                  </button>
                </div>
              </div>
            </div>
          </div>
        )}
      </div>
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
          console.log("세션성공");
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

  getToken(newSessionId) {
    return this.createSession(newSessionId).then((sessionId) =>
      this.createToken(sessionId)
    );
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
          resolve(response.data.token);
        })
        .catch((error) => {
          console.log("생성실패");
          reject(error);
        });
    });
  }
}

export default RoomDetail;
