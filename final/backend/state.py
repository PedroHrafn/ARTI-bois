class State(object):
    def __init__(self, board, x_turn, won, next_big, size):
        # State variables
        self.big_board = board
        self.x_turn = x_turn
        self.won = ''
        self.size = size
        self.value = 0

        # What cell next player does, if [] then any.
        self.next_big = next_big  # [row, col]

    def availableMoves(self):
        moves = []
        if self.next_big:
            # print(f"nextbig: {self.next_big}")
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
            """
        if small_row == 1 and small_col == 1:
            if self.x_turn:
                small_board["score"] -= 4
            else:
                small_board["score"] += 4
        elif small_row == 0 and small_col == 0 or small_row == 0 and small_col == 2 or small_row == 2 and small_col == 0 or small_row == 2 and small_col == 2:
            if self.x_turn:
                small_board["score"] -= 3
            else:
                small_board["score"] += 3
        else:
            if self.x_turn:
                small_board["score"] -= 2
            else:
                small_board["score"] += 2
        """

        if self.big_board[small_row][small_col]["status"]:
            self.next_big = []
        else:
            self.next_big = [small_row, small_col]
        self.x_turn = not self.x_turn

    def undoMove(self, state):
        self.big_board = state.big_board
        self.next_big = state.next_big
        self.x_turn = state.x_turn
        self.won = state.won
        self.value = state.value

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

    def flatten_move(self, move):
        big_row, big_col, small_row, small_col = move
        return big_row*self.size + big_col, small_row*self.size + small_col

    def copy_state(self):
        newBigBoard = [['' for _ in range(self.size)]
                       for _ in range(self.size)]
        for big_board_row in range(self.size):
            for big_board_col in range(self.size):
                small_board = self.big_board[big_board_row][big_board_col]
                newBigBoard[big_board_row][big_board_col] = {}
                newSmallBoard = newBigBoard[big_board_row][big_board_col]
                newSmallBoard["status"] = small_board["status"]
                newSmallBoard["count"] = small_board["count"]
                newSmallBoard["score"] = small_board["score"]
                newSmallBoard["board"] = [[small_board["board"][row][col]
                                           for col in range(self.size)] for row in range(self.size)]
        return State(newBigBoard, self.x_turn, self.won, self.next_big[:], self.size)

    def print_board(self):
        print(f"next big: {self.next_big}")
        for bigrow in range(3):
            for smallrow in range(3):
                print(
                    f"{ [x if x else ' ' for x in self.big_board[bigrow][0]['board'][smallrow]]} | {[x if x else ' ' for x in self.big_board[bigrow][1]['board'][smallrow]]} | {[x if x else ' ' for x in self.big_board[bigrow][2]['board'][smallrow]]}|")
                print("                |                 |                |")
            print("----------------------------------------------------")
        """ for big_row in self.big_board:
            for small_game in big_row:
                print(f"status: {small_game['status']}")
                for small_row in small_game["board"]:
                    print(small_row)
                print("\n")
            print("\n\n") """
