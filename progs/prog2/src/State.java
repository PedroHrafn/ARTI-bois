import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
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

    public Collection<String> availableMoves(int sizeX, int sizeY) {
        Collection<String> moves = new ArrayList<String>();

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
