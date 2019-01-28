public class Node {

    public Node parent;
    public State state;
    public int cost;
    public String move;

    public Node(Node parent, State state, String move, boolean astar) {
        this.parent = parent;
        this.state = state;
        this.move = move;

        if (parent == null) {
            this.cost = 0;
        } else {
            this.cost = parent.cost + cost(astar, move, state);
        }
    }

    private int cost(boolean astar, String move, State state) {
        if (astar) {
            return 1 + heuristic(move);
        }
        return 1;
    }

    private int heuristic(String move) {
        return 0;
    }

    public Node() {
        this.parent = null;
        this.state = null;
        this.move = "";
        this.cost = 0;
    }

    public String toString() {
        return "" + this.cost;
    }
}