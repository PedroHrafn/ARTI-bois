import time
import random
LOSS = -100
WIN = 100
DRAW = 0


class Agent(object):
    def __init__(self, playclock, state):
        self.start = time.time()  # Used for stopping the search when the time has run out
        self.symbol = "O"
        self.won = []
        self.playclock = playclock
        self.state = state.copy_state()
        self.max_score = 0

    def nextAction(self, state):
        self.state = state.copy_state()
        self.start = time.time()
        h = 1  # TODO: find better starting value for depth
        # import random
        # return self.state.flatten_move(random.choice(self.state.availableMoves()))
        move = []
        value = float("-inf")
        try:
            while value != WIN:
                value, move = self.abSearchRoot(h)
                print(f"Testing height: {h}")
                h += 1
        except Exception as e:
            print(f"Exception: {e}")
        print(move)
        print(f"AGENT MOVE: {self.state.flatten_move(move)}, VALUE: {value}")
        return self.state.flatten_move(move)

    def abSearchRoot(self, h):
        alpha = float("-inf")
        beta = float("inf")
        max_value = float("-inf")
        moves = self.state.availableMoves()
        best_move = moves[0]
        # print(f"BESSST_MOVE: {best_move}")
        # print(f"absearchRPPT moves: {moves}")
        # print(f"absearchRPPT nextbig: {self.state.next_big}")
        # random.shuffle(moves)
        self.sortMoves(moves)
        for move in moves:
            copystate = self.state.copy_state()
            self.state.makeMove(move[0], move[1], move[2], move[3])
            value = self.abSearch(alpha, beta, h, False, None)
            self.state.undoMove(copystate)
            if value > max_value:
                max_value = value
                alpha = value
                best_move = move

        return max_value, best_move

    def abSearch(self, alpha, beta, h, maximize, last_state):
        # Check if time has ran out
        if time.time() - self.start > self.playclock:
            print(f"Stopped at height: {h}")
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
        moves = self.state.availableMoves()
        self.sortMoves(moves)
        for move in moves:
            # Get next state, state changes when calling makeMove
            old_state = self.state.copy_state()
            self.state.makeMove(move[0], move[1], move[2], move[3])
            v = self.abSearch(alpha, beta, h - 1, not maximize, last_state)
            self.state.undoMove(old_state)
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

    def sortMoves(self, moves):
        moves.sort(key=lambda x: self.moveValue(x), reverse=True)

    def moveValue(self, move):
        smallboard = self.state.big_board[move[0]][move[1]]["board"]
        board = [['' for _ in range(3)] for _ in range(3)]
        for row in range(3):
            for col in range(3):
                board[row][col] = smallboard[row][col]
        row = move[2]
        col = move[3]
        ourMark = 'X' if self.state.x_turn else 'O'
        opponentMark = 'O' if self.state.x_turn else 'X'
        board[row][col] = 'X' if self.state.x_turn else 'O'
        ourOneOff = False
        opponentOneOff = False
        lines = []
        lines.append([board[0][0], board[0][1], board[0][2]])
        lines.append([board[1][0], board[1][1], board[1][2]])
        lines.append([board[2][0], board[2][1], board[2][2]])
        lines.append([board[0][0], board[1][1], board[2][2]])
        lines.append([board[0][0], board[1][0], board[2][0]])
        lines.append([board[0][1], board[1][1], board[2][1]])
        lines.append([board[0][2], board[1][2], board[2][2]])
        lines.append([board[0][2], board[1][1], board[2][0]])
        for line in lines:
            ourMarkCount = line.count(ourMark)
            opponentMarkCount = line.count(opponentMark)
            if ourMarkCount == 2 and opponentMarkCount == 0:
                ourOneOff = True
            elif opponentMarkCount == 2 and ourMarkCount == 0:
                opponentOneOff = True
            if ourOneOff and opponentOneOff:
                return 0
        if ourOneOff:
            return 1
        if opponentOneOff:
            return -1
        return 0

    def evaluateState(self):
        score = 0
        for big_row in self.state.big_board:
            for small_board in big_row:
                if small_board["status"] == self.symbol:
                    score += 10
                elif small_board["status"] != '' and small_board["status"] != 'D':
                    score -= 10
        if self.max_score < score:
            self.max_score = score
            print(score)
        return score
