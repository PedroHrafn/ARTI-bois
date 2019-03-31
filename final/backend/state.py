class State(object):
    def __init__(self, board, x_turn, won, next_big, size):
        # State variables
        self.big_board = board
        self.x_turn = x_turn
        self.won = ''
        self.size = size

        # What cell next player does, if [] then any.
        self.next_big = next_big  # [row, col]

    def availableMoves(self):
        moves = []
        if self.next_big:
            for row in range(self.size):
                for col in range(self.size):
                    if self.big_board[self.next_big[0]][self.next_big[1]]["board"][row][col] == '':
                        moves.append(
                            [self.next_big[0], self.next_big[1], row, col])
        else:
            for brow, big_board_row in enumerate(self.big_board):
                for bcol, small_board in enumerate(big_board_row):
                    if not small_board["status"]:
                        for row in range(self.size):
                            for col in range(self.size):
                                if not small_board["board"][row][col]:
                                    moves.append([brow, bcol, row, col])
        return moves

    def makeMove(self, big_row, big_col, small_row, small_col):
        small_board = self.big_board[big_row][big_col]
        small_board["count"] += 1
        small_board["board"][small_row][small_col] = 'X' if self.x_turn else 'O'

        if self.checkWinner(small_board["board"], small_row, small_col):
            small_board["status"] = 'X' if self.x_turn else 'O'
            big_board_status = self.big_to_small()

            if self.checkWinner(big_board_status, big_row, big_col):
                self.won = 'X' if self.x_turn else 'O'
                return
        elif small_board["count"] == 9:
            small_board["status"] = 'D'
            # check if it is a draw

        if self.big_board[small_row][small_col]["status"]:
            self.next_big = []
        else:
            self.next_big = [small_row, small_col]
        self.x_turn = not self.x_turn

    def undoMove(self, big_row, big_col, small_row, small_col, prev_next_big, won):
        small_board = self.big_board[big_row][big_col]
        small_board["count"] -= 1
        small_board["board"][small_row][small_col] = ''
        self.next_big = prev_next_big
        self.x_turn = not self.x_turn
        self.won = won

    def checkWinner(self, board, row, col):
        # check if it is row win
        for i in range(self.size):
            if board[row][i] != board[row][col]:
                break
        else:
            return True
        # check if it is col win
        for i in range(self.size):
            if board[i][col] != board[row][col]:
                break
        else:
            return True
        # check for \ diagonal win
        if row == col:
            for i in range(self.size):
                if board[i][i] != board[row][col]:
                    break
            else:
                return True
        # check for / diagonal win
        if self.size - row - 1 == col:
            for i in range(self.size):
                if board[self.size - i - 1][i] != board[row][col]:
                    break
            else:
                return True
        return False

    def big_to_small(self):
        return [[self.big_board[row][col]["status"] for col in range(self.size)] for row in range(self.size)]

    def extract_board(self):
        return [[self.big_board[row][col]["board"] for col in range(self.size)] for row in range(self.size)]