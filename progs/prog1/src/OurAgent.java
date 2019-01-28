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
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OurAgent implements Agent {
	private int posX, posY, sizeX, sizeY;
	private Stack<String> moves;
	private char[][] grid;
	private CopyOnWriteArrayList<Position> dirtList;
	private List<Position> block;
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
		dirtList = new CopyOnWriteArrayList<Position>();
		block = new ArrayList<Position>();
		moves = new Stack<String>();

		constructGrid(percepts);
		// ATH: grid[col][row]
		grid = new char[sizeX][sizeY];
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				grid[x][y] = '0';
			}
		}

		for (int i = 0; i < dirtList.size(); i++) {
			System.out.println("dirt at: " + dirtList.get(i));
		}

		for (int i = 0; i < block.size(); i++) {
			System.out.println("obstacle at: " + block.get(i));
			Position pos = block.get(i);
			grid[pos.x][pos.y] = 'X';
		}

		floodFill(posX, posY);
		int DxMin = sizeX;
		int DxMax = 0;
		int DyMin = sizeY;
		int DyMax = 0;
		for (Position pos : dirtList) {
			if (grid[pos.x][pos.y] != ' ') {
				System.out.println("unreachable dirt at: " + pos.x + ", " + pos.y);
				dirtList.remove(pos);
			} else {
				if (pos.x > DxMax)
					DxMax = pos.x;
				if (pos.x < DxMin)
					DxMin = posX;
				if (pos.y > DyMax)
					DyMax = pos.y;
				if (pos.y < DyMin)
					DyMin = pos.y;
				grid[pos.x][pos.y] = 'd';
			}
		}

		dirts = dirtList.size();
		grid[posX][posY] = (grid[posX][posY] != 'd') ? orientation : Character.toLowerCase(orientation);
		printGrid();
		long startTime = System.nanoTime();
		Node endNode = DFSearch();
		// Node endNode = uniformSearchCost();
		// AstrNode endNode = AstrSearch(DxMin, DxMax, DyMin, DyMax, dirtList);
		long endTime = System.nanoTime();
		moves.push("TURN_OFF");
		while (endNode != null) {
			moves.push(endNode.move);
			endNode = endNode.parent;
		}
		System.out.println("search algorithm runtime in ms: " + (endTime - startTime) / 1000000);
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
			Matcher perceptNameMatcher = perceptNamePattern.matcher(percept);
			if (perceptNameMatcher.matches()) {
				String perceptName = perceptNameMatcher.group(1);
				if (perceptName.equals("HOME")) {
					Matcher m = Pattern.compile("\\(\\s*HOME\\s+([0-9]+)\\s+([0-9]+)\\s*\\)").matcher(percept);
					if (m.matches()) {
						posX = Integer.parseInt(m.group(1)) - 1;
						posY = Integer.parseInt(m.group(2)) - 1;
					}
				} else if (perceptName.equals("SIZE")) {
					Matcher m = Pattern.compile("\\(\\s*SIZE\\s+([0-9]+)\\s+([0-9]+)\\s*\\)").matcher(percept);
					if (m.matches()) {
						sizeX = Integer.parseInt(m.group(1));
						sizeY = Integer.parseInt(m.group(2));
					}
				} else if (perceptName.equals("AT")) {
					Matcher m = Pattern.compile("\\(\\s*AT\\s+((DIRT)|(OBSTACLE))\\s+([0-9]+)\\s+([0-9]+)\\s*\\)")
							.matcher(percept);
					if (m.matches()) {
						if (m.group(1).equals("DIRT")) {
							Position pos = new Position(Integer.parseInt(m.group(4)) - 1,
									Integer.parseInt(m.group(5)) - 1);
							dirtList.add(pos);
						} else {
							Position pos = new Position(Integer.parseInt(m.group(4)) - 1,
									Integer.parseInt(m.group(5)) - 1);
							block.add(pos);
						}
					}
				} else if (perceptName.equals("ORIENTATION")) {
					orientation = percept.charAt(percept.indexOf(' ') + 1);
				} else {
				}
			} else {
				System.err.println("strange percept that does not match pattern: " + percept);
			}
		}
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

			for (String move : currState.availableMoves(sizeX, sizeY)) {
				Node newNode = new Node(curNode, currState.execute(move), move);
				State newState = newNode.state;
				if (!visited.contains(newState.getHash())) {
					visited.add(newState.getHash());
					if (newState.dirtsLeft == 0 && newState.posX == posX && newState.posY == posY) {
						return newNode;
					}
					queue.add(newNode);
				}
			}

		}
		return new Node();
	}

	public AstrNode AstrSearch(int DxMin, int DxMax, int DyMin, int DyMax, CopyOnWriteArrayList<Position> dirtList) {
		PriorityQueue<AstrNode> queue = new PriorityQueue<AstrNode>(30, (a, b) -> a.value - b.value);
		State rState = new State(posX, posY, orientation, grid, dirts);
		AstrNode root = new AstrNode(null, rState, "TURN_ON", DxMin, DxMax, DyMin, DyMax, dirtList, posX, posY);
		queue.add(root);
		HashSet<String> visited = new HashSet<String>();
		while (!queue.isEmpty()) {
			AstrNode curNode = queue.poll();
			State currState = curNode.state;
			if (currState.dirtsLeft == 0 && currState.posX == posX && currState.posY == posY) {
				return curNode;
			}
			if (!visited.contains(currState.getHash())) {
				visited.add(currState.getHash());
				for (String move : currState.availableMoves(sizeX, sizeY)) {
					AstrNode newNode = new AstrNode(curNode, currState.execute(move), move, curNode.xMin, curNode.xMax,
							curNode.yMin, curNode.yMax, curNode.dirtList, posX, posY);
					queue.add(newNode);

				}
			}
		}
		return new AstrNode();
	}

	public Node uniformSearchCost() {
		PriorityQueue<Node> queue = new PriorityQueue<Node>(30, (a, b) -> a.cost - b.cost);
		State rState = new State(posX, posY, orientation, grid, dirts);
		Node root = new Node(null, rState, "TURN_ON");
		queue.add(root);
		HashSet<String> visited = new HashSet<String>();
		while (!queue.isEmpty()) {
			Node curNode = queue.poll();
			State currState = curNode.state;
			if (currState.dirtsLeft == 0 && currState.posX == posX && currState.posY == posY) {
				return curNode;
			}
			if (!visited.contains(currState.getHash())) {
				visited.add(currState.getHash());
				for (String move : currState.availableMoves(sizeX, sizeY)) {
					Node newNode = new Node(curNode, currState.execute(move), move);
					queue.add(newNode);

				}
			}
		}
		return new Node();
	}

	public void DFSRecurs(Node root, TreeSet<String> visited, Node endNode) {
		State currState = root.state;
		if (currState.dirtsLeft == 0 && currState.posX == posX && currState.posY == posY) {
			endNode.state = root.state;
			endNode.parent = root.parent;
			endNode.move = root.move;
			return;
		}
		if (!visited.contains(currState.getHash())) {
			visited.add(currState.getHash());
			for (String move : currState.availableMoves(sizeX, sizeY)) {
				Node newNode = new Node(root, currState.execute(move), move);
				DFSRecurs(newNode, visited, endNode);
			}
		}
	}
	// public void DFSRecurs(Node root, TreeSet<String> visited, Node endNode) {
	// State currState = root.state;

	// // printGrid(currState.grid);
	// for (String move : currState.availableMoves(sizeX, sizeY)) {
	// // System.out.println(move);
	// Node newNode = new Node(root, currState.execute(move), move, false);
	// if (!visited.contains(newNode.state.getHash())) {
	// if (newNode.state.dirtsLeft == 0 && newNode.state.posX == posX &&
	// newNode.state.posY == posY) {
	// endNode.state = newNode.state;
	// endNode.parent = newNode.parent;
	// endNode.move = newNode.move;
	// return;
	// }
	// visited.add(newNode.state.getHash());
	// DFSRecurs(newNode, visited, endNode);
	// }
	// }
	// }

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
		return ret;
	}
}
