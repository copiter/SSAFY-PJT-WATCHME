import React, { useState, useContext } from "react";
import { FetchUrl } from "./communication";
import { getCookie, removeCookie } from "../Cookie";
import swal from "sweetalert";

const AuthContext = React.createContext({
  userData: "",
  userDataGetter: () => {},
  logout: () => {},
});

export const AuthContextProvider = (props) => {
  const FETCH_URL = useContext(FetchUrl);

  const [userData, setUserData] = useState(
    sessionStorage.hasOwnProperty("userData")
      ? JSON.parse(sessionStorage.getItem("userData"))
      : {}
  );
  const userDataGetter = (item) => {
    sessionStorage.setItem("userData", JSON.stringify(item));
    setUserData(item);
  };

  const logoutHandler = () => {
    sessionStorage.removeItem("userData");
    fetch(`${FETCH_URL}/members/auth/logout`, {
      method: "POST",
      headers: {
        accessToken: getCookie("accessToken"),
      },
    })
      .then((response) => response.json())
      .then((result) => {
        if (result.code === 200) {
          swal("로그아웃 되었습니다!", "", "success");
          setInterval(() => {
            removeCookie("accessToken");
            window.location.replace(window.location.origin);
          }, [1000]);
        }
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const contextValue = {
    userData: userData,
    userDataGetter: userDataGetter,
    logout: logoutHandler,
  };

  return (
    <AuthContext.Provider value={contextValue}>
      {props.children}
    </AuthContext.Provider>
  );
};

export default AuthContext;
