// React Components
import React, { Component } from "react";

// Our components
import BigBoard from "./components/BigBoard/BigBoard";

// CSS
import styles from "./App.module.css";

class App extends Component {
  render() {
    return (
      <div className={styles["container"]}>
        <h2>Ultimate Tic Tac Toe</h2>
        <BigBoard />
      </div>
    );
  }
}

export default App;
