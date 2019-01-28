import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AstrNode {
    public AstrNode parent;
    public State state;
    public int cost;
    public String move;
    public int xMin;
    public int xMax;
    public int yMin;
    public int yMax;
    public CopyOnWriteArrayList<Position> dirtList;
    public int value;

    public AstrNode(AstrNode parent, State state, String move, int xMin, int xMax, int yMin, int yMax,
            CopyOnWriteArrayList<Position> dirtList, int startX, int startY) {
        this.parent = parent;
        this.state = state;
        this.move = move;
        this.dirtList = new CopyOnWriteArrayList<Position>(dirtList);
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        if (move == "SUCK") {
            suck();
        }
        if (parent == null) {
            this.cost = 0;
        } else {
            this.cost = parent.cost + 1;
        }
        this.value = heuristic(startX, startY) * 2 + cost;
        // System.out.println(value);
    }

    private void suck() {
        for (Position dirt : dirtList) {
            if (dirt.y == this.state.posY && dirt.x == this.state.posX) {
                this.dirtList.remove(dirt);
            }
        }
        if (this.state.posX == xMin) {
            xMin = xMax;
            for (Position dirt : dirtList) {
                if (dirt.x < xMin)
                    xMin = dirt.x;
            }
        } else if (this.state.posX == xMax) {
            xMax = xMin;
            for (Position dirt : dirtList) {
                if (dirt.x > xMax)
                    xMax = dirt.x;
            }
        }

        if (this.state.posY == yMin) {
            yMin = yMax;
            for (Position dirt : dirtList) {
                if (dirt.y < yMin)
                    yMin = dirt.y;
            }
        } else if (this.state.posY == yMax) {
            yMax = yMin;
            for (Position dirt : dirtList) {
                if (dirt.y > yMax)
                    yMax = dirt.y;
            }
        }
    }

    private int heuristic(int startX, int startY) {
        int dirtsLeft = state.dirtsLeft;
        if (dirtsLeft != 0) {
            int squareSize = (xMax - xMin + yMax - yMin + 2) << 1;
            int distTo = 0;
            if (xMin > state.posX)
                distTo += xMin - state.posX;
            else if (xMax < state.posX)
                distTo += state.posX - xMax;
            if (yMin > state.posY) {
                if (distTo != 0)
                    distTo++;
                distTo += yMin - state.posY;
            } else if (yMax < state.posY) {
                if (distTo != 0)
                    distTo++;
                distTo += state.posY - yMax;
            }
            int distFrom = 0;
            if (xMin > startX)
                distFrom += xMin - startX;
            else if (xMax < startX)
                distFrom += startX - xMax;
            if (yMin > startY) {
                if (distFrom != 0)
                    distFrom++;
                distFrom += yMin - startY;
            } else if (yMax < startY) {
                if (distFrom != 0)
                    distFrom++;
                distFrom += startY - yMax;
            }
            return squareSize + distTo + distFrom + dirtsLeft * 2;
        }
        return Math.abs(state.posX - startX) + Math.abs(state.posY - startY);
    }

    public AstrNode() {
        parent = null;
        this.state = null;
        this.move = "";
        this.cost = 0;
    }

    public int getValue(int startX, int startY) {
        return this.cost + this.value;
    }

    public String toString() {
        return "" + this.cost;
    }
}