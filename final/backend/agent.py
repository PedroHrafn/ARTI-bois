import time

LOSS = -100
WIN = 100
DRAW = 0


class Agent(object):
    def __init__(self, playclock, state):
        self.start = time.time()  # Used for stopping the search when the time has run out
        self.symbol = "O"
        self.last_move = []  # TODO: check if this is correctly initialized
        self.won = []
        self.playclock = playclock
        self.state = state

    def nextAction(self):
        self.start = time.time()
        h = 1  # TODO: find better starting value for depth
        move = []
        value = float("-inf")
        try:
            while value != WIN:
                value, move = self.abSearchRoot(h)
                print(f"Testing height: {h}")
                h += 1
        except Exception as e:
            print(f"Exception: {e}")
            print(f"Stopped at height: {h}")

        print(self.state.flatten_move(move))
        return self.state.flatten_move(move)
    
    def abSearchRoot(self, h):
        alpha = float("-inf")
        beta = float("inf")
        max_value = float("-inf")
        moves = self.state.availableMoves()
        best_move = moves[0]

        for move in moves:
            value = self.abSearch(move, self.last_move, alpha, beta, h, True)
            if value > max_value:
                max_value = value
                alpha = value
                best_move = move

        return max_value, best_move

    def abSearch(self, move, prev_next_big, alpha, beta, h, maximize):
        # Check if time has ran out
        if time.time() - self.start > self.playclock:
            raise Exception()

        # Check if we have reached a terminal state
        if self.state.won == self.symbol:
            return WIN
        elif self.state.won == "D":
            return DRAW
        elif self.state.won:
            return LOSS

        # Check if max depth is reached
        if h == 0:
            # took in laststate and 
            return self.evaluateState()

        # Apply alpha beta pruning
        best_value = float("-inf") if maximize else float("inf")
        for move in self.state.availableMoves():
            # Get next state, state changes when calling makeMove
            won = self.state.won
            prev_next_big = self.state.next_big
            self.state.makeMove(move[0], move[1], move[2], move[3])
            v = self.abSearch(move, self.state.next_big,
                              alpha, beta, h - 1, not maximize)
            self.state.undoMove(move[0], move[1], move[2], move[3], prev_next_big, won)
            if maximize:
                if v > best_value:
                    best_value = v
                if v >= beta:
                    return v
                if v > alpha:
                    alpha = v
            else:
                if v < best_value:
                    best_value = v
                if v <= alpha:
                    return v
                if v < beta:
                    beta = v
            


        return best_value

    def evaluateState(self):
        score = 0
        for big_row in self.state.big_board:
            for small_board in big_row:
                if small_board["status"] == self.symbol:
                    score += 10
                elif small_board["status"] != '' and small_board["status"] != 'D':
                    score -= 10
        return score
