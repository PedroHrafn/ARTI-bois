import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OurAgent implements Agent {
	private Random random = new Random();

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
		Pattern perceptNamePattern = Pattern.compile("\\(\\s*([^\\s]+).*");
		int posX, posY, sizeX, sizeY;
		posX = posY = sizeX = sizeY = 0;
		String orientation = "";

		List<Position> dirt = new ArrayList<Position>();

		List<Position> block = new ArrayList<Position>();
		for (String percept : percepts) {
			System.out.println("current percept:" + percept);
			Matcher perceptNameMatcher = perceptNamePattern.matcher(percept);
			if (perceptNameMatcher.matches()) {
				String perceptName = perceptNameMatcher.group(1);
				if (perceptName.equals("HOME")) {
					Matcher m = Pattern.compile("\\(\\s*HOME\\s+([0-9]+)\\s+([0-9]+)\\s*\\)").matcher(percept);
					if (m.matches()) {
						posX = Integer.parseInt(m.group(1));
						posY = Integer.parseInt(m.group(2));
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
							Position pos = new Position(Integer.parseInt(m.group(4)) - 1, Integer.parseInt(m.group(5)) - 1);
							dirt.add(pos);
							// System.out.println("dirt at " + m.group(4) + "," + m.group(5));
						} else {
							Position pos = new Position(Integer.parseInt(m.group(4)) - 1, Integer.parseInt(m.group(5)) - 1);
							block.add(pos);
							// System.out.println("obstacle at " + m.group(4) + "," + m.group(5));
						}
					}
				} else if (perceptName.equals("ORIENTATION")) {
					orientation = percept.substring(percept.indexOf(' ') + 1, percept.indexOf(')'));
					// System.out.println("Orientation: " + orientation);
				} else {
					System.out.println("other percept: " + percept);
				}
			} else {
				System.err.println("strange percept that does not match pattern: " + percept);
			}
		}
		System.out.println("current position: " + posX + ", " + posY);
		System.out.println("orientation: " + orientation);
		System.out.println("size of grid: " + sizeX + ", " + sizeY);

		// ATH: grid[col][row]
		char[][] grid = new char[sizeX][sizeY];
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				grid[x][y] = '0';
			}
		}

		for (int i = 0; i < dirt.size(); i++) {
			System.out.println("dirt at: " + dirt.get(i));
		}
		for (int i = 0; i < block.size(); i++) {
			System.out.println("obstacle at: " + block.get(i));
			Position pos = block.get(i);
			grid[pos.x][pos.y] = 'X';
		}
		for (int y = sizeY - 1; y >= 0; y--) {
			for (int x = 0; x < sizeX; x++) {
				System.out.print(grid[x][y]);
			}
			System.out.println();
		}
		// TODO: perform flood fill algorithm on grid and mark reachable points as 'R'
		// TODO: loop dirts and check if it is reachable, remove if it isn't
	}

	public String nextAction(Collection<String> percepts) {
		System.out.print("perceiving:");
		for (String percept : percepts) {
			System.out.print("'" + percept + "', ");
		}
		System.out.println("");
		String[] actions = { "TURN_ON", "TURN_OFF", "TURN_RIGHT", "TURN_LEFT", "GO", "SUCK" };
		return actions[random.nextInt(actions.length)];
	}
}
