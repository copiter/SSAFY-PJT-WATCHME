import React from "react";

const AuthContext = React.createContext({
  isLoggedIn: false,
  login: () => {},
  logout: () => {},
});

export const AuthContextProvider = (props) => {
  const userIsLoggedIn = !localStorage.hasOwnProperty("isLoggedIn")
    ? 0
    : localStorage.getItem("isLoggedIn");

  const loginHandler = () => {
    localStorage.setItem("isLoggedIn", 1); //1 true, 0 false
  };
  const logoutHandler = () => {
    localStorage.setItem("isLoggedIn", 0);
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
