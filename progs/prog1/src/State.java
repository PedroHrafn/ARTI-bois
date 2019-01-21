import java.util.Collection;
import java.util.ArrayList;

public class State {
    public int posX, posY;
    public boolean on;
    public String orientation;
    public int dirtsCleaned;

    public Collection<String> availableMoves(int sizeX, int sizeY) {
        Collection<String> moves = new ArrayList<String>();
        if (!on) {
            moves.add("TURN_ON");
            return moves;
        }
        moves.add("GO");
        return moves;
    }

    public State execute(String move) {
        State tmp = new State();
        tmp.posX = this.posX;
        tmp.posY = this.posY;
        tmp.on = this.on;
        tmp.orientation = this.orientation;
        tmp.dirtsCleaned = this.dirtsCleaned;
        if (move.equals("TURN_ON")) {
            tmp.on = true;
        } else if (move.equals("GO")) {
            if (orientation.equals("NORTH")) {
                tmp.posY++;
            } else if (orientation.equals("EAST")) {
                tmp.posX++;
            } else if (orientation.equals("SOUTH")) {
                tmp.posY--;
            } else {
                tmp.posX--;
            }
        } else if (move.equals("TURN_LEFT")) {
            if (orientation.equals("NORTH")) {
                tmp.orientation = "WEST";
            } else if (orientation.equals("EAST")) {
                tmp.orientation = "NORTH";
            } else if (orientation.equals("SOUTH")) {
                tmp.orientation = "EAST";
            } else {
                tmp.orientation = "SOUTH";
            }
        } else if (move.equals("TURN_RIGHT")) {
            if (orientation.equals("NORTH")) {
                tmp.orientation = "EAST";
            } else if (orientation.equals("EAST")) {
                tmp.orientation = "SOUTH";
            } else if (orientation.equals("SOUTH")) {
                tmp.orientation = "WEST";
            } else {
                tmp.orientation = "NORTH";
            }
        } else if (move.equals("SUCK")) {
            dirtsCleaned++;
        } else {
            tmp.on = false;
        }
        return tmp;
    }
}
