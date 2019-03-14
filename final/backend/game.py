
class Game(object):

    def __init__(self, size=3):
        self.size = size
        self._initBoard()
        self.x_turn = True
        self.x_big_won = []
        self.o_big_won = []

        # What cell next player does, if [] then any.
        self.next_big = []  # [col, row]

    def _initBoard(self):
        self.board = []
        # Hardcoded rows for each tic tac toe
        row = ["", "", ""]
        size = self.size
        # For each row in big board
        for _ in range(size):
            big_row = []
            # For each cell in row in big Board
            for _ in range(size):
                small_board = []
                # For each row in small board
                for _ in range(size):
                    # slice for shallow copy
                    small_board.append(row[:])
                # Add small board to row
                big_row.append(small_board)
            self.board.append(big_row)

    def _next_big(self, big_col, big_row):
        return self.next_big != [] and self.next_big == [big_col, big_row]

    def _legal_move(self, big, small):
        """
        Tests if move is legal, return Boolean, big_row, big_col, small_row, small_col 
        """
        try:
            big = int(big)
            small = int(small)
            upper_boundary = self.size << 1
            lower_boundary = 0

            big_row = big // self.size
            big_col = big % self.size
            small_row = small // self.size
            small_col = small % self.size

            if big > upper_boundary or big < lower_boundary:
                raise Exception()
            if small > upper_boundary or small < lower_boundary:
                raise Exception()
            # FIXME: this is not working
            if self._next_big(big % self.size, big // self.size):
                raise Exception()
            if big in self.x_big_won or big in self.o_big_won:
                raise Exception()
            return True, big_row, big_col, small_row, small_col
        except:
            print("Illegal move")
            return False

    def make_move(self, big, small):
        """
        Takes in an index, returns the altered board. If the index given is not
        an integer it returns the unaltered board.
        """

        # Get indexes and if valid input
        ok, big_row, big_col, small_row, small_col = self._legal_move(
            big, small)
        if not ok:
            return self.board

        # Alter Board
        symbol = 'X' if self.x_turn else 'O'
        self.board[big_row][big_col][small_row][small_col] = symbol
        self.next_big = [small_col, small_row]

        # Collect won matches TODO: Check if this works
        if self.check_winner(self.board[big_row][big_col]):
            if self.x_turn:
                self.x_big_won.append([big_col, big_row])
            else:
                self.o_big_won.append([big_col, big_row])

        # TODO: Check if winner on big board? from then no moves can be made?

        self.x_turn = not self.x_turn
        return self.board

    def check_winner(self, grid):
        win = False
        win |= self._horizontal_win(grid)
        win |= self._vertical_win(grid)
        win |= self._diagonal_win(grid)
        return win

    def _horizontal_win(self, grid):
        for row in grid:
            s_row = ''.join(row)
            if s_row == "X"*self.size or "O"*self.size:
                return True
        return False

    def _vertical_win(self, grid):
        # TODO: vertical winnos on an griddos
        return False

    def _diagonal_win(self, grid):
        # TODO: diagonal winnos on an griddos
        return False


if __name__ == "__main__":
    # debugging
    game = Game()
    game.make_move(0, 1)
    game.make_move(0, 1)
