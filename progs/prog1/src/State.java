import java.util.Collection;
import java.util.ArrayList;

public class State
{
    public int posX, posY;
    public boolean on;
    public String orientation;
    public int dirtsCleaned; 

    public Collection<String> availableMoves() {
        Collection<String> moves = new ArrayList<String>();
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


        return tmp;
    }
}
