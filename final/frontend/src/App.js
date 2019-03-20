// React Components
import React, { Component } from 'react';

// Our components
import BigBoard from "./components/BigBoard/BigBoard";

// CSS
import './App.css';

class App extends Component {
  render() {
    return (
      <div className="container">
        <BigBoard />
      </div>
    );
  }
}

export default App;
