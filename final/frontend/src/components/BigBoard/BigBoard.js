// React components
import React, { Component } from "react";

// Third Party Components
import { CircularProgress } from "@material-ui/core";
import { Row, Col, Button, Modal } from 'react-bootstrap';

// Our components
import SmallBoard from "../SmallBoard/SmallBoard";

// CSS
import styles from "./BigBoard.module.css";

class BigBoard extends Component {
  constructor(props) {
    super(props);
    this.state = {
      board: [],
      winnersBoard: [],
      fetched: false,
      nextBig: [],
      xDoing: true,
      showModal: false,
    };
  }

  componentDidMount() {
    fetch("http://localhost:5000/board", {
      method: "GET",
      headers: {
        Accept: "application/json"
      }
    })
      .then(json => json.json())
      .then(resp => {
        this.setState({
          ...resp,
          fetched: true
        });
      })
      .catch(err => console.error(err));
  }

  move(big, small) {
    fetch("http://localhost:5000/board/move", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        small: small,
        big: big
      })
    })
      .then(res => {
        if (res.status === 200) {
          return res.json();
        } else {
          return { notOk: true, data: {} };
        }
      })
      .then(resp => {
        if (!resp.notOk) {
          this.setState({ ...resp, xDoing: !this.state.xDoing });
          this.agentMove()
        }
      });
  }

  agentMove() {
    fetch("http://localhost:5000/agent/move", {
      method: "GET",
      headers: {
        "Accept": "application/json"
      },
    })
      .then(res => {
        if (res.status === 200) {
          return res.json();
        } else {
          return { notOk: true, data: {} };
        }
      })
      .then(resp => {
        if (!resp.notOk) {
          this.setState({ ...resp, xDoing: !this.state.xDoing });
        }
      });
  }

  reset() {
    this.setState({showModal: false})
    fetch("http://localhost:5000/board/reset", {
      method: "GET",
      headers: {
        Accept: "application/json"
      }
    })
      .then(json => json.json())
      .then(resp => {
        this.setState({
          ...resp, xDoing: true
        });
      })
      .catch(err => console.error(err));
  }

  render() {
    const { fetched, board, winnerBoard, nextBig, xDoing } = this.state;
    let bigBoard = board.map((row, ri) => {
      // console.log(ri);
      return (
        <Row key={ri}>
          {/* For each row in big board */}
          {row.map((smallBoard, i) => {
            // 3 for the size of the board
            let indexOfCell = 3 * ri + i;
            var nextField = false;
            console.log(nextBig);
            if ((nextBig[1] === i && nextBig[0] === ri) || nextBig.length === 0) { 
              nextField = true 
            }
            if (!winnerBoard[ri][i]) {
              // console.log(i);
              return (
                <Col key={i} style={nextField ? { border: '2px solid red', padding: 0, margin: '8px' } : { padding: 0, margin: '10px' }}>
                  <SmallBoard
                    board={smallBoard}
                    bigIndex={indexOfCell}
                    makeMove={this.move.bind(this)}
                    nextField={nextField}
                  />
                </Col>
              );
            }
            return (
              <Col className={styles["big-win"]}>
                {winnerBoard[ri][i]}
              </Col>
            );
          })}
        </Row>
      );
    });

    if (!fetched) {
      return <CircularProgress />;
    }
    return (
      <div>
        <Modal
          show={this.state.showModal}
          onHide={() => this.reset()}
          dialogClassName="modal-122w"
          aria-labelledby="example-custom-modal-styling-title"
        >
          <Modal.Header closeButton>
            <Modal.Title id="example-custom-modal-styling-title">Game Over</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <h4>LEIK LOKIÐ</h4>
            <p>Hver vann?</p>
          </Modal.Body>
          <Modal.Footer>
            <Button onClick={() => this.reset()}>Play Again</Button>
          </Modal.Footer>
        </Modal>
        <Row>
          <Col>
            <h2 style={{ textAlign: 'center' }}>
              {xDoing ? "X has turn" : "O has turn"}
            </h2>
          </Col>
        </Row>

        {bigBoard}

        <Row className="justify-content-center">
          <Col xs="auto"><Button onClick={() => this.reset()}>Reset Game</Button></Col>
        </Row>
      </div>
    );
  }
}

export default BigBoard;
