import java.util.Collection;
import java.util.ArrayList;

public class State {
    public int xSize, ySize;
    char[][] grid;

    public State(int xSize, int ySize, char[][] grid) {
        this.xSize = xSize;
        this.ySize = ySize;
        for (int i = 0; i < grid.length; i++) {
            System.arraycopy(grid[i], 0, this.grid[i], 0, grid[i].length);
        }
    }

    public Collection<String> availableMoves(int sizeX, int sizeY) {
        Collection<String> moves = new ArrayList<String>();

        return moves;
    }

    public State execute(String move) {
        State tmp = new State(this.grid);

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
