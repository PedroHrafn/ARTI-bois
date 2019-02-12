import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import javafx.util.Pair;

import java.awt.Point;

public class State {
    public Environment env;
    public CopyOnWriteArrayList<Point> whites;
    public CopyOnWriteArrayList<Point> blacks;

    public State(Environment env, CopyOnWriteArrayList<Point> whites, CopyOnWriteArrayList<Point> blacks) {
        this.env = env;
        this.whites = new CopyOnWriteArrayList<Point>(whites);
        this.blacks = new CopyOnWriteArrayList<Point>(blacks);
    }

    public Collection<Pair<Point, Point>> availableMoves(boolean whiteTurn) {
        Collection<String> moves = new ArrayList<String>();
        if (whiteTurn) {
            for (Point from : whites) {
                // TODO: check if from.y + 1 is empty

                if (from.x != 1) {
                    // check if there is black at (from.x - 1, from.y + 1)
                }
                if (from.x != env.width) {
                    // check if there is black at (from.x - 1)
                }
            }
        }
        return moves;
    }

    public State execute(String move) {
        State tmp = new State(this.xSize, this.ySize, this.grid);

        return tmp;
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
