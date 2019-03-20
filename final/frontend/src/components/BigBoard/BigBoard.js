// React components
import React, { Component } from "react";

// Third Party Components
import { CircularProgress } from "@material-ui/core";

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
      fetched: false
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
    // TODO: test this
    fetch("http://localhost:5000/move", {
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
          console.log(resp);
          this.setState({ ...resp });
        }
      });
  }
  render() {
    const { fetched, board, winnerBoard } = this.state;
    let bigBoard = board.map((row, ri) => {
      return (
        <div key={ri} className={styles["big-row"]}>
          {/* For each row in big board */}
          {row.map((smallBoard, i) => {
            // 3 for the size of the board
            let indexOfCell = 3 * ri + i;
            if (!winnerBoard[i][indexOfCell]) {
              return (
                <SmallBoard
                  key={i}
                  board={smallBoard}
                  bigIndex={indexOfCell}
                  makeMove={this.move.bind(this)}
                />
              );
            }
            return <div> {winnerBoard[indexOfCell]} </div>;
          })}
        </div>
      );
    });

    if (!fetched) {
      return <CircularProgress />;
    }
    return <div className={styles["container"]}> {bigBoard} </div>;
  }
}

export default BigBoard;