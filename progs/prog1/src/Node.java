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
    }

    @Override
    public int compare(Node node1, Node node2)
    {
        if (node1.cost < node2.cost)
            return -1;
        if (node1.cost > node2.cost)
            return 1;
        if (node1.node < node2.node)
            return -1;
        return 0;
    }
}