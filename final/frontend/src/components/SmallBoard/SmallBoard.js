// React Components
import React, { Component } from "react";

// CSS
import styles from "./SmallBoard.module.css";

class SmallBoard extends Component {
  render() {
    const { board, bigIndex, makeMove } = this.props;
    let smallBoard = board.map((rows, ri) => {
      return (
        <div key={ri} className={styles["row"]}>
          {rows.map((tile, i) => {
            let indexOfTile = 3 * ri + i;
            return (
              <div
                key={i}
                className={styles["tile"]}
                onClick={() => makeMove(bigIndex, indexOfTile)}
              >
                {tile}
              </div>
            );
          })}
        </div>
      );
    });

    return <div> {smallBoard} </div>;
  }
}

export default SmallBoard;
