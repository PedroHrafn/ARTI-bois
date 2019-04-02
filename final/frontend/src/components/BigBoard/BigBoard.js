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
      winner: "",
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
        console.log(resp.winner)
        if (!resp.notOk) {
          this.setState({ ...resp, xDoing: !this.state.xDoing });
          this.agentMove();
          if (resp.winner === 'X' || resp.winner === 'O' || resp.winner === 'D') {
            console.log(resp.winner);
            this.setState({ showModal: true })
          }
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
        console.log(resp.winner)
        if (!resp.notOk) {
          this.setState({ ...resp, xDoing: !this.state.xDoing });
          if (resp.winner === 'X' || resp.winner === 'O' || resp.winner === 'D') {
            console.log(resp.winner);
            this.setState({ showModal: true })
          }
        }
      });
  }

  reset() {
    this.setState({ showModal: false })
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
    const { fetched, board, winnerBoard, nextBig, xDoing, showModal, winner } = this.state;
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
              <Col key={i} className={styles["big-win"]}>
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
          show={showModal}
          onHide={() => this.reset()}
          dialogClassName="modal-122w"
          aria-labelledby="example-custom-modal-styling-title"
        >
          <Modal.Header closeButton>
            <Modal.Title id="example-custom-modal-styling-title">Game Over</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <h4>{winner === 'D' ? 'Draw' : 'Winner is ' + winner}</h4>
          </Modal.Body>
          <Modal.Footer>
            <Button onClick={() => this.reset()}>Play Again</Button>
          </Modal.Footer>
        </Modal>
        <Row>
          <Col>
            <h4 style={{ textAlign: 'center' }}>
              {xDoing ? "X has turn" : "O has turn"}
            </h4>
            <p style={{ textAlign: 'center' }}>
              {!xDoing ? "Waiting for skynet to process next move" : "Your move"}
            </p>
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
