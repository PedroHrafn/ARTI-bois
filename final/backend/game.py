from state import State


class Game(object):
    size = 3

    def __init__(self):
        self.state = State(self._initBoard(), True, '', [], self.size)

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

        # Game over if winner or draw!
        """ if self.check_winner(state.big_won["board"]):
            state.game_over = True
            state.big_won["winner"] = "X" if state.x_turn else "O"
            return True, state.big_won["winner"]

        # TODO: put D in big won when small board ends in draw

        state.x_turn = not state.x_turn
        return True, state.big_won["winner"] """