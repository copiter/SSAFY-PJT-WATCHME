import { Routes, Route } from "react-router-dom";

//로그인관련
import Layout from "./components/layout/Layout";
import Login from "./components/ClientManagement/Login";
import SocialLogin from "./components/ClientManagement/SocialLogin";
import SignUp from "./components/ClientManagement/SignUp";
import FindID from "./components/ClientManagement/FindIDPWD/FindID";
import FindPWD from "./components/ClientManagement/FindIDPWD/FindPWD";
import PWDCheck from "./components/ClientManagement/ChangePWD/PWDCheck";
import ChangePWD from "./components/ClientManagement/ChangePWD/ChangePWD";

//공개미팅룸관련
import RoomRecruit from "./components/Room/RoomRecruit"; //룸탐색
import RoomCreate from "./components/Room/RoomCreate"; //룸생성
import RoomDetail from "./components/Room/WebRTC/RoomDetail"; //룸생성

//그룹관련
import GroupRecruit from "./components/Group/GroupRecruit"; //그룹탐색
import GroupCreate from "./components/Group/GroupCreate"; //그룹생성
import GroupReform from "./components/Group/GroupReform"; //그룹탐색
import GroupDetail from "./components/Group/GroupDetail"; //그룹정보

//스프린트 관련
import SprintCreate from "./components/Sprints/SprintCreate";

//MyPage관련
import MyPage from "./components/MyPage/MyPage";
import PointAdd from "./components/MyPage/Points/PointAdd";
import PointRefund from "./components/MyPage/Points/PointRefund";

//그외페이지
import MainPage from "./components/MainPage/MainPage";
import NotFound from "./pages/NotFound";
import About from "./components/ETC/About";

function App() {
  return (
    <Layout>
      <Routes>
        <Route path="/" element={<MainPage />} />
        <Route path="/login" element={<Login />} />
        <Route path="/slogin" element={<SocialLogin />} />
        <Route path="/signup" element={<SignUp />} />
        <Route path="/FindID" element={<FindID />} />
        <Route path="/FindPWD" element={<FindPWD />} />
        <Route path="/PWDCheck" element={<PWDCheck />} />
        <Route path="/ChangePWD" element={<ChangePWD />} />
        <Route path="/RoomRecruit" element={<RoomRecruit />} />
        <Route path="/RoomCreate" element={<RoomCreate />} />
        <Route path="/RoomDetail/:id" element={<RoomDetail />} />
        <Route path="/GroupCreate" element={<GroupCreate />} />
        <Route path="/GroupDetail/:id" element={<GroupDetail />} />
        <Route path="/GroupRecruit" element={<GroupRecruit />} />
        <Route path="/GroupReform/:id" element={<GroupReform />} />
        <Route path="/SprintCreate/:id" element={<SprintCreate />} />
        <Route path="/MyPage" element={<MyPage />} />
        <Route path="/PointAdd" element={<PointAdd />} />
        <Route path="/PointRefund" element={<PointRefund />} />
        <Route path="/About" element={<About />} />
        <Route path="/MyPage" element={<MyPage />} />
        <Route path="*" element={<NotFound />} />
      </Routes>
    </Layout>
  );
}

export default App;
