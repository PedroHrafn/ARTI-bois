import java.util.Collection;
import java.util.ArrayList;

public class State {
    public int posX, posY;
    public char orientation;
    public int dirtsCleaned;
    char[][] grid;

    public Collection<String> availableMoves(int sizeX, int sizeY) {
        Collection<String> moves = new ArrayList<String>();
        if (grid[posX][posY] == 'D') {
            moves.add("SUCK");
        }

        // moves.add("GO");
        return moves;
    }

    public State execute(String move) {
        State tmp = new State();
        tmp.posX = this.posX;
        tmp.posY = this.posY;
        tmp.orientation = this.orientation;
        tmp.dirtsCleaned = this.dirtsCleaned;
        if (move.equals("GO")) {
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
            if (orientation == 'N') {
                tmp.orientation = 'W';
            } else if (orientation == 'E') {
                tmp.orientation = 'N';
            } else if (orientation == 'S') {
                tmp.orientation = 'E';
            } else {
                tmp.orientation = 'S';
            }
        } else if (move.equals("TURN_RIGHT")) {
            if (orientation == 'N') {
                tmp.orientation = 'E';
            } else if (orientation == 'E') {
                tmp.orientation = 'S';
            } else if (orientation == 'S') {
                tmp.orientation = 'W';
            } else {
                tmp.orientation = 'N';
            }
        } else if (move.equals("SUCK")) {
            tmp.dirtsCleaned++;
        }
        return tmp;
    }
}
