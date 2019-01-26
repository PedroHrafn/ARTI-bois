import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.PriorityQueue;
import java.util.Iterator;
import java.util.Random;
import java.util.Stack;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OurAgent implements Agent {
	private int posX, posY, sizeX, sizeY;
	private Stack<String> moves = new Stack<String>();
	private char[][] grid;
	private List<Position> dirt = new ArrayList<Position>();
	private List<Position> block = new ArrayList<Position>();
	private char orientation = ' ';
	int dirts = 0;

	/*
	 * init(Collection<String> percepts) is called once before you have to select
	 * the first action. Use it to find a plan. Store the plan and just execute it
	 * step by step in nextAction.
	 */

	public void init(Collection<String> percepts) {
		/*
		 * Possible percepts are: - "(SIZE x y)" denoting the size of the environment,
		 * where x,y are integers - "(HOME x y)" with x,y >= 1 denoting the initial
		 * position of the robot - "(ORIENTATION o)" with o in {"NORTH", "SOUTH",
		 * "EAST", "WEST"} denoting the initial orientation of the robot - "(AT o x y)"
		 * with o being "DIRT" or "OBSTACLE" denoting the position of a dirt or an
		 * obstacle Moving north increases the y coordinate and moving east increases
		 * the x coordinate of the robots position. The robot is turned off initially,
		 * so don't forget to turn it on.
		 */
		posX = posY = sizeX = sizeY = 0;

		constructGrid(percepts);
		// ATH: grid[col][row]
		grid = new char[sizeX][sizeY];
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				grid[x][y] = '0';
			}
		}

		// FIXME: MUNA AÐ TAKA ÚT FYRIR SKIL
		for (int i = 0; i < dirt.size(); i++) {
			System.out.println("dirt at: " + dirt.get(i));
		}

		for (int i = 0; i < block.size(); i++) {
			System.out.println("obstacle at: " + block.get(i));
			Position pos = block.get(i);
			grid[pos.x][pos.y] = 'X';
		}

		printGrid();
		floodFill(posX, posY);

		for (int i = 0; i < dirt.size(); i++) {
			Position pos = dirt.get(i);
			if (grid[pos.x][pos.y] != ' ') {
				// System.out.println("unreachable dirt at: " + pos.x + ", " + pos.y);
				dirt.remove(i);
			} else {
				grid[pos.x][pos.y] = 'd';
			}
		}
		dirts = dirt.size();
		grid[posX][posY] = orientation;
		// printGrid();
		long startTime = System.nanoTime();
		Node endNode = uniformSearchCost();
		long endTime = System.nanoTime();
		moves.push("TURN_OFF");
		while (endNode != null) {
			System.out.println();
			// printGrid(endNode.state.grid);
			moves.push(endNode.move);
			endNode = endNode.parent;
		}
		// System.out.println("search algorithm runtime in ms: " + (endTime - startTime)
		// / 1000000);
	}

	private void floodFill(int x, int y) {
		if (x < 0 || y < 0 || x >= sizeX || y >= sizeY)
			return;
		if (grid[x][y] == '0') {
			grid[x][y] = ' ';
			floodFill(x + 1, y);
			floodFill(x - 1, y);
			floodFill(x, y + 1);
			floodFill(x, y - 1);
		}
	}

	private void constructGrid(Collection<String> percepts) {
		Pattern perceptNamePattern = Pattern.compile("\\(\\s*([^\\s]+).*");

		for (String percept : percepts) {
			// System.out.println("current percept:" + percept);
			Matcher perceptNameMatcher = perceptNamePattern.matcher(percept);
			if (perceptNameMatcher.matches()) {
				String perceptName = perceptNameMatcher.group(1);
				if (perceptName.equals("HOME")) {
					Matcher m = Pattern.compile("\\(\\s*HOME\\s+([0-9]+)\\s+([0-9]+)\\s*\\)").matcher(percept);
					if (m.matches()) {
						posX = Integer.parseInt(m.group(1)) - 1;
						posY = Integer.parseInt(m.group(2)) - 1;
						// System.out.println("robot is at " + m.group(1) + "," + m.group(2));
					}
				} else if (perceptName.equals("SIZE")) {
					Matcher m = Pattern.compile("\\(\\s*SIZE\\s+([0-9]+)\\s+([0-9]+)\\s*\\)").matcher(percept);
					if (m.matches()) {
						sizeX = Integer.parseInt(m.group(1));
						sizeY = Integer.parseInt(m.group(2));
						// System.out.println("size of grid is " + m.group(1) + "," + m.group(2));
					}
				} else if (perceptName.equals("AT")) {
					Matcher m = Pattern.compile("\\(\\s*AT\\s+((DIRT)|(OBSTACLE))\\s+([0-9]+)\\s+([0-9]+)\\s*\\)")
							.matcher(percept);
					if (m.matches()) {
						if (m.group(1).equals("DIRT")) {
							Position pos = new Position(Integer.parseInt(m.group(4)) - 1,
									Integer.parseInt(m.group(5)) - 1);
							dirt.add(pos);
							// System.out.println("dirt at " + m.group(4) + "," + m.group(5));
						} else {
							Position pos = new Position(Integer.parseInt(m.group(4)) - 1,
									Integer.parseInt(m.group(5)) - 1);
							block.add(pos);
							// System.out.println("obstacle at " + m.group(4) + "," + m.group(5));
						}
					}
				} else if (perceptName.equals("ORIENTATION")) {
					orientation = percept.charAt(percept.indexOf(' ') + 1);
					// System.out.println("Orientation: " + orientation);
				} else {
					// System.out.println("other percept: " + percept);
				}
			} else {
				System.err.println("strange percept that does not match pattern: " + percept);
			}
		}
		// System.out.println("current position: " + posX + ", " + posY);
		// System.out.println("orientation: " + orientation);
		// System.out.println("size of grid: " + sizeX + ", " + sizeY);

	}

	public void printGrid() {
		for (int y = sizeY - 1; y >= 0; y--) {
			for (int x = 0; x < sizeX; x++) {
				System.out.print(grid[x][y]);
			}
			System.out.println();
		}
	}

	public void printGrid(char[][] ngrid) {
		for (int y = sizeY - 1; y >= 0; y--) {
			System.out.print(" ");
			for (int x = 0; x < sizeX; x++) {
				System.out.print(ngrid[x][y]);
			}
			System.out.println();
		}
	}

	public Node BFSearch() {
		ArrayDeque<Node> queue = new ArrayDeque<Node>();
		State rState = new State(posX, posY, orientation, grid, dirts);
		Node root = new Node(null, rState, "TURN_ON");
		queue.add(root);
		HashSet<String> visited = new HashSet<String>();
		while (!queue.isEmpty()) {
			Node curNode = queue.pop();
			State currState = curNode.state;
			if (currState.dirtsLeft == 0 && currState.posX == posX && currState.posY == posY) {
				System.out.println("found the end");
				return curNode;
			}
			// System.out.println(currState.dirtsLeft);
			if (!visited.contains(currState.getHash())) {
				visited.add(currState.getHash());
				// printGrid(currState.grid);
				for (String move : currState.availableMoves(sizeX, sizeY)) {
					// System.out.println(move);
					Node newNode = new Node(curNode, currState.execute(move), move);
					queue.add(newNode);
				}
			}
		}
		System.out.println("BFS FAILED");
		return new Node();
	}

	public Node uniformSearchCost() {
		// 30 is the initial capacity of the queue
		PriorityQueue<Node> queue = new PriorityQueue<Node>(30, new NodeComparator());
		HashSet<String> visited = new HashSet<String>();
		State rState = new State(posX, posY, orientation, grid, dirts);
		Node root = new Node(null, rState, "TURN_ON");
		Node endNode = new Node();

		queue.add(root);

		while (!queue.isEmpty()) {
			Node curNode = queue.poll();

			// If the rest of the queue has a greater cost than the
			// shortest current path, the scp is optimal
			if (endNode.cost != 0 && !queue.isEmpty() && queue.peek().cost > endNode.cost) {
				break;
			}

			State currState = curNode.state;
			if (currState.dirtsLeft == 0 && currState.posX == posX && currState.posY == posY) {
				System.out.println("Endnode cost: " + endNode.cost);
				// If this path is shorter return it instead.
				if (curNode.cost < endNode.cost || endNode.cost == 0)
					endNode = curNode;

				System.out.println("found the end");
			}

			if (!visited.contains(currState.getHash())) {
				visited.add(currState.getHash());
				for (String move : currState.availableMoves(sizeX, sizeY)) {
					Node newNode = new Node(curNode, currState.execute(move), move);
					queue.add(newNode);

				}
			}
		}
		System.out.println("USC FAILED");
		return endNode;
	}

	public void DFSRecurs(Node root, TreeSet<String> visited, Node endNode) {
		State currState = root.state;
		if (currState.dirtsLeft == 0 && currState.posX == posX && currState.posY == posY) {
			System.out.println("found the end");
			endNode.state = root.state;
			endNode.parent = root.parent;
			endNode.move = root.move;
			return;
		}
		if (!visited.contains(currState.getHash())) {
			visited.add(currState.getHash());
			// printGrid(currState.grid);
			for (String move : currState.availableMoves(sizeX, sizeY)) {
				// System.out.println(move);
				Node newNode = new Node(root, currState.execute(move), move);
				DFSRecurs(newNode, visited, endNode);
			}
		}
	}

	public Node DFSearch() {
		TreeSet<String> visited = new TreeSet<String>();
		State rState = new State(posX, posY, orientation, grid, dirts);
		Node root = new Node(null, rState, "TURN_ON");
		Node endNode = new Node(null, rState, "TURN_ON");
		DFSRecurs(root, visited, endNode);

		return endNode;
	}

	public String nextAction(Collection<String> percepts) {
		String ret = moves.pop();
		System.out.println(ret);
		return ret;
		/*
		 * System.out.print("perceiving:"); for (String percept : percepts) {
		 * System.out.print("'" + percept + "', "); } System.out.println(""); String[]
		 * actions = { "TURN_ON", "TURN_OFF", "TURN_RIGHT", "TURN_LEFT", "GO", "SUCK" };
		 * return actions[random.nextInt(actions.length)];
		 */
	}
}
