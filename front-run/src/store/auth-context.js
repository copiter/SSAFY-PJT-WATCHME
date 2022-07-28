import React from "react";

const AuthContext = React.createContext({
  isLoggedIn: false,
  login: () => {},
  logout: () => {},
});

export const FetchUrl = React.createContext("https://43.200.6.20:81");

export const AuthContextProvider = (props) => {
  const userIsLoggedIn = !sessionStorage.hasOwnProperty("isLoggedIn")
    ? 0
    : sessionStorage.getItem("isLoggedIn");

  const loginHandler = () => {
    sessionStorage.setItem("isLoggedIn", 1); //1 true, 0 false
  };
  const logoutHandler = () => {
    sessionStorage.setItem("isLoggedIn", 0);
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
