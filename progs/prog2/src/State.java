import java.util.Collection;
import java.util.List;
import java.util.ArrayList;


import java.awt.Point;

public class State {
    public char[][] grid;
    public boolean whiteTurn;
    public boolean isTerminal;
    public char winner;
    public int whitePawns;
    public int blackPawns;

    public State(char[][] grid, boolean whiteTurn) {
        this.isTerminal = false;
        this.winner = 'd';
        this.whiteTurn = whiteTurn;
        System.out.println(grid.length);
        this.whitePawns = grid.length * 2;
        this.blackPawns = grid.length * 2;
        this.grid = new char[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            System.arraycopy(grid[i], 0, this.grid[i], 0, grid[i].length);
        }
    }

    public List<int[]> availableMoves() {
        List<int[]> moves = new ArrayList<int[]>();
        int forward = whiteTurn ? 1 : -1;
        char friendly = whiteTurn ? 'W' : 'B';
        char opponent = whiteTurn ? 'B' : 'W';

        // TODO: implement for black
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                System.out.print(grid[x][y]);
                if (grid[x][y] == friendly) {
                    if (x != 0) {
                        if (grid[x - 1][y + forward] == opponent) {
                            moves.add(new int[] { x, y, x - 1, y + forward });
                        }
                    }
                    if (x != grid.length - 1) {
                        if (grid[x + 1][y + forward] == opponent) {
                            moves.add(new int[] { x, y, x + 1, y + forward });
                        }
                    }
                    if (grid[x][y + forward] == ' ') {
                        moves.add(new int[] { x, y, x, y + forward });
                    }
                }
            }
            System.out.println();
        }
        // if moves is empty then its a draw
        if (moves.isEmpty()) {
            this.isTerminal = true;
        }
        // for (int[] move : moves) {
        //     System.out.println();
        //     for (int i = 0; i < 4; i++) {
        //         System.out.print(move[i] + ", ");
        //     }
        //     System.out.println();
        // }
        return moves;
    }

    public State nextState(int[] move) {
        State newState = new State(this.grid, !this.whiteTurn);
        char victim = newState.grid[move[2]][move[3]];
        newState.grid[move[2]][move[3]] = grid[move[0]][move[1]];
        newState.grid[move[0]][move[1]] = ' ';
        // if move[3] == 0 then black wins, if its Y max then white wins
        if (move[3] == 0) {
            newState.isTerminal = true;
            newState.winner = 'B';
        } else if (move[3] == grid[0].length - 1) {
            newState.isTerminal = true;
            newState.winner = 'W';
        }
        else if (victim == 'W')
        {
            newState.whitePawns --;
        }
        else if (victim == 'B')
        { 
            newState.blackPawns --;
        }
        return newState;
    }

    public String getHash() {
        String hash = "";
        for (int y = grid[0].length - 1; y >= 0; y--) {
            for (int x = 0; x < grid.length; x++) {
                hash += grid[x][y];
            }
        }
        return hash;
    }
}
