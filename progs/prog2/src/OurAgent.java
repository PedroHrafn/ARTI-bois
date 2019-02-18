import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.awt.Point;
import java.util.List;

public class OurAgent implements Agent {
	private Random random = new Random();

	private String role; // the name of this agent's role (white or black)
	private int playclock; // this is how much time (in seconds) we have before nextAction needs to return
							// a move
	private long currTime;
	private boolean myTurn; // whether it is this agent's turn or not
	private int width, height; // dimensions of the board

	private State state;

	/*
	 * init(String role, int playclock) is called once before you have to select the
	 * first action. Use it to initialize the agent. role is either "white" or
	 * "black" and playclock is the number of seconds after which nextAction must
	 * return.
	 */
	public void init(String role, int width, int height, int playclock) {
		System.out.println("IN INIT");
		this.role = role;
		this.playclock = playclock;
		myTurn = !role.equals("white");
		this.width = width;
		this.height = height;
		currTime = 0;
		// Initializing board
		char[][] grid = new char[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (y < 2) {
					grid[x][y] = 'W';
				} else if (y > height - 3) {
					grid[x][y] = 'B';
				} else {
					grid[x][y] = ' ';
				}

			}
		}
		state = new State(grid, !myTurn);
	}

	// lastMove is null the first time nextAction gets called (in the initial state)
	// otherwise it contains the coordinates x1,y1,x2,y2 of the move that the last
	// player did
	public String nextAction(int[] lastMove) {
		if (lastMove != null) {
			int x1 = lastMove[0], y1 = lastMove[1], x2 = lastMove[2], y2 = lastMove[3];
			String roleOfLastPlayer;
			if (myTurn && role.equals("white") || !myTurn && role.equals("black")) {
				roleOfLastPlayer = "white";
			} else {
				roleOfLastPlayer = "black";
			}
			System.out.println(roleOfLastPlayer + " moved from " + x1 + "," + y1 + " to " + x2 + "," + y2);
			for (int i = 0; i < 4; i++) {
				lastMove[i]--;
			}

			state = state.nextState(lastMove);
			printGrid(state.grid);

		}

		// update turn (above that line it myTurn is still for the previous state)
		myTurn = !myTurn;
		if (myTurn) {
			// TODO: 2. run alpha-beta search to determine the best move
			int[] move = getBestMove();
			// Here we just construct a random move (that will most likely not even be
			// possible),
			// this needs to be replaced with the actual best move.
			// List<int[]> moves = state.availableMoves();
			String moveString = "(move " + (move[0] + 1) + " " + (move[1] + 1) + " " + (move[2] + 1) + " "
					+ (move[3] + 1) + ")";
			System.out.println("OUR MOVE:  " + moveString);
			return moveString;
		} else {
			return "noop";
		}
	}

	private int[] getBestMove() {
		int[] move = new int[4];
		int h = 1;
		// TODO: JOI VEIT
		this.currTime = System.currentTimeMillis();
		try {
			while (move[3] != height - 1) {
				move = ABSearchRoot(h);
				if (move[3] == height) {
					System.out.println("Win move");
				}
				h++;
			}
		} catch (Exception exception) {
			System.out.println("Exception: " + exception.getMessage());
			System.out.println("DID NOT FINISH AT DEPTH = " + h);
		}
		return move;
	}

	int ABSearch(State lastState, State currState, int alpha, int beta, int h, boolean max) throws Exception {
		// Check if time has run out
		if (System.currentTimeMillis() - this.currTime > this.playclock * 1000 - 100) {
			throw new Exception("Time ran out");
		}

		// TODO: ORDER MOVES SO THAT THE PRUNING WILL PRUNE MORE
		List<int[]> moves = currState.availableMoves();
		if (currState.isTerminal) {
			boolean isWhite = role.equals("white");
			if ((currState.winner == 'W' && isWhite) || (currState.winner == 'B' && !isWhite)) {
				return 100;
			} else if ((currState.winner == 'B' && isWhite) || (currState.winner == 'W' && !isWhite)) {
				return -100;
			}
			return 0;
		}
		if (h == 0) {
			return evaluateState(lastState, currState);
		}

		// successor state value
		int v;
		int bestValue = max ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		for (int[] move : moves) {
			// TODo: INSTEAD OF DOING NEXTSTATE, TO SAVE MEMORY DO DOMOVE
			// value = ABSearch(currState.nextState(move), -beta, -alpha, h - 1, !max);
			v = ABSearch(currState, currState.nextState(move), alpha, beta, h - 1, !max);
			// System.out.println("========================================");
			// System.out.println("DEPTH: " + h);
			// System.out.println("THE ALPHA: " + alpha);
			// System.out.println("THE BETA: " + beta);
			// System.out.println("THE VALUE: " + v);
			// System.out.println("========================================");
			// TODo: AND UNDOMOVE HERE
			if (max) {
				if (v > bestValue) {
					bestValue = v;
				}
				if (v >= beta) {
					return v;
				}
				if (v > alpha) {
					alpha = v;
				}
			} else {
				if (v < bestValue) {
					bestValue = v;
				}
				if (v <= alpha) {
					return v;
				}
				if (v < beta) {
					beta = v;
				}
			}
		}
		return bestValue;
	}

	int evaluateState(State lastState, State evalState) {
		int blackDist = 0;
		int whiteDist = 0;
		int y = 0;
		boolean found = false;
		while (y < this.height && !found) {
			for (int x = 0; x < this.width; x++) {
				if (evalState.grid[x][y] == 'B') {
					blackDist = y;
					found = true;
					break;
				}
			}
			y++;
		}

		found = false;
		y = state.grid[0].length - 2;
		while (y > 0 && !found) {
			for (int x = 0; x < state.grid.length; x++) {
				if (evalState.grid[x][y] == 'W') {
					whiteDist = state.grid[0].length - y - 1;
					found = true;
					break;
				}
			}
			y--;
		}

		// printGrid(evalState.grid);
		// System.out.println("WhiteDist: " + whiteDist);
		// System.out.println("BlackDist: " + blackDist);

		// System.out.println(role.equals("white") ? (blackDist - whiteDist) : whiteDist
		// - blackDist);

		// if we are black then return opposite
		// return role.equals("white") ? (blackDist - whiteDist) : whiteDist -
		// blackDist;
		return role.equals("white") ? (blackDist - whiteDist) + (evalState.whitePawns - evalState.blackPawns)
				: (whiteDist - blackDist) + (evalState.blackPawns - evalState.whitePawns);
	}

	int[] ABSearchRoot(int h) throws Exception {
		int alpha = Integer.MIN_VALUE + 1;
		int beta = Integer.MAX_VALUE;
		int maxVal = -101;
		int[] bestMove = new int[4];
		// System.out.println(h);
		for (int[] move : state.availableMoves()) {
			int value = ABSearch(state, state.nextState(move), alpha, beta, h, false);
			if (value > maxVal) {
				maxVal = value;
				bestMove = move;
				alpha = value;
			}
		}
		return bestMove;
	}

	// is called when the game is over or the match is aborted
	@Override
	public void cleanup() {
		// TODO: cleanup so that the agent is ready for the next match
	}

	public void printGrid(char[][] ngrid) {
		System.out.println();
		System.out.print("  ");
		for (int i = 1; i <= ngrid.length; i++) {
			System.out.print(" " + i);
		}
		System.out.println();
		for (int y = ngrid[0].length - 1; y >= 0; y--) {
			System.out.print(" " + (y + 1) + " ");
			for (int x = 0; x < ngrid.length; x++) {
				System.out.print(ngrid[x][y] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

}
