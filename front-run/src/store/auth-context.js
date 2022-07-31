import React from "react";

const AuthContext = React.createContext({
  isLoggedIn: false,
  login: () => {},
  logout: () => {},
});

export const AuthContextProvider = (props) => {
  let userIsLoggedIn = sessionStorage.hasOwnProperty("isLoggedIn")
    ? true
    : false;

  const loginHandler = () => {
    sessionStorage.setItem("isLoggedIn", 1); //1 true, 0 false
    userIsLoggedIn = true;
  };

  const logoutHandler = () => {
    sessionStorage.removeItem("isLoggedIn");
    userIsLoggedIn = false;
    document.cookie =
      "accessToken" + "=; expires=Thu, 01 Jan 1999 00:00:10 GMT;";
    window.location.reload();
  };

  const contextValue = {
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
