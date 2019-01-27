public class Node {

    public Node parent;
    public State state;
    public int cost;
    public String move;

    public Node(Node parent, State state, String move, boolean astar) {
        this.parent = parent;
        this.state = state;
        this.move = move;

        // TODO: Alter cost so that suck + 5, go +1 and so on.
        int cost = 1;
        if (parent == null) {
            this.cost = 0;
        } else {
            this.cost = astar ? parent.cost + cost + heuristic() : parent.cost + cost;
        }
    }

    private int heuristic() {
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