import java.util.Collection;
import java.util.List;
import java.util.PriorityQueue;
import java.util.ArrayList;

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
        this.whitePawns = grid.length * 2;
        this.blackPawns = grid.length * 2;
        this.grid = new char[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            System.arraycopy(grid[i], 0, this.grid[i], 0, grid[i].length);
        }
    }

    public List<int[]> availableMoves(boolean weWhite) {
        // Priority Queue that orders the moves after how far the pawns go
        List<int[]> moves = weWhite ? findWhiteMove() : findBlackMove();


       
        // if moves is empty then its a draw
        if (moves.isEmpty()) {
            this.isTerminal = true;
        }
        // for (int[] move : moves) {
        //     System.out.println();
       // int[] move = moves.get(0);
        // System.out.println("FIRST MOVE");
        // for (int i = 0; i < 4; i++) {
        //     System.out.print((move[i] + 1) + ", ");
        // }
        // System.out.println();
        // }
        return moves;
    }

    private List<int[]> findWhiteMove() {
        List<int[]> moves = new ArrayList<int[]>();

        int forward = 1;
        char friendly ='W';
        char opponent = 'B';

        // Check if there is anyone on the border to winning
        int border = 0;
        for (int x = 0; x < grid.length - 1; x++) {
            if(grid[x][border] == friendly) {
                if(x > 0) {
                    if(grid[x - 1 ][border + forward] == opponent) {
                        moves.add(new int[] {x, border, x - 1, border + forward});
                        return moves;
                    }
                } else if(x < grid.length ) {
                    if(grid[x - 1 ][border + forward] == opponent) {
                        moves.add(new int[] {x, border, x + 1, border + forward});
                        return moves;
                    }
                }
            }
        }
    
        for (int y = grid[0].length - 2; y >= 0; y--){
            for (int x = 0; x < grid.length - 1; x++) {
                char tileFront = grid[x][y + forward];
                if (grid[x][y] == friendly) {
                    if (x > 0) {
                        char tileLeft = grid[x - 1][y + forward];
                        // Can we kill an opponent not next to our border?
                        if (tileLeft== opponent && y != 0) {
                            moves.add(new int[] { x, y, x - 1, y + forward });
                        } else if(tileFront != opponent ) { // Is there an opponent in front of me, that I am stopping
                            // Can we win by killing? 
                            if(tileLeft== opponent && y + forward == grid.length - 1){
                                moves.add(0, new int[] { x, y, x - 1, y + forward });
                                return moves;
                            }else if(tileLeft == opponent) { // If there is an opponent next to the border kill him immediately
                                moves.add(0, new int[] { x, y, x - 1, y + forward });
                                return moves;
                            }
                        }
                    }
                    if (x < grid.length) {
                        char tileRight = grid[x + 1][y + forward];
                        // Can we kill an opponent not next to our border?
                        if (tileRight == opponent && y != 0) {
                            moves.add(new int[] { x, y, x + 1, y + forward });
                        } else if(tileFront != opponent && y != 0) { // Is there an opponent in front of me, that I am stopping
                            if(tileRight == opponent && y + forward == grid.length - 1){
                                moves.add(0, new int[] { x, y, x + 1, y + forward });
                                return moves;
                            }else if(tileRight == opponent) { // If there is an opponent next to the border kill him immediately
                                moves.add(0, new int[] { x, y, x + 1, y + forward });
                                return moves;
                            }
                        }
                    }
                    // Can we go forward?
                    if (tileFront == ' ') {
                        moves.add(new int[] { x, y, x, y + forward });
                        // If we are one move from winning, we go for it
                        if(y == grid.length - 2) {
                            return moves;
                        }
                    }
                }
            }
        }
        return moves;
    }

    private List<int[]> findBlackMove() {
        List<int[]> moves = new ArrayList<int[]>();

        int forward = -1;
        char friendly = 'B';
        char opponent = 'W';

        for (int y = 0; y < grid[0].length; y++) {
            for (int x = 0; x < grid.length; x++) {
                char tileFront = grid[x][y + forward];
                if (grid[x][y] == friendly) {
                    if (x > 0) {
                        char tileLeft = grid[x - 1][y + forward];
                        // Can we kill an opponent not next to our border?
                        if (tileLeft== opponent && y != grid[0].length - 1) {
                            moves.add(new int[] { x, y, x - 1, y + forward });
                        } else if(tileFront != opponent ) { // Is there an opponent in front of me, that I am stopping
                            // Can we win by killing? 
                            if(tileLeft== opponent && y + forward == 0){
                                moves.add(0, new int[] { x, y, x - 1, y + forward });
                                return moves;
                            }else if(tileLeft == opponent) { // If there is an opponent next to the border kill him immediately
                                moves.add(0, new int[] { x, y, x - 1, y + forward });
                                return moves;
                            }
                        }
                    }
                    if (x < grid.length) {
                        char tileRight = grid[x + 1][y + forward];
                        // Can we kill an opponent not next to our border?
                        if (tileRight == opponent && y != grid[0].length) {
                            moves.add(new int[] { x, y, x + 1, y + forward });
                        } else if(tileFront != opponent && y != 0) { // Is there an opponent in front of me, that I am stopping
                            if(tileRight == opponent && y + forward == 0){
                                moves.add(0, new int[] { x, y, x + 1, y + forward });
                                return moves;
                            }else if(tileRight == opponent) { // If there is an opponent next to the border kill him immediately
                                moves.add(0, new int[] { x, y, x + 1, y + forward });
                                return moves;
                            }
                        }
                    }
                    // Can we go forward?
                    if (tileFront == ' ') {
                        moves.add(new int[] { x, y, x, y + forward });
                        // If we are one move from winning, we go for it
                        if(y == 1) {
                            return moves;
                        }
                    }
                }
            }
        }
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
