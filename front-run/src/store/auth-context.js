import React, { useState, useContext } from "react";
import { FetchUrl } from "./communication";
import getCookie from "../Cookie";

const AuthContext = React.createContext({
  userData: "",
  userDataGetter: () => {},
  isLoggedIn: false,
  login: () => {},
  logout: () => {},
});

export const AuthContextProvider = (props) => {
  const FETCH_URL = useContext(FetchUrl);

  let userIsLoggedIn = sessionStorage.hasOwnProperty("isLoggedIn")
    ? true
    : false;

  const [userData, setUserData] = useState(
    sessionStorage.hasOwnProperty("userData")
      ? JSON.parse(sessionStorage.getItem("userData"))
      : {}
  );
  const userDataGetter = (item) => {
    sessionStorage.setItem("userData", JSON.stringify(item));
    setUserData(item);
  };

  const loginHandler = () => {
    sessionStorage.setItem("isLoggedIn", 1); //1 true, 0 false
    userIsLoggedIn = true;
  };

  const logoutHandler = () => {
    sessionStorage.removeItem("isLoggedIn");
    sessionStorage.removeItem("userData");
    userIsLoggedIn = false;

    fetch(`${FETCH_URL}/members/auth/logout`, {
      method: "POST",
      headers: {
        accessToken: getCookie("accessToken"),
      },
    })
      .then((response) => response.json())
      .then((result) => {
        console.log(result);
      })
      .catch((err) => {
        console.log(err);
      });

    document.cookie =
      "accessToken" + "=; expires=Thu, 01 Jan 1999 00:00:10 GMT;";
    window.location.reload();
  };

  const contextValue = {
    userData: userData,
    userDataGetter: userDataGetter,
    isLoggedIn: userIsLoggedIn,
    login: loginHandler,
    logout: logoutHandler,
  };

  return (
    <AuthContext.Provider value={contextValue}>
      {props.children}
    </AuthContext.Provider>
  );
};

export default AuthContext;
