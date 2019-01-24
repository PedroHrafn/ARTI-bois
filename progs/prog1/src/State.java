import java.util.Collection;
import java.util.ArrayList;

public class State {
    public int posX, posY, dirtsLeft;
    public char orientation;
    char[][] grid;

    public State(int posX, int posY, char orientation, char[][] grid, int dirts) {
        this.posX = posX;
        this.posY = posY;
        this.orientation = orientation;
        this.grid = new char[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            System.arraycopy(grid[i], 0, this.grid[i], 0, grid[i].length);
        }
        this.dirtsLeft = dirts;
    }

    public State(char[][] grid) {
        this.grid = new char[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            System.arraycopy(grid[i], 0, this.grid[i], 0, grid[i].length);
        }
    }

    public Collection<String> availableMoves(int sizeX, int sizeY) {
        Collection<String> moves = new ArrayList<String>();
        if (grid[posX][posY] == 'D') {
            moves.add("SUCK");
        } else {
            moves.add("TURN_LEFT");
            moves.add("TURN_RIGHT");
            if (orientation == 'N' && posY != sizeY - 1 && grid[posX][posY + 1] != 'X') {
                moves.add("GO");
            } else if (orientation == 'E' && posX != sizeX - 1 && grid[posX + 1][posY] != 'X') {
                moves.add("GO");
            } else if (orientation == 'S' && posY != 0 && grid[posX][posY - 1] != 'X') {
                moves.add("GO");
            } else if (orientation == 'W' && posX != 0 && grid[posX - 1][posY] != 'X') {
                moves.add("GO");
            }
        }

        return moves;
    }

    public State execute(String move) {
        State tmp = new State(this.grid);
        tmp.posX = this.posX;
        tmp.posY = this.posY;
        tmp.orientation = this.orientation;
        tmp.dirtsLeft = this.dirtsLeft;
        if (move.equals("GO")) {
            tmp.grid[posX][posY] = ' ';
            if (orientation == 'N') {
                tmp.posY++;
            } else if (orientation == 'E') {
                tmp.posX++;
            } else if (orientation == 'S') {
                tmp.posY--;
            } else {
                tmp.posX--;
            }
        } else if (move.equals("TURN_LEFT")) {
            tmp.orientation = getOriention(false);
        } else if (move.equals("TURN_RIGHT")) {
            tmp.orientation = getOriention(true);
        } else if (move.equals("SUCK")) {
            tmp.grid[posX][posY] = ' ';
            tmp.dirtsLeft--;
            System.out.println(dirtsLeft);
        }
        if (tmp.grid[tmp.posX][tmp.posY] != 'D')
            tmp.grid[tmp.posX][tmp.posY] = tmp.orientation;
        return tmp;
    }

    private char getOriention(boolean clockwise) {
        if (orientation == 'N') {
            return clockwise ? 'E' : 'W';
        } else if (orientation == 'E') {
            return clockwise ? 'S' : 'N';
        } else if (orientation == 'S') {
            return clockwise ? 'W' : 'E';
        }
        return clockwise ? 'N' : 'S';
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
