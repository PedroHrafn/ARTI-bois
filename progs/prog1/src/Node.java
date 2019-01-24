public class Node {

    private Node parent;
    private State state;
    private int cost;
    private String move;
	
	public Node(Node parent, State state, String move) {
        this.parent = parent;
        this.state = state;
        this.move = move;

        if(parent == null) {
            this.cost = 0;
        } else {
            this.cost = parent.cost +1;
        }
    }
    
}