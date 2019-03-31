import time

LOSS = -100
WIN = 100
DRAW = 0

class Agent(object):
    def __init__(self, playclock, state):
        self.start = time.time()  # Used for stopping the search when the time has run out
        self.symbol = "O"
        self.last_move = [] #TODO: check if this is correctly initialized
        self.won = []
        self.playclock = playclock
        self.state = state
    
    def nextAction(self):
        self.start = time.time()
        h = 1 #TODO: find better starting value for depth
        move = []
        value = float("-inf")
        try:
            while value != WIN:
                value, move = self.abSearchRoot(h)
                h += 1
        except:
            print(f"Stopped at height: {h}")

        return self.state.flatten_move(move)
    
    def abSearchRoot(self, h):
        alpha = float("-inf")
        beta = float("inf")
        max_value = float("-inf")
        best_move = [] 

        for move in self.state.availableMoves():
            value = self.abSearch(move, self.last_move,alpha, beta, h, True)
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
        elif self.state.won != "D" and self.state.won != self.symbol:
            return LOSS
        elif self.state.won:
            return DRAW

        # Check if max depth is reached
        if h == 0:
            # took in laststate and 
            return self.evaluateState()

        # Apply alpha beta pruning
        best_value = float("-inf") if maximize else float("inf")
        for move in self.state.availableMoves():
            # Get next state, state changes when calling makeMove
            prev_next_big = self.state.next_big
            self.state.makeMove(move)
            v = self.abSearch(move, self.state.next_big, alpha, beta, h - 1, not maximize)
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
        return 
    