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
		int h = 2;
		// TODO: JOI VEIT
		try {
			while (h <= 10/*move[3] != 0 || move[3] != height -1*/) {
				move = ABSearchRoot(h);
				
				System.out.println("trying depth = " + h);
				h++;
			}
		} catch (Exception exception) {
			System.out.print("DID NOT FINISH AT DEPTH = " + h);
		}
		return move;
	}

	int ABSearch(State lastState, State currState, int alpha, int beta, int h, boolean max) throws Exception {
		// IF TIME IS UP THROW EXCEPTION

		// TODO: ORDER MOVES SO THAT THE PRUNING WILL PRUNE MORE
		List<int[]> moves = currState.availableMoves();
		if (currState.isTerminal) {
			if (currState.winner == 'W') {
				return 100;
			} else if (currState.winner == 'B') {
				return -100;
			}
			return 0;
		}
		if (h == 0) {
			return evaluateState(currState);
		}

		// Check if time has ran out
		if(this.currTime - System.currentTimeMillis() > this.playclock * 1000) {
			throw new Exception("Time ran out");
		}

		// successor state value
		int v;
		int bestValue = max ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		for (int[] move : moves) {
			// TODo: INSTEAD OF DOING NEXTSTATE, TO SAVE MEMORY DO DOMOVE
			// value = ABSearch(currState.nextState(move), -beta, -alpha, h - 1, !max);
			v = ABSearch(currState, currState.nextState(move), alpha, beta, h - 1, !max);
			// System.out.println("THE ALPHA: " + alpha);
			// System.out.println("THE VALUE: " + beta);
			// System.out.println("THE VALUE: " + v);
			// TODo: AND UNDOMOVE HERE
			if(max) {
				if (v > bestValue) {
					bestValue = v;
				}
				if (v >= beta) {
					return v;
				}
				if(v > alpha) {
					alpha = v;
				}
			} else {
				if(v < bestValue) {
					bestValue = v;
				}
				if(v <= alpha) {
					return v;
				}
				if(v < beta) {
					beta = v;
				}
			}
		}
		return bestValue;
	}



	int evaluateState(State evalState) {
		int blackDist = 0;
		int whiteDist = 0;
		int y = 0;
		boolean found = false;
		while (y < state.grid[0].length || !found) {
			for (int x = 0; x < state.grid.length; x++) {
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
		while (y > 0 || !found) {
			for (int x = 0; x < state.grid.length; x++) {
				if (evalState.grid[x][y] == 'W') {
					whiteDist = state.grid[0].length - y - 1;
					found = true;
					break;
				}
			}
			y--;
		}
		
		// if we are black then return opposite
		return evalState.whiteTurn ? blackDist - whiteDist : whiteDist - blackDist;
	}

	int[] ABSearchRoot(int h) throws Exception {
		int alpha = Integer.MIN_VALUE + 1;
		int beta = Integer.MAX_VALUE;
		int maxVal = -101;
		int[] bestMove = new int[4];
		for (int[] move : state.availableMoves()) {
<<<<<<< HEAD
			int value = ABSearch(state.nextState(move), alpha, beta, h, true);
=======
			int value = ABSearch(state, state.nextState(move), alpha, beta, h);
>>>>>>> b594ad77ba4f97e074c829a04f7d9373544659c8
			if (value > maxVal) {
				maxVal = value;
				bestMove = move;
			}
		}
		System.out.println("Max val: " + maxVal);
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
