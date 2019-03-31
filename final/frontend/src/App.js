// React Components
import React, { Component } from "react";

import { Container, Row, Col } from 'react-bootstrap';

// Our components
import BigBoard from "./components/BigBoard/BigBoard";

// CSS

class App extends Component {
  render() {
    return (
      <Container fluid={true} style={{maxWidth: '800px'}}>
        <Row><Col><h2 style={{textAlign: 'center'}}>Ultimate Tic Tac Toe</h2></Col></Row>
        <BigBoard />
      </Container>
    );
  }
}

export default App;
