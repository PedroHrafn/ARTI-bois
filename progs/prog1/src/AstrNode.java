public class AstrNode {
    public Node parent;
    public State state;
    public int cost;
    public String move;

    public AstrNode(Node parent, State state, String move) {
        this.parent = parent;
        this.state = state;
        this.move = move;

        if (parent == null) {
            this.cost = 0;
        } else {
            this.cost = parent.cost + 1 + heuristic(move);
        }
    }

    private int heuristic(String move) {
        return 0;
    }

    public AstrNode() {
        this.parent = null;
        this.state = null;
        this.move = "";
        this.cost = 0;
    }

    public String toString() {
        return "" + this.cost;
    }
}