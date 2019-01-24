import java.util.Collection;
import java.util.ArrayList;

public class State {
    public int posX, posY;
    public char orientation;
    public int dirtsCleaned;
    char[][] grid;

    public State(int posX, int posY, char orientation, char[][] grid)
    {
        this.posX = posX;
        this.posY = posY;
        this.orientation = orientation;
        this.grid = grid;
    }

    public State() {

    }

    public Collection<String> availableMoves(int sizeX, int sizeY) {
        Collection<String> moves = new ArrayList<String>();
        if (grid[posX][posY] == 'D') {
            moves.add("SUCK");
        }

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
            tmp.orientation = getOriention(false);
        } else if (move.equals("TURN_RIGHT")) {
            tmp.orientation = getOriention(true);
        } else if (move.equals("SUCK")) {
            tmp.dirtsCleaned++;
        }
        return tmp;
    }

    private char getOriention(boolean right) {
        if (orientation == 'N') {
            return right ? 'E' : 'W';
        } else if (orientation == 'E') {
            return right ? 'S' : 'N';
        } else if (orientation == 'S') {
            return right ? 'W' : 'E';
        }
        return right ? 'N' : 'S';
    }
}
