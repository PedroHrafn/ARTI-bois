import java.util.Comparator;

public class NodeComparator implements Comparator<Node> {

    public int compare(Node a, Node b) {
        if (a.cost < b.cost)
            return -1;
        if (a.cost > b.cost)
            return 1;
        return 0;
    }
}