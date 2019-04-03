from state import State


class Game(object):
    size = 3

    def __init__(self):
        self.state = State(self._initBoard(), True, '', [], self.size)
        self.moves_made = 0

    def _next_big(self, big_col, big_row):
        return self.state.next_big != [] and self.state.next_big != [big_col, big_row]

    def _initBoard(self):
        board = [[self._makeSmallBoard() for _ in range(self.size)]
                 for _ in range(self.size)]
        return board

    def _makeSmallBoard(self):
        small_board = {}
        small_board["board"] = [
            ["" for _ in range(self.size)] for i in range(self.size)]
        small_board["status"] = ''
        small_board["score"] = 0
        small_board["count"] = 0
        return small_board

    def reset(self, size=3):
        self.__init__()

    def make_move(self, big, small):
        """
        Takes in an index for both big and small board, returns a success, winner
        """
        state = self.state
        if state.won:
            return False, state.won

        # Convert indexes from 1D to 2D
        big_row = big // self.size
        big_col = big % self.size
        small_row = small // self.size
        small_col = small % self.size

        # return False if move was not legal
        available_moves = state.availableMoves()
        if not [big_row, big_col, small_row, small_col] in available_moves:
            return False, state.won

        # make the move
        state.makeMove(big_row, big_col, small_row, small_col)
        self.moves_made += 1
        if self.moves_made == 81:
            state.won = "D"

        return True, state.won
