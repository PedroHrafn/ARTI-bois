
class Game(object):

    def __init__(self, size=3):
        self.reset(size)

    def _initBoard(self):
        self.board = []
        # For each row in big board
        for _ in range(self.size):
            big_row = []
            # For each cell in row in big Board
            for _ in range(self.size):
                big_row.append(self._makeSmallBoard())
            self.board.append(big_row)

        self.big_won["board"] = self._makeSmallBoard()

    def _makeSmallBoard(self):
        row = ["" for _ in range(self.size)]
        small_board = []
        # For each row in small board
        for _ in range(self.size):
            # slice for shallow copy
            small_board.append(row[:])
        return small_board

    def _next_big(self, big_col, big_row):
        return self.next_big != [] and self.next_big != [big_col, big_row]

    def _legal_move(self, big, small):
        """
        Tests if move is legal, return Boolean, big_row, big_col, small_row, small_col
        """
        try:
            big = int(big)
            small = int(small)
            upper_boundary = self.size**2
            lower_boundary = 0

            big_row = big // self.size
            big_col = big % self.size
            small_row = small // self.size
            small_col = small % self.size

            if big > upper_boundary or big < lower_boundary:
                raise Exception("Out of bounds on big board")
            if small > upper_boundary or small < lower_boundary:
                raise Exception("Out of bounds on small board")
            if small not in self.big_won["tiles"] and self._next_big(big % self.size, big // self.size):
                raise Exception("Wrong cell on Big Board")
            if big in self.big_won["tiles"]:
                raise Exception("Cell already won")

            return big_row, big_col, small_row, small_col
        except Exception as e:
            print(f"Illegal move: {e}")
            return []

    def make_move(self, big, small):
        """
        Takes in an index for both big and small board, returns a success, winner
        """

        if self.game_over:
            return False, self.big_won["winner"]

        # Get indexes and if valid input
        ok = self._legal_move(big, small)

        if not ok:
            return False, self.big_won["winner"]

        big_row, big_col, small_row, small_col = ok

        # Alter Board
        symbol = 'X' if self.x_turn else 'O'
        self.board[big_row][big_col][small_row][small_col] = symbol
        if small in self.big_won["tiles"]:
            self.next_big = []
        else:
            self.next_big = [small_col, small_row]

       # Collect won matches TODO: Check if this works
        if self.check_winner(self.board[big_row][big_col]):
            if self.x_turn:
                self.big_won["board"][big_row][big_col] = "X"
            else:
                self.big_won["board"][big_row][big_col] = "O"

            self.big_won["tiles"].append(big)

        # Game over if winner or draw!
        if self.check_winner(self.big_won["board"]):
            self.game_over = True
            self.big_won["winner"] = "X" if self.x_turn else "O"
            return True, self.big_won["winner"]

        if self.move_counter == (self.size**2)**2:
            self.game_over = True
            self.big_won["winner"] = "D"
            return True, self.big_won["winner"]

        self.x_turn = not self.x_turn
        self.move_counter += 1
        return True, self.big_won["winner"]

    def check_winner(self, grid):
        win = False
        win |= self._horizontal_win(grid)
        win |= self._vertical_win(grid)
        win |= self._diagonal_win(grid)
        return win

    def reset(self, size=3):
        self.size = size
        self.big_won = {"board": [], "tiles": [], "winner": ""}
        self._initBoard()
        self.x_turn = True
        self.game_over = False
        self.move_counter = 0

        # What cell next player does, if [] then any.
        self.next_big = []  # [col, row]

    def _horizontal_win(self, grid):
        x_winner = "X"*self.size
        o_winner = "O"*self.size
        for row in grid:
            s_row = ''.join(row)
            if s_row == x_winner or o_winner == s_row:
                return True
        return False

    def _vertical_win(self, grid):
        for i in range(self.size):
            x_counter = 0
            o_counter = 0
            for j in range(self.size):
                if grid[j][i] == "X":
                    x_counter += 1
                if grid[j][i] == "O":
                    o_counter += 1
            if x_counter == self.size or o_counter == self.size:
                return True
        return False

    def _diagonal_win(self, grid):
        x_counter = 0
        o_counter = 0
        # Check right diagonal
        for i in range(self.size):
            if grid[i][i] == "X":
                x_counter += 1
            if grid[i][i] == "O":
                o_counter += 1
            if x_counter == self.size or o_counter == self.size:
                return True

        x_counter = 0
        o_counter = 0
        # Check left diagonal by reversing range
        for i in range(self.size):
            if grid[i][self.size - 1 - i] == "X":
                x_counter += 1
            if grid[i][self.size - 1 - i] == "O":
                o_counter += 1
            if x_counter == self.size or o_counter == self.size:
                return True
        return False


if __name__ == "__main__":
    # debugging, X wins top left Cell
    game = Game()

    # test diagonal win
    print("Checking diagonal")
    board = [["X", "", ""], ["", "X", ""], ["", "", "X"]]
    print(game._diagonal_win(board))
    print("Checking inverse diagonal")
    board = [["", "", "X"], ["", "X", ""], ["X", "", ""]]
    print(game._diagonal_win(board))
    # print("Move 1: 0,0")
    # print(game.make_move(0, 0))
    # game.reset()
    # print("Move 2: 0,3")
    # print(game.make_move(0, 3))
    # print("Move 3: 3,3")
    # print(game.make_move(3, 3))
    # print("Move 4: 3,0")
    # print(game.make_move(3, 0))
    # print("Move 5: 0,1")
    # print(game.make_move(0, 1))
    # print("Move 6: 1,0")
    # print(game.make_move(1, 0))
    # print("Move 6: 0,2")
    # print(game.make_move(0, 2))
    # print("Move 6: 2,0")
    # print(game.make_move(2, 0))
    # print("Move 6: 0,2")
    # print(game.make_move(0, 4))

    for row in game.board:
        print(row)
    # game.make_move(0, 1)
    # game.make_move(0, 1)
    # game.make_move(0, 1)
    # game.make_move(0, 1)
    # game.make_move(0, 1)
    # game.make_move(0, 1)
