from state import State


class Game(object):
    size = 3

    def __init__(self):
        self.state = State(self._initBoard(), True, '', [], size)

    def _next_big(self, big_col, big_row):
        return self.state.next_big != [] and self.state.next_big != [big_col, big_row]

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
            if small not in self.state.big_won["tiles"] and self._next_big(big % self.size, big // self.size):
                raise Exception("Wrong cell on Big Board")
            if big in self.state.big_won["tiles"]:
                raise Exception("Cell already won")

            return big_row, big_col, small_row, small_col
        except Exception as e:
            print(f"Illegal move: {e}")
            return []

    def _initBoard(self):
        board = [[self._makeSmallBoard() for _ in range(self.size)]
                 for _ in range(self.size)]
        return board

    def _makeSmallBoard(self):
        small_board = {}
        small_board["board"] = [
            ["" for _ in range(self.size)] for i in range(self.size)]
        small_board["status"] = ''
        small_board["count"] = 0
        return small_board

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

    def reset(self, size=3):
        self.__init__()

    def check_winner(self, grid):
        win = False
        win |= self._horizontal_win(grid)
        win |= self._vertical_win(grid)
        win |= self._diagonal_win(grid)
        return win

    def make_move(self, big, small):
        """
        Takes in an index for both big and small board, returns a success, winner
        """
        state = self.state
        if state.won:
            return False, state.won

        # Get indexes and if valid input
        available_moves = state.availableMoves()
        big_row = big // self.size
        big_col = big % self.size
        small_row = small // self.size
        small_col = small % self.size

        # could not work!! --- - - -
        if not [big_row, big_col, small_row, small_col] in available_moves:
            return False, state.won

        state.makeMove(big_row, big_col, small_row, small_col)
        return True, state.won
       # Collect won matches TODO: Check if this works

        # Game over if winner or draw!
        """ if self.check_winner(state.big_won["board"]):
            state.game_over = True
            state.big_won["winner"] = "X" if state.x_turn else "O"
            return True, state.big_won["winner"]

        # TODO: put D in big won when small board ends in draw

        state.x_turn = not state.x_turn
        return True, state.big_won["winner"] """


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

    for row in game.state.board:
        print(row)
    # game.make_move(0, 1)
    # game.make_move(0, 1)
    # game.make_move(0, 1)
    # game.make_move(0, 1)
    # game.make_move(0, 1)
    # game.make_move(0, 1)
