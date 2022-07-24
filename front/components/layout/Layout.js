import React, { Fragment } from "react";
import NavBar from "./NavBar";

function Layout(props) {
  return (
    <Fragment>
      <NavBar />
      <main>{props.children}</main>
    </Fragment>
  );
}

export default Layout;
