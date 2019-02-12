public class Node {

    public Node parent;
    public State state;
    public int cost;
    public String move;

    public Node(Node parent, State state, String move) {
        this.parent = parent;
        this.state = state;
        this.move = move;

        if (parent == null) {
            this.cost = 0;
        } else {
            this.cost = parent.cost + 1;
        }
    }

    public Node() {
        this.parent = null;
        this.state = null;
        this.move = "";
    }

    public String toString() {
        return "" + this.cost;
    }
}