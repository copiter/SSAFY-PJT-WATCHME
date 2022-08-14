import React, { useState, useContext } from "react";
import { FetchUrl } from "./communication";
import { getCookie } from "../Cookie";
import ErrorCode from "../Error/ErrorCode";

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

    if (!!getCookie("accessToken")) {
      fetch(`${FETCH_URL}/members/auth/logout`, {
        method: "POST",
        headers: {
          accessToken: getCookie("accessToken"),
        },
      })
        .then((response) => response.json())
        .then((result) => {
          if (result.code === 501) {
            ErrorCode(result);
          }
        })
        .catch((err) => {
          console.log(err);
        });

      document.cookie =
        "accessToken" + "=; expires=Thu, 01 Jan 1999 00:00:10 GMT;";
      window.location.reload();
    }
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
